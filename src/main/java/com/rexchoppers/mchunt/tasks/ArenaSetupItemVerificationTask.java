package com.rexchoppers.mchunt.tasks;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.managers.LocalizationManager;
import com.rexchoppers.mchunt.models.ArenaSetup;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import static com.rexchoppers.mchunt.util.PlayerUtil.sendPlayerAudibleMessage;

public class ArenaSetupItemVerificationTask extends BukkitRunnable {
    private final MCHunt plugin;

    public ArenaSetupItemVerificationTask(MCHunt plugin) {
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
                        return; // Exit this runnable if the player is null or not online
                    }

                    // Check if player has the correct items in their inventory
                    int[] assignedSlots = {0, 1, 2};
                    boolean notify = false;

                    for (int slot : assignedSlots) {
                        if (player.getInventory().getItem(slot) == null) {
                            notify = true;
                            plugin.getItemManager().setArenaSetupItems(player);
                        }
                    }

                    if (notify) {
                        sendPlayerAudibleMessage(
                                player,
                                new LocalizationManager(MCHunt.getCurrentLocale())
                                        .getMessage("player.setup.missing_items_restored")
                        );
                    }
                }
            }.runTask(plugin);
        }
    }
}
