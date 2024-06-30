package com.rexchoppers.mchunt.listeners;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.events.internal.ArenaSetupUpdatedEvent;
import net.engio.mbassy.listener.Handler;

public class ArenaSetupUpdatedListener {
    private final MCHunt plugin;

    public ArenaSetupUpdatedListener(MCHunt plugin) {
        this.plugin = plugin;
    }

    @Handler
    public void updateArenaSigns(ArenaSetupUpdatedEvent event){

    }

}
