package com.rexchoppers.mchunt;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rexchoppers.mchunt.enums.ArenaStatus;
import com.rexchoppers.mchunt.exceptions.ArenaExistsException;
import com.rexchoppers.mchunt.managers.ArenaManager;
import com.rexchoppers.mchunt.models.Arena;
import com.rexchoppers.mchunt.serializers.ArenaStatusSerializer;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.FileSystems;
import java.util.Locale;
import java.util.UUID;

public final class MCHunt extends JavaPlugin {

    private Gson gson;

    private static Locale currentLocale;
    private ArenaManager arenaManager;

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

        try {
            this.arenaManager.createArena(new Arena(UUID.randomUUID().toString(), "Arena 1"));
        } catch (ArenaExistsException e) {
            throw new RuntimeException(e);
        }
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
}
