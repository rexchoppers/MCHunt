package com.rexchoppers.mchunt.tasks;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.enums.ArenaPlayerRole;
import com.rexchoppers.mchunt.enums.ArenaStatus;
import com.rexchoppers.mchunt.managers.ArenaManager;
import com.rexchoppers.mchunt.managers.LocalizationManager;
import com.rexchoppers.mchunt.managers.SignManager;
import com.rexchoppers.mchunt.models.Arena;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import static com.rexchoppers.mchunt.util.PlayerUtil.sendPlayerAudibleMessage;

public class ArenaTick extends BukkitRunnable {
    private final ArenaManager arenaManager;
    private final SignManager signManager;

    public ArenaTick(MCHunt plugin) {
        this.arenaManager = plugin.getArenaManager();
        this.signManager = plugin.getSignManager();
    }

    @Override
    public void run() {
        List<Arena> arenas = arenaManager.getArenas();

        for (Arena arena : arenas) {
            if (!arena.getStatus().equals(ArenaStatus.IN_PROGRESS)) continue;

            // Decrease the game time
            arena.setCurrentGameTime(arena.getCurrentGameTime() - 1);

            signManager.initArenaSigns(arena);

            // Announce the time left at key intervals
            if (arena.getCurrentGameTime() % 60 == 0 || arena.getCurrentGameTime() == 30 || arena.getCurrentGameTime() == 10 || arena.getCurrentGameTime() <= 5) {
                arena.getPlayers().forEach(player -> {
                    Player serverPlayer = Bukkit.getPlayer(player.getUUID());

                    if (serverPlayer != null) {
                        sendPlayerAudibleMessage(
                                serverPlayer,
                                new LocalizationManager(MCHunt.getCurrentLocale())
                                        .getMessage("arena.time_left", arena.getCurrentGameTime())
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

                arena.getPlayers().forEach(player -> {
                    // Broadcast that the seekers have been released
                    Player serverPlayer = Bukkit.getPlayer(player.getUUID());

                    sendPlayerAudibleMessage(
                            serverPlayer,
                            new LocalizationManager(MCHunt.getCurrentLocale())
                                    .getMessage("arena.seekers_released")
                    );
                });
            }
        }
    }
}
