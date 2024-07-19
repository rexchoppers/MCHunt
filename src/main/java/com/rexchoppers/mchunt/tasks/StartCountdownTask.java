package com.rexchoppers.mchunt.tasks;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.enums.ArenaStatus;
import com.rexchoppers.mchunt.managers.ArenaManager;
import com.rexchoppers.mchunt.managers.LocalizationManager;
import com.rexchoppers.mchunt.managers.SignManager;
import com.rexchoppers.mchunt.models.Arena;
import com.rexchoppers.mchunt.models.Countdown;
import com.rexchoppers.mchunt.signs.ScrollingSign;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import static com.rexchoppers.mchunt.util.PlayerUtil.sendPlayerAudibleMessage;

public class StartCountdownTask extends BukkitRunnable {
    private final ArenaManager arenaManager;
    private final SignManager signManager;

    public StartCountdownTask(MCHunt plugin) {
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

                signManager.initArenaSigns(arena);

                int[] secondsToDisplay = {10, 5, 4, 3, 2, 1};

                // Only send messages to users if the countdown is in the secondsToDisplay array
                for (int second : secondsToDisplay) {
                    if (currentCountdown != second || currentCountdown != arena.getCountdownBeforeStart())  continue;

                    arena.getPlayers().forEach(player -> {
                        sendPlayerAudibleMessage(
                                Bukkit.getPlayer(player.getUUID()),
                                new LocalizationManager(MCHunt.getCurrentLocale())
                                        .getMessage(
                                                "arena.countdown",
                                                Integer.toString(currentCountdown)
                                        )
                        );
                    });
                }
            }
        }
    }
}
