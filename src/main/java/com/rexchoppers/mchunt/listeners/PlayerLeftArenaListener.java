package com.rexchoppers.mchunt.listeners;

import com.google.common.eventbus.Subscribe;
import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.enums.ArenaStatus;
import com.rexchoppers.mchunt.events.internal.ArenaSetupPlayerJoinedEvent;
import com.rexchoppers.mchunt.events.internal.ArenaSetupUpdatedEvent;
import com.rexchoppers.mchunt.events.internal.PlayerLeftArenaEvent;
import com.rexchoppers.mchunt.managers.LocalizationManager;
import com.rexchoppers.mchunt.models.Arena;
import com.rexchoppers.mchunt.models.ArenaSetup;
import org.bukkit.entity.Player;

import static com.rexchoppers.mchunt.util.PlayerUtil.sendPlayerAudibleMessage;
import static com.rexchoppers.mchunt.util.PlayerUtil.sendPlayerMessage;

public class PlayerLeftArenaListener {
    private final MCHunt plugin;

    public PlayerLeftArenaListener(MCHunt plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void broadcastToPlayersInArena(PlayerLeftArenaEvent event) {
        Arena arena = plugin.getArenaManager().getArenaByUUID(event.arenaUuid()).orElse(null);

        if (arena == null) {
            return;
        }

        arena.getPlayers().forEach(player -> {
            Player serverPlayer = plugin.getServer().getPlayer(player.getUUID());

            if (serverPlayer != null) {
                sendPlayerMessage(
                        serverPlayer,
                        new LocalizationManager(MCHunt.getCurrentLocale())
                                .getMessage(
                                        "arena.player_left",
                                        plugin.getServer().getPlayer(event.playerUuid()).getName()
                                )
                );
            }
        });
    }

    @Subscribe
    public void updateArenaSigns(PlayerLeftArenaEvent event) {
        Arena arena = plugin.getArenaManager().getArenaByUUID(event.arenaUuid()).orElse(null);

        if (arena == null) {
            return;
        }

        plugin.getSignManager().initArenaSigns(arena);
    }

    /**
     * Cancel the start countdown if the player leaves the arena and the min player count is not met
     */
    @Subscribe
    public void cancelStartCountdown(PlayerLeftArenaEvent event) {
        Arena arena = plugin.getArenaManager().getArenaByUUID(event.arenaUuid()).orElse(null);

        if (arena == null) {
            return;
        }

        if (!arena.getStatus().equals(ArenaStatus.COUNTDOWN_START)) {
            return;
        }

        if (!(arena.getPlayers().size() < arena.getMinimumPlayers())) return;

        arena.setStatus(ArenaStatus.WAITING);
        arena.setStartCountdown(null);

        plugin.getSignManager().initArenaSigns(arena);

        // Send players message that the countdown has been cancelled
        arena.getPlayers().forEach(player -> {
            Player serverPlayer = plugin.getServer().getPlayer(player.getUUID());

            if (serverPlayer != null) {
                sendPlayerAudibleMessage(
                        serverPlayer,
                        new LocalizationManager(MCHunt.getCurrentLocale())
                                .getMessage("arena.countdown_cancelled")
                );
            }
        });
    }
}