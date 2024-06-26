package com.rexchoppers.mchunt.events;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.exceptions.ArenaSetupNotFoundException;
import com.rexchoppers.mchunt.managers.ArenaSetupManager;
import com.rexchoppers.mchunt.managers.LocalizationManager;
import com.rexchoppers.mchunt.models.ArenaSetup;
import com.rexchoppers.mchunt.util.BoundaryUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

import static com.rexchoppers.mchunt.util.PlayerUtil.sendPlayerAudibleMessage;

public class ArenaSetupEventHandler implements Listener {
    private final MCHunt plugin;

    private HashMap<UUID, Long> lastInteractTimes = new HashMap<>();

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

        long lastTime = lastInteractTimes.getOrDefault(player.getUniqueId(), 0L);
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastTime < 500) {
            event.setCancelled(true);
            return;
        }

        lastInteractTimes.put(player.getUniqueId(), currentTime);

        String action = this.plugin.getItemManager().getItemAction(itemInMainHand);

        switch (action) {
            // Boundary selection
            case "mchunt.boundarySelection":
                BoundaryUtil boundaryUtil = new BoundaryUtil();

                // Clear any pre-existing boundaries for the user
                boundaryUtil.clearTemporaryBoundary(player, arenaSetup);

                // Left click = Boundary point 1
                if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                    arenaSetup.setLocationBoundaryPoint1(event.getClickedBlock().getLocation());
                    this.plugin.getArenaSetupManager().updateArenaSetup(arenaSetup);
                    sendPlayerAudibleMessage(
                            player,
                            new LocalizationManager(MCHunt.getCurrentLocale())
                                    .getMessage(
                                            "arena.setup.boundary_set",
                                            "1",
                                            event.getClickedBlock().getLocation().getWorld().getName(),
                                            Double.toString(event.getClickedBlock().getLocation().getX()),
                                            Double.toString(event.getClickedBlock().getLocation().getY()),
                                            Double.toString(event.getClickedBlock().getLocation().getZ())
                                    )
                    );
                }

                // Right click = Boundary point 2
                if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                    arenaSetup.setLocationBoundaryPoint2(event.getClickedBlock().getLocation());
                    this.plugin.getArenaSetupManager().updateArenaSetup(arenaSetup);
                    sendPlayerAudibleMessage(
                            player,
                            new LocalizationManager(MCHunt.getCurrentLocale())
                                    .getMessage(
                                            "arena.setup.boundary_set",
                                            "2",
                                            event.getClickedBlock().getLocation().getWorld().getName(),
                                            Double.toString(event.getClickedBlock().getLocation().getX()),
                                            Double.toString(event.getClickedBlock().getLocation().getY()),
                                            Double.toString(event.getClickedBlock().getLocation().getZ())
                                    )
                    );
                }

                if (arenaSetup.getLocationBoundaryPoint1() != null &&
                        arenaSetup.getLocationBoundaryPoint2() != null) {
                    boundaryUtil.drawArenaBoundary(
                            player,
                            arenaSetup
                    );
                }

                this.plugin.getArenaSetupManager().updateArenaSetup(arenaSetup);
                event.setCancelled(true);
                break;
            default:
                break;
        }
    }
}
