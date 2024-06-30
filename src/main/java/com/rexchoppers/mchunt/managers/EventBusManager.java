package com.rexchoppers.mchunt.managers;

import com.rexchoppers.mchunt.MCHunt;
import net.engio.mbassy.bus.MBassador;

public class EventBusManager {
    private final MBassador<Object> eventBus;
    private final MCHunt plugin;

    public EventBusManager(MCHunt plugin) {
        this.eventBus = new MBassador<>();
        this.plugin = plugin;
        // Register all event listeners

    }

    public void publishEvent(Object event) {
        eventBus.publish(event);
    }
}
