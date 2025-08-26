package com.rexchoppers.mchunt.tasks;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.items.ItemBuilder;
import com.rexchoppers.mchunt.managers.LocalizationManager;
import com.rexchoppers.mchunt.models.ArenaSetup;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Map;

import static com.rexchoppers.mchunt.util.PlayerUtil.sendPlayerAudibleMessage;

public class ArenaSetupItemVerificationTask extends BukkitRunnable {
    private final MCHunt plugin;

    public ArenaSetupItemVerificationTask(MCHunt plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        // This code runs asynchronously
        List<ArenaSetup> arenaSetups = plugin.getArenaSetupManager().getData();

        for (ArenaSetup arenaSetup : arenaSetups) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    // This part runs on the main thread
                    Player player = plugin.getServer().getPlayer(arenaSetup.getPlayerUuid());

                    if (player == null || !player.isOnline()) {
                        return;
                    }

                    boolean notify = false;

                    Map<Integer, ItemBuilder> arenaItems = plugin.getItemManager().getDefaultHotbarArenaSetupItems();

                    for (Map.Entry<Integer, ItemBuilder> entry : arenaItems.entrySet()) {
                        int slot = entry.getKey();
                        ItemBuilder itemBuilder = entry.getValue();
                        if (player.getInventory().getItem(slot) == null || !player.getInventory().getItem(slot).isSimilar(itemBuilder.build())) {

                            player.getInventory().setItem(slot, itemBuilder.build());
                            notify = true;
                        }
                    }

                    if (notify) {
                        sendPlayerAudibleMessage(
                                player,
                                MCHunt.getLocalization()
                                        .getMessage("player.setup.missing_items_restored")
                        );
                    }
                }
            }.runTask(plugin);
        }
    }
}
