package com.rexchoppers.mchunt.events;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.exceptions.ArenaSetupNotFoundException;
import com.rexchoppers.mchunt.managers.ArenaSetupManager;
import com.rexchoppers.mchunt.models.ArenaSetup;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ArenaSetupEventHandler implements Listener {
    private final MCHunt plugin;

    public ArenaSetupEventHandler(MCHunt plugin) {
        this.plugin = plugin;
    }

    public boolean isEmptyItem(ItemStack item) {
        return item == null || item.getType().isAir();
    }

    public boolean isBlockClickedEmpty(PlayerInteractEvent event) {
        return event.getClickedBlock() == null || event.getClickedBlock().getType().isAir();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) throws ArenaSetupNotFoundException {
        Player player = event.getPlayer();
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();

        if (isEmptyItem(itemInMainHand) || isBlockClickedEmpty(event)) {
            return;
        }

        ArenaSetup arenaSetup = this.plugin.getArenaSetupManager()
                .getArenaSetupByPlayerUuid(
                        plugin.getArenaSetupManager().getArenaSetups(),
                        player.getUniqueId()).orElse(null);

        if (arenaSetup == null) {
            throw new ArenaSetupNotFoundException();
        }

        String action = this.plugin.getItemManager().getItemAction(itemInMainHand);

        switch (action) {
            // Boundary selection
            case "mchunt.boundarySelection":

                // Left click = Boundary point 1
                if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                    arenaSetup.setLocationBoundaryPoint1(event.getClickedBlock().getLocation());
                    this.plugin.getArenaSetupManager().updateArenaSetup(arenaSetup);
                }

                // Right click = Boundary point 2
                if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {

                }
                event.setCancelled(true);
                break;
            default:
                break;
        }
    }
}
