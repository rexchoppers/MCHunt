package com.rexchoppers.mchunt.listeners;

import com.google.common.eventbus.Subscribe;
import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.enums.ArenaPlayerRole;
import com.rexchoppers.mchunt.enums.ArenaStatus;
import com.rexchoppers.mchunt.events.internal.PlayerDiedEvent;
import com.rexchoppers.mchunt.events.internal.PlayerJoinedArenaEvent;
import com.rexchoppers.mchunt.managers.LocalizationManager;
import com.rexchoppers.mchunt.models.Arena;
import com.rexchoppers.mchunt.models.ArenaPlayer;
import com.rexchoppers.mchunt.models.Countdown;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import static com.rexchoppers.mchunt.util.PlayerUtil.sendPlayerMessage;

public record PlayerDiedListener(MCHunt plugin) {

    @Subscribe
    public void broadcast(PlayerDiedEvent event) {
        Arena arena = event.arena();

        arena.getPlayers().forEach(player -> {
            Player serverPlayer = plugin.getServer().getPlayer(player.getUUID());

            if (serverPlayer == null) return;

            sendPlayerMessage(
                    serverPlayer,
                    new LocalizationManager(MCHunt.getCurrentLocale())
                            .getMessage(
                                    "arena.player_died",
                                    serverPlayer.getName()
                            )
            );
        });
    }

    @Subscribe
    public void switchTeamOrEndGame(PlayerDiedEvent event) {
        ArenaPlayer arenaPlayer = event.arenaPlayer();
        Arena arena = event.arena();

        if (arena.getHiders().size() <= 1) {
            // End the game if there are no hiders left
            arena.setStatus(ArenaStatus.FINISHED);
            // plugin.getServer().getPluginManager().callEvent(new com.rexchoppers.mchunt.events.internal.ArenaFinishedEvent(arena));
            return;
        }

        if (arenaPlayer.getRole() != null && arenaPlayer.getRole().equals(ArenaPlayerRole.HIDER)) {
            arenaPlayer.setRole(ArenaPlayerRole.SEEKER);

            // Give the player a respawn countdown
        }

    }
}
