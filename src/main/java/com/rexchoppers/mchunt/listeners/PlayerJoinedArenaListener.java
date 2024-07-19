package com.rexchoppers.mchunt.listeners;

import com.google.common.eventbus.Subscribe;
import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.events.internal.PlayerJoinedArenaEvent;
import com.rexchoppers.mchunt.managers.LocalizationManager;
import com.rexchoppers.mchunt.models.Arena;
import org.bukkit.entity.Player;

import static com.rexchoppers.mchunt.util.PlayerUtil.sendPlayerError;
import static com.rexchoppers.mchunt.util.PlayerUtil.sendPlayerMessage;

public class PlayerJoinedArenaListener {
    private final MCHunt plugin;

    public PlayerJoinedArenaListener(MCHunt plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void broadcastToPlayersInArena(PlayerJoinedArenaEvent event) {
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
                                        "arena.player_joined",
                                        plugin.getServer().getPlayer(event.playerUuid()).getName()
                                )
                );
            }
        });
    }

    @Subscribe
    public void updateArenaSigns(PlayerJoinedArenaEvent event) {
        Arena arena = plugin.getArenaManager().getArenaByUUID(event.arenaUuid()).orElse(null);

        if (arena == null) {
            return;
        }

        plugin.getSignManager().initArenaSigns(arena);
    }
}
