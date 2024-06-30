package com.rexchoppers.mchunt.managers;

import com.google.common.eventbus.EventBus;
import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.listeners.ArenaSetupUpdatedListener;
import org.bukkit.Bukkit;

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
    }

    public void publishEvent(Object event) {
        eventBus.post(event);
    }
}
