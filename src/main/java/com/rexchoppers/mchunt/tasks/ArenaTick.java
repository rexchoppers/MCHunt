package com.rexchoppers.mchunt.tasks;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.enums.ArenaPlayerRole;
import com.rexchoppers.mchunt.enums.ArenaStatus;
import com.rexchoppers.mchunt.events.internal.ArenaFinishedEvent;
import com.rexchoppers.mchunt.events.internal.ArenaResetCountdownEndedEvent;
import com.rexchoppers.mchunt.events.internal.ArenaSeekersReleasedEvent;
import com.rexchoppers.mchunt.events.internal.HiderIsStillEvent;
import com.rexchoppers.mchunt.managers.ArenaRepository;
import com.rexchoppers.mchunt.managers.LocalizationManager;
import com.rexchoppers.mchunt.managers.SignManager;
import com.rexchoppers.mchunt.models.Arena;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

import static com.rexchoppers.mchunt.util.PlayerUtil.sendPlayerAudibleMessage;

public class ArenaTick extends BukkitRunnable {
    private final MCHunt plugin;
    private final ArenaRepository arenaManager;
    private final SignManager signManager;

    public ArenaTick(MCHunt plugin) {
        this.plugin = plugin;
        this.arenaManager = plugin.getArenaManager();
        this.signManager = plugin.getSignManager();
    }

    @Override
    public void run() {
        List<Arena> arenas = arenaManager.getData();

        for (Arena arena : arenas) {
            switch (arena.getStatus()) {
                case ArenaStatus.IN_PROGRESS:
                    // Decrease the game time
                    arena.setCurrentGameTime(arena.getCurrentGameTime() - 1);

                    signManager.initArenaSigns(arena);

                    // Announce the time left at specific key intervals: 2m, 1m, 30s, 10s and downwards
                    int timeLeft = arena.getCurrentGameTime();
                    if (arena.getStatus().equals(ArenaStatus.IN_PROGRESS) &&
                            (timeLeft == 120 || timeLeft == 60 || timeLeft == 30 || (timeLeft <= 10 && timeLeft > 0))) {
                        arena.getPlayers().forEach(player -> {
                            Player serverPlayer = Bukkit.getPlayer(player.getUUID());

                            if (serverPlayer != null) {
                                // Choose a localized message that best represents the time left
                                String key;
                                Object[] params;
                                int minutes = timeLeft / 60;
                                int seconds = timeLeft % 60;

                                if (timeLeft >= 60 && seconds == 0) {
                                    // Whole minutes remaining
                                    if (minutes == 1) {
                                        key = "arena.time_left_minute";
                                    } else {
                                        key = "arena.time_left_minutes";
                                    }
                                    params = new Object[] { minutes };
                                } else if (timeLeft >= 60) {
                                    // Minutes and seconds remaining
                                    if (minutes == 1 && seconds == 1) {
                                        key = "arena.time_left_min_sec_m_s";
                                    } else if (minutes == 1) {
                                        key = "arena.time_left_min_sec_m_ss";
                                    } else if (seconds == 1) {
                                        key = "arena.time_left_min_sec_mm_s";
                                    } else {
                                        key = "arena.time_left_min_sec";
                                    }
                                    params = new Object[] { minutes, seconds };
                                } else {
                                    // Seconds only
                                    if (timeLeft == 1) {
                                        key = "arena.time_left_second";
                                    } else {
                                        key = "arena.time_left_seconds";
                                    }
                                    params = new Object[] { timeLeft };
                                }

                                LocalizationManager lm = MCHunt.getLocalization();
                                String message;
                                try {
                                    message = lm.getMessage(key, params);
                                } catch (Exception ex) {
                                    // Fallback to the old key if new ones are not present in the bundle
                                    message = lm.getMessage("arena.time_left", timeLeft);
                                }

                                sendPlayerAudibleMessage(serverPlayer, message);
                            }
                        });
                    }

                    // Check if seekers can be released
                    if (arena.getCurrentGameTime() == arena.getGameLength() - arena.getSeekerReleaseDelay()) {
                        // Teleport seekers to their spawn points
                        arena.getPlayers().stream()
                                .filter(player -> player.getRole().equals(ArenaPlayerRole.SEEKER))
                                .forEach(player -> {
                                    Player serverPlayer = Bukkit.getPlayer(player.getUUID());

                                    if (serverPlayer != null) {
                                        // Pick a random seeker spawn point
                                        int randomIndex = (int) (Math.random() * arena.getSeekerSpawns().length);
                                        serverPlayer.teleport(arena.getSeekerSpawns()[randomIndex]);
                                    }
                                });

                        this.plugin.getEventBusManager().publishEvent(new ArenaSeekersReleasedEvent(arena));
                    }

                    // Convert arena still time from seconds to milliseconds
                    int hiderStillTime = arena.getHiderStillTime() * 1000;

                    // Hider block task
                    arena.getPlayers().forEach(player -> {
                        Player serverPlayer = Bukkit.getPlayer(player.getUUID());

                        if (serverPlayer != null && player.getRole().equals(ArenaPlayerRole.HIDER)) {
                            player.updateMovement(serverPlayer.getLocation());

                            // Show a pre-lock countdown on the XP bar for hiders who are standing still
                            if (!player.isDisguiseLocked()) {
                                long elapsed = System.currentTimeMillis() - player.getLastMovement();

                                // If player just moved recently, clear any countdown UI
                                if (elapsed < 500 && player.getLastStillCountdownSeconds() != -1) {
                                    serverPlayer.setExp(0f);
                                    serverPlayer.setLevel(0);
                                    player.setLastStillCountdownSeconds(-1);
                                }

                                // During the 5-second stillness window, update XP bar and remaining seconds as level
                                if (elapsed >= 0 && elapsed < hiderStillTime) {
                                    int secondsLeft = (int) Math.ceil((hiderStillTime - elapsed) / 1000.0);
                                    float progress = Math.max(0f, Math.min(0.999f, (float) elapsed / (float) hiderStillTime));

                                    if (player.getLastStillCountdownSeconds() != secondsLeft) {
                                        // If this is the start of countdown, inform the player once
                                        if (player.getLastStillCountdownSeconds() == -1) {
                                            sendPlayerAudibleMessage(serverPlayer, MCHunt.getLocalization()
                                                    .getMessage("player.hider.still_countdown_start", "5"));
                                        }

                                        player.setLastStillCountdownSeconds(secondsLeft);
                                        serverPlayer.setLevel(secondsLeft);
                                    }

                                    serverPlayer.setExp(progress);
                                }
                            }

                            // If the hider has been still for 5 seconds, set their block
                            if (player.hasBeenStillFor(hiderStillTime) && !player.isDisguiseLocked()) {
                                // Clear countdown UI right before locking
                                serverPlayer.setExp(0f);
                                serverPlayer.setLevel(0);
                                player.setLastStillCountdownSeconds(-1);

                                this.plugin.getEventBusManager().publishEvent(new HiderIsStillEvent(arena, player));
                            }

                            // If already disguised/locked, ensure countdown UI is cleared
                            if (player.isDisguiseLocked()) {
                                if (serverPlayer.getLevel() != 0 || serverPlayer.getExp() != 0f) {
                                    serverPlayer.setExp(0f);
                                    serverPlayer.setLevel(0);
                                }
                                if (player.getLastStillCountdownSeconds() != -1) {
                                    player.setLastStillCountdownSeconds(-1);
                                }
                            }
                        }
                    });

                    // Check if the game time has run out
                    if (arena.getCurrentGameTime() <= 0) {
                        arena.setStatus(ArenaStatus.FINISHED);

                        this.plugin.getEventBusManager().publishEvent(new ArenaFinishedEvent(arena));
                    }
                    break;

                case ArenaStatus.COUNTDOWN_RESET:
                    // Broadcast the time left to teleporting back to lobby. Only do this from 5 seconds and below
                    if (arena.getResetCountdown() != null &&
                            arena.getResetCountdown().getCountdown() > 0 &&
                            arena.getResetCountdown().getCountdown() <= 5) {
                        int countdown = arena.getResetCountdown().getCountdown();

                        arena.getPlayers().forEach(player -> {
                            Player serverPlayer = Bukkit.getPlayer(player.getUUID());

                            if (serverPlayer != null) {
                                String message = MCHunt.getLocalization()
                                        .getMessage("arena.teleporting_to_lobby_countdown", countdown);

                                sendPlayerAudibleMessage(serverPlayer, message);
                            }
                        });
                    }

                    arena.getResetCountdown().decrementCountdown();

                    if (arena.getResetCountdown().getCountdown() == 0) {
                        this.plugin.getEventBusManager().publishEvent(new ArenaResetCountdownEndedEvent(
                                arena,
                                // Copy of players. Don't want race conditions with clearing players
                                new ArrayList<>(arena.getPlayers())
                        ));
                    }

                    break;
            }
        }
    }
}
