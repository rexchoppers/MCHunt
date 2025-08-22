package com.rexchoppers.mchunt.listeners;

import com.google.common.eventbus.Subscribe;
import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.events.internal.ArenaSetupPlayerJoinedEvent;
import com.rexchoppers.mchunt.events.internal.ArenaSetupUpdatedEvent;
import com.rexchoppers.mchunt.models.ArenaSetup;

public record ArenaSetupUpdatedListener(MCHunt plugin) {

    @Subscribe
    public void updateArenaSigns(ArenaSetupUpdatedEvent event) {
        ArenaSetup arenaSetup = this.plugin.getArenaSetupManager().getByUUID(event.arenaSetupUuid()).orElse(null);
        if (arenaSetup == null) return;
        plugin.getSignManager().initArenaSetupSigns(arenaSetup);
    }

    @Subscribe
    public void updateArenaSignsOnArenaSetupPlayerJoin(ArenaSetupPlayerJoinedEvent event) {
        ArenaSetup arenaSetup = this.plugin.getArenaSetupManager().getByUUID(event.arenaSetupUuid()).orElse(null);
        if (arenaSetup == null) return;
        plugin.getSignManager().initArenaSetupSigns(arenaSetup);
    }
}