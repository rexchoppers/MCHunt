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
        Class<?>[] listenerClasses = {
                ArenaSetupUpdatedListener.class,
                ArenaSetupDiscardedListener.class,
                ArenaCreatedListener.class,
                PlayerJoinedArenaListener.class,
                PlayerLeftArenaListener.class,
                ArenaSeekersReleasedListener.class,
                ArenaStartedListener.class,
                HiderIsStillListener.class,
                HiderHasMovedListener.class,
                ArenaFinishedListener.class,
                ArenaResetCountdownEndedListener.class,
                PlayerDiedListener.class,
        };

        for (Class<?> clazz : listenerClasses) {
            try {
                Object listener = clazz.getConstructor(MCHunt.class).newInstance(plugin);
                this.eventBus.register(listener);
            } catch (Exception e) {
                throw new RuntimeException("Failed to register listener: " + clazz.getName(), e);
            }
        }
    }

    public void publishEvent(Object event) {
        eventBus.post(event);
    }
}
