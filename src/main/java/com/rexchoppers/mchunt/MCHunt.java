package com.rexchoppers.mchunt;

import co.aikar.commands.PaperCommandManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rexchoppers.mchunt.adapters.InstantTypeAdapter;
import com.rexchoppers.mchunt.commands.CommandMCHunt;
import com.rexchoppers.mchunt.http.ApiClient;
import com.rexchoppers.mchunt.http.requests.RegisterServerRequest;
import com.rexchoppers.mchunt.http.responses.RegisterServerResponse;
import com.rexchoppers.mchunt.managers.*;
import com.rexchoppers.mchunt.models.Arena;
import com.rexchoppers.mchunt.security.ED25519;
import com.rexchoppers.mchunt.serializers.*;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import fr.minuskube.inv.InventoryManager;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.security.*;
import java.time.Instant;
import java.util.Locale;

public final class MCHunt extends JavaPlugin {

    private Gson gson;

    private static Locale currentLocale;

    private InventoryManager inventoryManager;
    private ArenaManager arenaManager;
    private ArenaSetupManager arenaSetupManager;
    private ItemManager itemManager;
    private ApiClient apiClient;

    private EventManager eventManager;

    private EventBusManager eventBusManager;

    private PacketManager packetManager;

    private TaskManager taskManager;

    private SignManager signManager;

    private WorldGuard worldGuard;

    @Override
    public void onEnable() {
        Security.addProvider(new BouncyCastleProvider());

        this.gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class, new InstantTypeAdapter())

                .registerTypeAdapter(Arena.class, new ArenaDeserializer())
                .registerTypeAdapter(ItemStack[].class, new ItemStackArraySerializer())
                .registerTypeAdapter(ItemStack[].class, new ItemStackArrayDeserializer())
                .registerTypeAdapter(Location[].class, new LocationArraySerializer())
                .registerTypeAdapter(Location[].class, new LocationArrayDeserializer())
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

        // Create keys directory if it doesn't exist
        File keysDir = new File(pluginDir.getAbsolutePath() + FileSystems.getDefault().getSeparator() + "keys");
        if (!keysDir.exists()) {
            keysDir.mkdirs();
        }

        /**
         * Create default configuration. We won't use this much
         */
        saveDefaultConfig();

        // Generate keys if they don't exist
        ED25519 ed25519 = new ED25519(this);
        ed25519.generateKeys();

        // Create API client
        this.apiClient = new ApiClient(
                "http://host.docker.internal:8080",
                this.gson
        );

        // Register server
        try {
            // Get contents of public key
            String publicKeyContents = ed25519.getPublicKeyContents();

            RegisterServerRequest registerServerRequest = new RegisterServerRequest(publicKeyContents);
            RegisterServerResponse registerServerResponse = this.apiClient.registerServer(registerServerRequest);

            getConfig().set("server.uuid", registerServerResponse.uuid);
            saveConfig();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        currentLocale = Locale.getDefault();

        // Setup managers
        this.signManager = new SignManager();

        this.arenaManager = new ArenaManager(this,
                this.getDataFolder().getAbsolutePath() + FileSystems.getDefault().getSeparator() + "arenas.json"
        );

        this.arenaSetupManager = new ArenaSetupManager(this,
                this.getDataFolder().getAbsolutePath() + FileSystems.getDefault().getSeparator() + "arenaSetup.json"
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
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
    }

    public Gson getGson() {
        return gson;
    }

    public static Locale getCurrentLocale() {
        return currentLocale;
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public ArenaSetupManager getArenaSetupManager() {
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
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }
        return (WorldGuardPlugin) plugin;
    }
}
