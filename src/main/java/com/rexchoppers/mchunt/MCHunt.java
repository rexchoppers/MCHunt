package com.rexchoppers.mchunt;

import co.aikar.commands.PaperCommandManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rexchoppers.mchunt.adapters.InstantTypeAdapter;
import com.rexchoppers.mchunt.commands.CommandMCHunt;
import com.rexchoppers.mchunt.managers.*;
import com.rexchoppers.mchunt.models.Arena;
import com.rexchoppers.mchunt.serializers.*;
import com.rexchoppers.mchunt.util.Format;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import fr.minuskube.inv.InventoryManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.nio.file.FileSystems;
import java.security.*;
import java.time.Instant;
import java.util.Locale;

public final class MCHunt extends JavaPlugin {

    private Gson gson;

    private static Locale currentLocale;

    private InventoryManager inventoryManager;
    private ArenaRepository arenaManager;
    private ArenaSetupRepository arenaSetupManager;
    private ItemManager itemManager;
    private EventManager eventManager;

    private EventBusManager eventBusManager;

    private PacketManager packetManager;

    private TaskManager taskManager;

    private SignManager signManager;

    private WorldGuard worldGuard;

    @Override
    public void onEnable() {
        // Set the current locale
        currentLocale = Locale.getDefault();

        Bukkit.getConsoleSender().sendMessage(
                Format.processString(
                    new LocalizationManager(MCHunt.getCurrentLocale())
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

        WorldGuardPlugin wgPlugin = getWorldGuard();
        if (wgPlugin == null) {
            getLogger().severe("WorldGuard plugin not found. Disabling plugin.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // Create MCHunt directory if it doesn't exist
        File pluginDir = new File(this.getDataFolder().getAbsolutePath());
        if (!pluginDir.exists()) {
            pluginDir.mkdirs();
        }

        /*
          Create the default configuration. We won't use this much
         */
        saveDefaultConfig();



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


        this.packetManager = new PacketManager(this);
        this.packetManager.registerPackets();

        this.taskManager = new TaskManager(this);
        this.taskManager.registerTasks();

        this.eventManager = new EventManager(this);
        this.eventManager.registerEvents();

        this.itemManager = new ItemManager(this);

        inventoryManager = new InventoryManager(this);
        inventoryManager.init();

        // Setup commands
        PaperCommandManager manager = new PaperCommandManager(this);
        manager.registerCommand(new CommandMCHunt(this));

        Bukkit.getConsoleSender().sendMessage(
                Format.processString(
                    new LocalizationManager(MCHunt.getCurrentLocale())
                            .getMessage("mchunt.startup_complete")
                )
        );
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(
                Format.processString(
                    new LocalizationManager(MCHunt.getCurrentLocale())
                            .getMessage("mchunt.shutdown_initiated")
                )
        );

        Bukkit.getScheduler().cancelTasks(this);

        Bukkit.getConsoleSender().sendMessage(
                Format.processString(
                        new LocalizationManager(MCHunt.getCurrentLocale())
                                .getMessage("mchunt.shutdown_complete")
                )
        );
    }

    public Gson getGson() {
        return gson;
    }

    public static Locale getCurrentLocale() {
        return currentLocale;
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

    public EventManager getEventManager() {
        return eventManager;
    }

    public PacketManager getPacketManager() {
        return packetManager;
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

    public WorldGuardPlugin getWorldGuard() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
        if (!(plugin instanceof WorldGuardPlugin)) {
            return null;
        }
        return (WorldGuardPlugin) plugin;
    }
}
