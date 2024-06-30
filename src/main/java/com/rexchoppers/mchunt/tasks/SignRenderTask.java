package com.rexchoppers.mchunt.tasks;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.models.ArenaSetup;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class SignRenderTask extends BukkitRunnable {
    private final MCHunt plugin;

    public SignRenderTask(MCHunt plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        List<ArenaSetup> arenaSetups = plugin.getArenaSetupManager().getArenaSetups();

        // Render arena setup signs
        for (ArenaSetup arenaSetup : arenaSetups) {
            if (arenaSetup.getArenaSigns() == null) continue;;
            // Render arena setup signs
            for (int i = 0; i < arenaSetup.getArenaSigns().length; i++) {

            }
        }
    }
}
