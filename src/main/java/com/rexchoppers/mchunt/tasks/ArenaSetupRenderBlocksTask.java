package com.rexchoppers.mchunt.tasks;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.managers.LocalizationManager;
import com.rexchoppers.mchunt.models.ArenaSetup;
import com.rexchoppers.mchunt.util.BoundaryUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import static com.rexchoppers.mchunt.util.PlayerUtil.sendPlayerAudibleMessage;

public class ArenaSetupRenderBlocksTask extends BukkitRunnable {
    private final MCHunt plugin;

    private final BoundaryUtil boundaryUtil = new BoundaryUtil();

    public ArenaSetupRenderBlocksTask(MCHunt plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        List<ArenaSetup> arenaSetups = plugin.getArenaSetupManager().getData();

        for (ArenaSetup arenaSetup : arenaSetups) {
            new BukkitRunnable() {
                @Override
                public void run() {
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

                    if (arenaSetup.getSeekerSpawns() != null) {
                        for (int i = 0; i < arenaSetup.getSeekerSpawns().length; i++) {
                            player.sendBlockChange(arenaSetup.getSeekerSpawns()[i], plugin.getItemManager().itemArenaSetupSeekerSpawn().getMaterial().createBlockData());
                        }
                    }

                    if (arenaSetup.getLobbySpawn() != null) {
                        player.sendBlockChange(arenaSetup.getLobbySpawn(), plugin.getItemManager().itemArenaSetupLobbySpawn().getMaterial().createBlockData());
                    }

                    if (arenaSetup.getAfterGameSpawn() != null) {
                        player.sendBlockChange(arenaSetup.getAfterGameSpawn(), plugin.getItemManager().itemArenaSetupAfterGameSpawn().getMaterial().createBlockData());
                    }

                    // Render boundary blocks
                    if (arenaSetup.getLocationBoundaryPoint1() != null && arenaSetup.getLocationBoundaryPoint2() != null) {
                        boundaryUtil.drawArenaBoundary(player, arenaSetup);
                    }
                }
            }.runTask(plugin);
        }
    }
}
