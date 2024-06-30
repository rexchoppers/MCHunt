package com.rexchoppers.mchunt.listeners;

import com.google.common.eventbus.Subscribe;
import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.events.internal.ArenaSetupUpdatedEvent;
import com.rexchoppers.mchunt.models.ArenaSetup;
import org.bukkit.Bukkit;

public class ArenaSetupUpdatedListener {
    private final MCHunt plugin;

    public ArenaSetupUpdatedListener(MCHunt plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void updateArenaSigns(ArenaSetupUpdatedEvent event){
        ArenaSetup arenaSetup = this.plugin.getArenaSetupManager().getArenaSetupByUUID(event.arenaSetupUuid()).orElse(null);
        if (arenaSetup == null) return;
        plugin.getSignManager().initArenaSetupSigns(arenaSetup);
    }
}