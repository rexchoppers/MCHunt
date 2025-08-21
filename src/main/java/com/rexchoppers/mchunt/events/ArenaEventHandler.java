package com.rexchoppers.mchunt.events;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.enums.ArenaPlayerRole;
import com.rexchoppers.mchunt.enums.ArenaStatus;
import com.rexchoppers.mchunt.events.internal.HiderHasMovedEvent;
import com.rexchoppers.mchunt.events.internal.PlayerLeftArenaEvent;
import com.rexchoppers.mchunt.models.Arena;
import com.rexchoppers.mchunt.models.ArenaPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public record ArenaEventHandler(MCHunt plugin) implements Listener {

    /**
     * Handles player movement events.
     * This method is only used for hider movement checks to reduce
     * the number of checks performed by the server.
     *
     * @param event
     */
    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        Player serverPlayer = event.getPlayer();
        UUID playerUUID = serverPlayer.getUniqueId();

        // Check if player is in an arena
        if (plugin.getArenaManager().isPlayerInArena(playerUUID)) {
            Arena arena = plugin.getArenaManager().getArenaPlayerIsIn(playerUUID).orElse(null);

            if (arena == null) {
                return;
            }

            if (!arena.getStatus().equals(ArenaStatus.IN_PROGRESS)) {
                return;
            }

            ArenaPlayer player = arena.getPlayer(playerUUID);

            if (player.getRole() == null || !player.getRole().equals(ArenaPlayerRole.HIDER)) {
                return;
            }

            if (!player.isDisguiseLocked()) {
                return;
            }

            // Ignore non-moving events to reduce server load
            if (event.getFrom().getX() == event.getTo().getX()
                    && event.getFrom().getY() == event.getTo().getY()
                    && event.getFrom().getZ() == event.getTo().getZ()) {
                return;
            }

            // Add a threshold so that small movements do not trigger the event
            double threshold = 0.5;
            if (Math.abs(event.getFrom().getX() - event.getTo().getX()) < threshold
                    && Math.abs(event.getFrom().getY() - event.getTo().getY()) < threshold
                    && Math.abs(event.getFrom().getZ() - event.getTo().getZ()) < threshold) {
                return;
            }

            this.plugin.getEventBusManager().publishEvent(new HiderHasMovedEvent(
                    arena,
                    player
            ));
        }
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        // Check if player is in an arena
        if (plugin.getArenaManager().isPlayerInArena(playerUUID)) {
            Arena arena = plugin.getArenaManager().getArenaPlayerIsIn(playerUUID).orElse(null);
            arena.removePlayer(playerUUID);

            this.plugin.getEventBusManager().publishEvent(new PlayerLeftArenaEvent(arena.getUUID(), playerUUID));
        }
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (plugin.getArenaManager().isPlayerInArena(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if (plugin.getArenaManager().isPlayerInArena(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodLevelChangeEvent(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        // Prevent food level changes in arenas
        if (plugin.getArenaManager().isPlayerInArena(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }
}
