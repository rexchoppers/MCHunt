package com.rexchoppers.mchunt.tasks;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.enums.ArenaPlayerRole;
import com.rexchoppers.mchunt.enums.ArenaStatus;
import com.rexchoppers.mchunt.events.internal.ArenaFinishedEvent;
import com.rexchoppers.mchunt.events.internal.ArenaSeekersReleasedEvent;
import com.rexchoppers.mchunt.events.internal.HiderIsStillEvent;
import com.rexchoppers.mchunt.managers.ArenaRepository;
import com.rexchoppers.mchunt.managers.LocalizationManager;
import com.rexchoppers.mchunt.managers.SignManager;
import com.rexchoppers.mchunt.models.Arena;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

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
                                sendPlayerAudibleMessage(
                                        serverPlayer,
                                        new LocalizationManager(MCHunt.getCurrentLocale())
                                                .getMessage("arena.time_left", timeLeft)
                                );
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

                    // Hider block task
                    arena.getPlayers().forEach(player -> {
                        Player serverPlayer = Bukkit.getPlayer(player.getUUID());

                        if (serverPlayer != null && player.getRole().equals(ArenaPlayerRole.HIDER)) {
                            player.updateMovement(serverPlayer.getLocation());

                            // If the hider has been still for 5 seconds, set their block
                            if (player.hasBeenStillFor(5000) && !player.isDisguiseLocked()) {
                                this.plugin.getEventBusManager().publishEvent(new HiderIsStillEvent(arena, player));
                            }
                        }
                    });

                    // Check if the game time has run out
                    if (arena.getCurrentGameTime() <= 0) {
                        arena.setStatus(ArenaStatus.FINISHED);

                        this.plugin.getEventBusManager().publishEvent(new ArenaFinishedEvent(arena));
                    }
                    break;
            }



        }
    }
}
