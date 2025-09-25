package com.rexchoppers.mchunt.listeners;

import com.google.common.eventbus.Subscribe;
import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.enums.ArenaStatus;
import com.rexchoppers.mchunt.events.internal.ArenaFinishedEvent;
import com.rexchoppers.mchunt.events.internal.PlayerLeftArenaEvent;
import com.rexchoppers.mchunt.models.Arena;
import org.bukkit.entity.Player;

import static com.rexchoppers.mchunt.util.PlayerUtil.sendPlayerAudibleMessage;
import static com.rexchoppers.mchunt.util.PlayerUtil.sendPlayerMessage;

public record PlayerLeftArenaListener(MCHunt plugin) {

    @Subscribe
    public void handleArenaWaiting(PlayerLeftArenaEvent event) {
        Arena arena = plugin.getArenaManager().getByUUID(event.arenaUuid()).orElse(null);

        if (arena == null) {
            return;
        }

        if (!arena.getStatus().equals(ArenaStatus.WAITING)) {
            return;
        }

        plugin.getSignManager().initArenaSigns(arena);

        arena.getPlayers().forEach(player -> {
            Player serverPlayer = plugin.getServer().getPlayer(player.getUUID());

            if (serverPlayer != null) {
                sendPlayerMessage(
                        serverPlayer,
                        MCHunt.getLocalization()
                                .getMessage(
                                        "arena.player_left",
                                        plugin.getServer().getPlayer(event.playerUuid()).getName()
                                )
                );
            }
        });

        // Send a player themselves a message to let them know they have left the arena
        Player serverPlayer = plugin.getServer().getPlayer(event.playerUuid());
        if (serverPlayer != null) {
            sendPlayerMessage(
                    serverPlayer,
                    MCHunt.getLocalization()
                            .getMessage("player.left_arena")
            );
        }
    }

    @Subscribe
    public void teleportToAfterGameSpawn(PlayerLeftArenaEvent event) {
        Arena arena = plugin.getArenaManager().getByUUID(event.arenaUuid()).orElse(null);

        if (arena == null) {
            return;
        }

        Player serverPlayer = plugin.getServer().getPlayer(event.playerUuid());

        if (serverPlayer != null && serverPlayer.isOnline() && arena.getAfterGameSpawn() != null) {
            serverPlayer.teleport(arena.getAfterGameSpawn());
        }
    }

    @Subscribe
    public void handleArenaInProgress(PlayerLeftArenaEvent event) {
        Arena arena = plugin.getArenaManager().getByUUID(event.arenaUuid()).orElse(null);

        if (arena == null) {
            return;
        }

        if (!arena.getStatus().equals(ArenaStatus.IN_PROGRESS)) {
            return;
        }

        // Announce player left the arena
        arena.getPlayers().forEach(player -> {
            Player serverPlayer = plugin.getServer().getPlayer(player.getUUID());

            if (serverPlayer != null) {
                sendPlayerAudibleMessage(
                        serverPlayer,
                        MCHunt.getLocalization()
                                .getMessage("arena.player_left",
                                        serverPlayer.getName())
                );
            }
        });

        if (arena.getHiders().isEmpty() || arena.getSeekers().isEmpty()) {
            this.plugin.getEventBusManager().publishEvent(new ArenaFinishedEvent(arena));
        }
    }

    /**
     * Cancel the start countdown if the player leaves the arena and the min player count is not met
     * @param event
     */
    @Subscribe
    public void cancelStartCountdown(PlayerLeftArenaEvent event) {
        Arena arena = plugin.getArenaManager().getByUUID(event.arenaUuid()).orElse(null);

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
                        MCHunt.getLocalization()
                                .getMessage("arena.countdown_cancelled")
                );
            }
        });
    }
}