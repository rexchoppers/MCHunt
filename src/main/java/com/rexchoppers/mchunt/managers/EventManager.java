package com.rexchoppers.mchunt.managers;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.events.ArenaSetupEventHandler;
import org.bukkit.Bukkit;

public class EventManager {
    private final MCHunt plugin;

    public EventManager(MCHunt plugin) {
        this.plugin = plugin;
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new ArenaSetupEventHandler(), plugin);
    }
}
