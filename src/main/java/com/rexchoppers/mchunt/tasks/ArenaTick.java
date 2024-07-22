package com.rexchoppers.mchunt.tasks;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.enums.ArenaStatus;
import com.rexchoppers.mchunt.managers.ArenaManager;
import com.rexchoppers.mchunt.managers.SignManager;
import com.rexchoppers.mchunt.models.Arena;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

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
            arena.setCurrentGameTime(arena.getGameLength() - 1);

            signManager.initArenaSigns(arena);
        }
    }
}
