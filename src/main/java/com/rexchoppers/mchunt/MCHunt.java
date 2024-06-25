package com.rexchoppers.mchunt;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rexchoppers.mchunt.enums.ArenaStatus;
import com.rexchoppers.mchunt.managers.ArenaManager;
import com.rexchoppers.mchunt.serializers.ArenaStatusSerializer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;

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
        this.arenaManager = new ArenaManager(this, "arenas.json");
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
