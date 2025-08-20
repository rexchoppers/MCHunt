package com.rexchoppers.mchunt.events;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.events.internal.PlayerLeftArenaEvent;
import com.rexchoppers.mchunt.models.Arena;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public record ArenaEventHandler(MCHunt plugin) implements Listener {

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
}
