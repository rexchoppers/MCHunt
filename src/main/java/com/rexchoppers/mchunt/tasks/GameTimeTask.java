package com.rexchoppers.mchunt.tasks;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.enums.ArenaStatus;
import com.rexchoppers.mchunt.managers.ArenaManager;
import com.rexchoppers.mchunt.managers.LocalizationManager;
import com.rexchoppers.mchunt.managers.SignManager;
import com.rexchoppers.mchunt.models.Arena;
import com.rexchoppers.mchunt.models.Countdown;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import static com.rexchoppers.mchunt.util.PlayerUtil.sendPlayerAudibleMessage;

public class GameTimeTask extends BukkitRunnable {
    private final ArenaManager arenaManager;
    private final SignManager signManager;

    public GameTimeTask(MCHunt plugin) {
        this.arenaManager = plugin.getArenaManager();
        this.signManager = plugin.getSignManager();
    }

    @Override
    public void run() {
        List<Arena> arenas = arenaManager.getArenas();

        for (Arena arena : arenas) {
            if (!arena.getStatus().equals(ArenaStatus.IN_PROGRESS)) continue;

            // Decrease the game time
            arena.setGameTime(arena.getGameTime() - 1);

            signManager.initArenaSigns(arena);
        }
    }
}
