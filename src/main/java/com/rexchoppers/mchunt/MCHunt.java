package com.rexchoppers.mchunt;

import co.aikar.commands.PaperCommandManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rexchoppers.mchunt.commands.CommandMCHunt;
import com.rexchoppers.mchunt.enums.ArenaStatus;
import com.rexchoppers.mchunt.managers.*;
import com.rexchoppers.mchunt.serializers.*;
import fr.minuskube.inv.InventoryManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.FileSystems;
import java.util.Locale;

public final class MCHunt extends JavaPlugin {

    private Gson gson;

    private static Locale currentLocale;

    private InventoryManager inventoryManager;
    private ArenaManager arenaManager;
    private ArenaSetupManager arenaSetupManager;
    private ItemManager itemManager;
    
    private EventManager eventManager;

    private PacketManager packetManager;

    private TaskManager taskManager;

    @Override
    public void onEnable() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(ArenaStatus.class, new ArenaStatusSerializer())
                .registerTypeAdapter(ItemStack[].class, new ItemStackArraySerializer())
                .registerTypeAdapter(ItemStack[].class, new ItemStackArrayDeserializer())
                .registerTypeAdapter(Location.class, new LocationSerializer())
                .registerTypeAdapter(Location.class, new LocationDeserializer())
                .registerTypeAdapter(Location[].class, new LocationArraySerializer())
                .registerTypeAdapter(Location[].class, new LocationArrayDeserializer())
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        currentLocale = Locale.getDefault();



        // Setup managers
        this.arenaManager = new ArenaManager(this,
                this.getDataFolder().getAbsolutePath() + FileSystems.getDefault().getSeparator() + "arenas.json"
        );

        this.arenaSetupManager = new ArenaSetupManager(this,
                this.getDataFolder().getAbsolutePath() + FileSystems.getDefault().getSeparator() + "arenaSetup.json"
        );

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
}
