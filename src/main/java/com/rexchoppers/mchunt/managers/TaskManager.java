package com.rexchoppers.mchunt.managers;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.tasks.ArenaSetupItemVerificationTask;
import com.rexchoppers.mchunt.tasks.ArenaSetupRenderBlocksTask;
import com.rexchoppers.mchunt.tasks.SignRenderTask;
import com.rexchoppers.mchunt.tasks.StartCountdownTask;
import org.bukkit.Material;

public class TaskManager {
    private final MCHunt plugin;

    public TaskManager(MCHunt plugin) {
        this.plugin = plugin;
    }

    public void registerTasks() {
        new ArenaSetupItemVerificationTask(plugin).runTaskTimer(plugin, 0, 20 * 5);
        new ArenaSetupRenderBlocksTask(plugin).runTaskTimer(plugin, 0, 20L);
        new SignRenderTask(plugin).runTaskTimer(plugin, 0L, 5L);
        new StartCountdownTask(plugin).runTaskTimer(plugin, 0L, 20L);
    }
}
