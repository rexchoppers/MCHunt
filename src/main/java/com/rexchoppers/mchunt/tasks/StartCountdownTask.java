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
    private final MCHunt plugin;

    private final ArenaManager arenaManager;
    private final SignManager signManager;

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

            Bukkit.broadcastMessage("S " + Integer.toString(arena.getPlayers().size()));


            if (currentCountdown == 0) {
                arena.setStatus(ArenaStatus.IN_PROGRESS);

                signManager.initArenaSigns(arena);


            } else {
                startCountdown.decrementCountdown();

                signManager.initArenaSigns(arena);

                int[] secondsToDisplay = {10, 5, 4, 3, 2, 1};

                // Only send messages to users if the countdown is in the secondsToDisplay array
                for (int second : secondsToDisplay) {
                    Bukkit.broadcastMessage("C " + Integer.toString(currentCountdown));
                    Bukkit.broadcastMessage("SEC " + Integer.toString(second));


                    if (currentCountdown != second)  continue;

                    Bukkit.broadcastMessage(
                            new LocalizationManager(MCHunt.getCurrentLocale())
                                    .getMessage(
                                            "arena.countdown",
                                            Integer.toString(currentCountdown)
                                    ));

                    Bukkit.broadcastMessage("SIZE " + Integer.toString(arena.getPlayers().size()));

                    arena.getPlayers().forEach(player -> {
                        Bukkit.broadcastMessage("P " + player.getUUID().toString() + " " + Integer.toString(currentCountdown));

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
