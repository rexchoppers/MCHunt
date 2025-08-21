package com.rexchoppers.mchunt.managers;

import com.google.common.eventbus.EventBus;
import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.listeners.*;

public class EventBusManager {
    private final EventBus eventBus;
    private final MCHunt plugin;

    public EventBusManager(MCHunt plugin) {
        this.eventBus = new EventBus();
        this.plugin = plugin;

        registerListeners();
    }

    private void registerListeners() {
        this.eventBus.register(new ArenaSetupUpdatedListener(this.plugin));
        this.eventBus.register(new ArenaSetupDiscardedListener(this.plugin));
        this.eventBus.register(new ArenaCreatedListener(this.plugin));
        this.eventBus.register(new PlayerJoinedArenaListener(this.plugin));
        this.eventBus.register(new PlayerLeftArenaListener(this.plugin));
        this.eventBus.register(new ArenaSeekersReleasedListener(this.plugin));
        this.eventBus.register(new ArenaStartedListener(this.plugin));
        this.eventBus.register(new HiderIsStillListener(this.plugin));
        this.eventBus.register(new HiderHasMovedListener(this.plugin));
        this.eventBus.register(new ArenaFinishedListener(this.plugin));
    }

    public void publishEvent(Object event) {
        eventBus.post(event);
    }
}
