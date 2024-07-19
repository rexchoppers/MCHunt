package com.rexchoppers.mchunt.tasks;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.enums.ArenaStatus;
import com.rexchoppers.mchunt.managers.ArenaManager;
import com.rexchoppers.mchunt.managers.SignManager;
import com.rexchoppers.mchunt.models.Arena;
import com.rexchoppers.mchunt.models.Countdown;
import com.rexchoppers.mchunt.signs.ScrollingSign;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class StartCountdownTask extends BukkitRunnable {
    private final MCHunt plugin;
    private ArenaManager arenaManager;
    private SignManager signManager;

    public StartCountdownTask(MCHunt plugin) {
        this.plugin = plugin;
        this.arenaManager = plugin.getArenaManager();
        this.signManager = plugin.getSignManager();
    }

    @Override
    public void run() {
        List<Arena> arenas = arenaManager.getArenas();

        for (Arena arena : arenas) {
            if (!arena.getStatus().equals(ArenaStatus.COUNTDOWN_START)) continue;

            Countdown startCountdown = arena.getStartCountdown();
            int currentCountdown = startCountdown.getCountdown();

            if (currentCountdown == 0) {
                // Start game
            } else {
                startCountdown.decrementCountdown();

                Bukkit.broadcastMessage(Integer.toString(startCountdown.getCountdown()));

                signManager.initArenaSigns(arena);
            }
        }
    }
}
