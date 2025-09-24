package com.rexchoppers.mchunt.listeners;

import com.google.common.eventbus.Subscribe;
import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.enums.ArenaStatus;
import com.rexchoppers.mchunt.events.internal.PlayerJoinedArenaEvent;
import com.rexchoppers.mchunt.managers.LocalizationManager;
import com.rexchoppers.mchunt.models.Arena;
import com.rexchoppers.mchunt.models.Countdown;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import static com.rexchoppers.mchunt.util.PlayerUtil.*;

public record PlayerJoinedArenaListener(MCHunt plugin) {

    @Subscribe
    public void broadcastToPlayersInArena(PlayerJoinedArenaEvent event) {
        Arena arena = plugin.getArenaManager().getByUUID(event.arenaUuid()).orElse(null);

        if (arena == null) {
            return;
        }

        arena.getPlayers().forEach(player -> {
            Player serverPlayer = plugin.getServer().getPlayer(player.getUUID());

            if (serverPlayer != null) {
                sendPlayerMessage(
                        serverPlayer,
                        MCHunt.getLocalization()
                                .getMessage(
                                        "arena.player_joined",
                                        plugin.getServer().getPlayer(event.playerUuid()).getName()
                                )
                );
            }
        });
    }

    @Subscribe
    public void updateArenaSigns(PlayerJoinedArenaEvent event) {
        Arena arena = plugin.getArenaManager().getByUUID(event.arenaUuid()).orElse(null);

        if (arena == null) {
            return;
        }

        plugin.getSignManager().initArenaSigns(arena);
    }

    @Subscribe
    public void setupPlayer(PlayerJoinedArenaEvent event) {
        Player player = plugin.getServer().getPlayer(event.playerUuid());

        if (player == null) {
            return;
        }

        player.getInventory().clear();
        player.setGameMode(GameMode.ADVENTURE);
        player.setFoodLevel(20);
        player.setHealth(20.0);
    }

    @Subscribe
    public void triggerArenaStart(PlayerJoinedArenaEvent event) {
        Arena arena = plugin.getArenaManager().getByUUID(event.arenaUuid()).orElse(null);

        if (arena == null) {
            return;
        }

        // DEBUG LINE
        // if (arena.getPlayers().size() >= 1 && arena.getStatus().equals(ArenaStatus.WAITING)) {

        if (arena.getPlayers().size() >= arena.getMinimumPlayers() && arena.getStatus().equals(ArenaStatus.WAITING)) {
            arena.setStatus(ArenaStatus.COUNTDOWN_START);
            arena.setStartCountdown(
                    new Countdown(arena.getCountdownBeforeStart())
            );
            plugin.getArenaManager().update(arena);
        }
    }
}
