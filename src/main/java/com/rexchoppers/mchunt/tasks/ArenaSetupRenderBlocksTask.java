package com.rexchoppers.mchunt.tasks;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.managers.LocalizationManager;
import com.rexchoppers.mchunt.models.ArenaSetup;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import static com.rexchoppers.mchunt.util.PlayerUtil.sendPlayerAudibleMessage;

public class ArenaSetupRenderBlocksTask extends BukkitRunnable {
    private final MCHunt plugin;

    public ArenaSetupRenderBlocksTask(MCHunt plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        // This code runs asynchronously
        List<ArenaSetup> arenaSetups = plugin.getArenaSetupManager().getArenaSetups();

        for (ArenaSetup arenaSetup : arenaSetups) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    // This part runs on the main thread
                    Player player = plugin.getServer().getPlayer(arenaSetup.getPlayerUuid());

                    if (player == null || !player.isOnline()) {
                        return;
                    }

                    // Render spawn blocks
                    if (arenaSetup.getHiderSpawns() != null) {
                        for (int i = 0; i < arenaSetup.getHiderSpawns().length; i++) {
                            player.sendBlockChange(arenaSetup.getHiderSpawns()[i], plugin.getItemManager().itemArenaSetupHiderSpawn().getMaterial().createBlockData());
                        }
                    }
                }
            }.runTask(plugin);
        }
    }
}
