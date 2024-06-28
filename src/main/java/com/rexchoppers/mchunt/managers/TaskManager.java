package com.rexchoppers.mchunt.managers;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.tasks.ArenaSetupItemVerificationTask;

public class TaskManager {
    private final MCHunt plugin;

    public TaskManager(MCHunt plugin) {
        this.plugin = plugin;
    }

    public void registerTasks() {
        new ArenaSetupItemVerificationTask(plugin).runTaskTimer(plugin, 0, 20 * 5);
    }
}
