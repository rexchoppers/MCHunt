package com.rexchoppers.mchunt.tasks;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.models.Arena;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class ArenaHiderBlockTask extends BukkitRunnable {
    private final MCHunt plugin;

    public ArenaHiderBlockTask(MCHunt plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        List<Arena> arenas = plugin.getArenaManager().getData();

        
    }
}
