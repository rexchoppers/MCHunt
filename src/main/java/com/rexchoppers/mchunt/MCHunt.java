package com.rexchoppers.mchunt;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rexchoppers.mchunt.adapters.InstantTypeAdapter;
import com.rexchoppers.mchunt.managers.*;
import com.rexchoppers.mchunt.models.Arena;
import com.rexchoppers.mchunt.models.ArenaPlayer;
import com.rexchoppers.mchunt.menus.MenuInGame;
import com.rexchoppers.mchunt.menus.MenuMain;
import com.rexchoppers.mchunt.serializers.*;
import com.rexchoppers.mchunt.util.Format;
import com.rexchoppers.mchunt.util.PlayerUtil;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import fr.minuskube.inv.InventoryManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.nio.file.FileSystems;
import java.time.Instant;
import java.util.Locale;

public final class MCHunt extends JavaPlugin { // also acts as CommandExecutor

    private Gson gson;

    private InventoryManager inventoryManager;
    private ArenaRepository arenaManager;
    private ArenaSetupRepository arenaSetupManager;
    private ItemManager itemManager;

    private EventBusManager eventBusManager;

    private TaskManager taskManager;

    private SignManager signManager;

    private static LocalizationManager localizationManager;

    public static LocalizationManager getLocalization() {
        return localizationManager;
    }

    @Override
    public void onEnable() {
        // Init localization manager to load the default locale file
        localizationManager = new LocalizationManager(Locale.getDefault());

        Bukkit.getConsoleSender().sendMessage(
                Format.processString(
                    MCHunt.getLocalization()
                            .getMessage("mchunt.startup_initiated")
                )
        );

        this.gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class, new InstantTypeAdapter())
                .registerTypeAdapter(Arena.class, new ArenaDeserializer())
                .registerTypeAdapter(ItemStack[].class, new ItemStackArraySerializer())
                .registerTypeAdapter(ItemStack[].class, new ItemStackArrayDeserializer())
                .registerTypeAdapter(Location[].class, new LocationArraySerializer())
                .registerTypeAdapter(Location[].class, new LocationArrayDeserializer())
                .registerTypeAdapter(Location.class, new LocationSerializer())
                .registerTypeAdapter(Location.class, new LocationDeserializer())
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        if(!checkDependencies()) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Create MCHunt directory if it doesn't exist
        File pluginDir = new File(this.getDataFolder().getAbsolutePath());
        if (!pluginDir.exists()) {
            pluginDir.mkdirs();
        }

        // Setup managers
        this.signManager = new SignManager();

        this.arenaManager = new ArenaRepository(
                this,
                this.getDataFolder().getAbsolutePath() + FileSystems.getDefault().getSeparator() + "arenas"
        );

        this.arenaSetupManager = new ArenaSetupRepository(this,
                this.getDataFolder().getAbsolutePath() + FileSystems.getDefault().getSeparator() + "arena_setup"
        );

        this.eventBusManager = new EventBusManager(this);

        (new PacketManager(this)).registerPackets();

        this.taskManager = new TaskManager(this);
        this.taskManager.registerTasks();

        (new EventManager(this)).registerEvents();

        this.itemManager = new ItemManager(this);

        inventoryManager = new InventoryManager(this);
        inventoryManager.init();

        // Setup commands (register Bukkit command executor)
        if (getCommand("mchunt") != null) {
            getCommand("mchunt").setExecutor(this);
            getCommand("mchunt").setTabCompleter((sender, command, alias, args) -> java.util.Collections.emptyList());
        }
        if (getCommand("mch") != null) {
            getCommand("mch").setExecutor(this);
            getCommand("mch").setTabCompleter((sender, command, alias, args) -> java.util.Collections.emptyList());
        }

        Bukkit.getConsoleSender().sendMessage(
                Format.processString(
                    MCHunt.getLocalization()
                            .getMessage("mchunt.startup_complete")
                )
        );
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(
                Format.processString(
                    MCHunt.getLocalization()
                            .getMessage("mchunt.shutdown_initiated")
                )
        );

        // Teleport all players currently in arenas to the after-game spawn
        // Arena states will be cleared on next startup anyway
        for (Arena arena : this.arenaManager.getData()) {
            if (arena == null || arena.getPlayers() == null) continue;

            for (ArenaPlayer arenaPlayer : arena.getPlayers()) {
                Player serverPlayer = Bukkit.getPlayer(arenaPlayer.getUUID());
                if (serverPlayer == null || !serverPlayer.isOnline()) continue;

                serverPlayer.teleport(arena.getAfterGameSpawn());

                // Tell the player the arena has been shut down so their game has been ended
                PlayerUtil.sendPlayerError(
                        serverPlayer,
                        MCHunt.getLocalization().getMessage("arena.shutdown")
                );
            }
        }

        // Cancel any scheduled tasks for this plugin
        Bukkit.getScheduler().cancelTasks(this);

        Bukkit.getConsoleSender().sendMessage(
                Format.processString(
                        MCHunt.getLocalization()
                                .getMessage("mchunt.shutdown_complete")
                )
        );
    }

    public Gson getGson() {
        return gson;
    }

    public ArenaRepository getArenaManager() {
        return arenaManager;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public ArenaSetupRepository getArenaSetupManager() {
        return arenaSetupManager;
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }

    public SignManager getSignManager() {
        return signManager;
    }

    public EventBusManager getEventBusManager() {
        return eventBusManager;
    }

    private boolean checkDependencies() {
        if (getWorldGuard() == null) {
            getLogger().severe(MCHunt.getLocalization().getMessage("mchunt.plugin_not_found", "WorldGuard"));

            return false;
        }

        if (!getServer().getPluginManager().isPluginEnabled("LibsDisguises")) {
            getLogger().severe(MCHunt.getLocalization().getMessage("mchunt.plugin_not_found", "LibsDisguises"));

            return false;
        }

        if (!getServer().getPluginManager().isPluginEnabled("ProtocolLib")) {
            getLogger().severe(MCHunt.getLocalization().getMessage("mchunt.plugin_not_found", "ProtocolLib"));
            return false;
        }


        return true;
    }

    public WorldGuardPlugin getWorldGuard() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
        if (!(plugin instanceof WorldGuardPlugin)) {
            return null;
        }
        return (WorldGuardPlugin) plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Only handle our commands
        String name = command.getName().toLowerCase();
        if (!name.equals("mchunt") && !name.equals("mch")) {
            return false;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;

        if (this.getArenaManager().getArenaPlayerIsIn(player.getUniqueId()).isPresent()) {
            new MenuInGame(this).getInventory().open(player);
            return true;
        }
        new MenuMain(this).open(player);
        return true;
    }
}
