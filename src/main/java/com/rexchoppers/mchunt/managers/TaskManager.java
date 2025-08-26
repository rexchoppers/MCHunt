package com.rexchoppers.mchunt.managers;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.tasks.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public record TaskManager(MCHunt plugin) {

    public void registerTasks() {
        HashMap<Class<?>, Long> tasks = new HashMap<>() {{
            put(ArenaSetupItemVerificationTask.class, 20 * 5L);
            put(ArenaSetupRenderBlocksTask.class, 20L);
            put(SignRenderTask.class, 5L);
            put(StartCountdownTask.class, 20L);
            put(ArenaTick.class, 20L);
        }};

        for (Class<?> clazz : tasks.keySet()) {
            try {
                Long interval = tasks.get(clazz);
                BukkitRunnable task = (BukkitRunnable) clazz.getConstructor(MCHunt.class).newInstance(plugin);
                task.runTaskTimer(plugin, interval, interval);
            } catch (Exception e) {
                throw new RuntimeException("Failed to register task: " + clazz.getName(), e);
            }
        }
    }
}
