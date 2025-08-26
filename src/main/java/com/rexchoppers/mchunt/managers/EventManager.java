package com.rexchoppers.mchunt.managers;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.events.ArenaEventHandler;
import com.rexchoppers.mchunt.events.ArenaSetupEventHandler;
import com.rexchoppers.mchunt.events.ArenaSignEventHandler;
import com.rexchoppers.mchunt.events.DroppableEventHandler;
import com.rexchoppers.mchunt.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public record EventManager(MCHunt plugin) {
    public void registerEvents() {
        Class<?>[] handlerClasses = {
                ArenaSetupEventHandler.class,
                ArenaSignEventHandler.class,
                ArenaEventHandler.class,
                DroppableEventHandler.class
        };

        for (Class<?> clazz : handlerClasses) {
            try {
                Object listener = clazz.getConstructor(MCHunt.class).newInstance(this.plugin);
                Bukkit.getPluginManager().registerEvents((Listener) listener, this.plugin);
            } catch (Exception e) {
                throw new RuntimeException("Failed to register handler: " + clazz.getName(), e);
            }
        }
    }
}