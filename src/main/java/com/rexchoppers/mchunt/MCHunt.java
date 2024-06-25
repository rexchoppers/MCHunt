package com.rexchoppers.mchunt;

import co.aikar.commands.PaperCommandManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rexchoppers.mchunt.commands.CommandMCHunt;
import com.rexchoppers.mchunt.enums.ArenaStatus;
import com.rexchoppers.mchunt.exceptions.ArenaExistsException;
import com.rexchoppers.mchunt.items.ItemManager;
import com.rexchoppers.mchunt.managers.ArenaManager;
import com.rexchoppers.mchunt.models.Arena;
import com.rexchoppers.mchunt.serializers.ArenaStatusSerializer;
import fr.minuskube.inv.InventoryManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.FileSystems;
import java.util.Locale;
import java.util.UUID;

public final class MCHunt extends JavaPlugin {

    private Gson gson;

    private static Locale currentLocale;

    private InventoryManager inventoryManager;
    private ArenaManager arenaManager;
    private ItemManager itemManager;

    @Override
    public void onEnable() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(ArenaStatus.class, new ArenaStatusSerializer())
                .create();

        currentLocale = Locale.getDefault();



        // Setup managers
        this.arenaManager = new ArenaManager(this,
                this.getDataFolder().getAbsolutePath() + FileSystems.getDefault().getSeparator() + "arenas.json"
        );

        this.itemManager = new ItemManager(this);

        inventoryManager = new InventoryManager(this);
        inventoryManager.init();

        // Setup commands
        PaperCommandManager manager = new PaperCommandManager(this);
        manager.registerCommand(new CommandMCHunt(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
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
}
