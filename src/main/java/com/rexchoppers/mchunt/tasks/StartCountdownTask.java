package com.rexchoppers.mchunt.tasks;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.enums.ArenaPlayerRole;
import com.rexchoppers.mchunt.enums.ArenaStatus;
import com.rexchoppers.mchunt.managers.ArenaRepository;
import com.rexchoppers.mchunt.managers.LocalizationManager;
import com.rexchoppers.mchunt.managers.SignManager;
import com.rexchoppers.mchunt.models.Arena;
import com.rexchoppers.mchunt.models.ArenaPlayer;
import com.rexchoppers.mchunt.models.Countdown;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import static com.rexchoppers.mchunt.util.PlayerUtil.sendPlayerAudibleMessage;

public class StartCountdownTask extends BukkitRunnable {
    private final MCHunt plugin;

    private final ArenaRepository arenaManager;
    private final SignManager signManager;

    public StartCountdownTask(MCHunt plugin) {
        this.plugin = plugin;
        this.arenaManager = plugin.getArenaManager();
        this.signManager = plugin.getSignManager();
    }

    @Override
    public void run() {
        List<Arena> arenas = arenaManager.getData();

        for (Arena arena : arenas) {
            if (!arena.getStatus().equals(ArenaStatus.COUNTDOWN_START)) continue;

            Countdown startCountdown = arena.getStartCountdown();
            int currentCountdown = startCountdown.getCountdown();

            if (currentCountdown == 0) {
                arena.setStatus(ArenaStatus.IN_PROGRESS);
                arena.setCurrentGameTime(arena.getGameLength());

                arenaManager.update(arena);
                signManager.initArenaSigns(arena);

                // Get count of players in arena
                int playerCount = arena.getPlayers().size();

                // Get the seekers count on game start
                int seekerCount = arena.getSeekerCount();

                // Randomly set the seekers in the arena
                for (int i = 0; i < seekerCount; i++) {
                    int randomIndex = (int) (Math.random() * playerCount);
                    ArenaPlayer seeker = arena.getPlayers().get(randomIndex);
                    Player player = Bukkit.getPlayer(seeker.getUUID());
                    seeker.setRole(ArenaPlayerRole.SEEKER);

                    // Send a message to the seeker
                    sendPlayerAudibleMessage(
                            player,
                            new LocalizationManager(MCHunt.getCurrentLocale())
                                    .getMessage("arena.seeker")
                    );
                }

                // Set the rest of the players as hiders
                arena.getPlayers().forEach(player -> {
                    if (player.getRole().equals(ArenaPlayerRole.HIDER)) return;

                    // If they've already been
                    if (player.getRole().equals(ArenaPlayerRole.SEEKER)) return;

                    player.setRole(ArenaPlayerRole.HIDER);

                    Bukkit.getServer().getPlayer(player.getUUID()).teleport(
                            arena.getHiderSpawns()[(int) (Math.random() * arena.getHiderSpawns().length)]);
                });

            } else {
                startCountdown.decrementCountdown();

                signManager.initArenaSigns(arena);

                int[] secondsToDisplay = {10, 5, 4, 3, 2, 1};

                // Only send messages to users if the countdown is in the secondsToDisplay array
                for (int second : secondsToDisplay) {
                    if (currentCountdown != second) continue;

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
