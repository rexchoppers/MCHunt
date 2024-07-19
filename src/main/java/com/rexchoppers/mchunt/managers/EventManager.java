package com.rexchoppers.mchunt.managers;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.events.ArenaSetupEventHandler;
import com.rexchoppers.mchunt.events.ArenaSignEventHandler;
import org.bukkit.Bukkit;

public class EventManager {
    private final MCHunt plugin;

    public EventManager(MCHunt plugin) {
        this.plugin = plugin;
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new ArenaSetupEventHandler(this.plugin), this.plugin);
        Bukkit.getPluginManager().registerEvents(new ArenaSignEventHandler(this.plugin), this.plugin);
    }
}
