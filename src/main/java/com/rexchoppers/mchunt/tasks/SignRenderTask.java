package com.rexchoppers.mchunt.tasks;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.managers.SignManager;
import com.rexchoppers.mchunt.models.Arena;
import com.rexchoppers.mchunt.models.ArenaSetup;
import com.rexchoppers.mchunt.signs.ScrollingSign;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class SignRenderTask extends BukkitRunnable {
    private final MCHunt plugin;
    private SignManager signManager;

    public SignRenderTask(MCHunt plugin) {
        this.plugin = plugin;
        this.signManager = plugin.getSignManager(); // Assuming there's a method to get the sign manager
    }

    @Override
    public void run() {
        List<ArenaSetup> arenaSetups = plugin.getArenaSetupManager().getData();
        // Render arena setup signs
        for (ArenaSetup arenaSetup : arenaSetups) {
            if (arenaSetup.getArenaSigns() == null || arenaSetup.getArenaSigns().length == 0) continue;

            // Update all signs related to this ArenaSetup
            List<ScrollingSign> signs = signManager.getArenaSetupSignsByArenaSetupUUID(arenaSetup.getUUID());
            if (signs != null) {
                for (ScrollingSign sign : signs) {
                    sign.updateText();
                }
            }
        }

        List<Arena> arenas = plugin.getArenaManager().getData();

        // Render arena signs
        for (Arena arena : arenas) {
            if (arena.getArenaSigns() == null || arena.getArenaSigns().length == 0) continue;

            // Update all signs related to this Arena
            List<ScrollingSign> signs = signManager.getArenaSignsByUUID(arena.getUUID());
            if (signs != null) {
                for (ScrollingSign sign : signs) {
                    sign.updateText();
                }
            }
        }
    }
}
