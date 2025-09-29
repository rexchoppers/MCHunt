package com.rexchoppers.mchunt.listeners;

import com.google.common.eventbus.Subscribe;
import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.enums.ArenaPlayerRole;
import com.rexchoppers.mchunt.enums.ArenaStatus;
import com.rexchoppers.mchunt.events.internal.ArenaFinishedEvent;
import com.rexchoppers.mchunt.events.internal.PlayerDiedEvent;
import com.rexchoppers.mchunt.models.Arena;
import com.rexchoppers.mchunt.models.ArenaPlayer;
import com.rexchoppers.mchunt.models.Countdown;
import me.libraryaddict.disguise.DisguiseAPI;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import static com.rexchoppers.mchunt.util.PlayerUtil.sendPlayerMessage;

public record PlayerDiedListener(MCHunt plugin) {

    @Subscribe
    public void broadcast(PlayerDiedEvent event) {
        Arena arena = event.arena();
        ArenaPlayer arenaPlayer = event.arenaPlayer();
        Player arenaServerPlayer = plugin.getServer().getPlayer(arenaPlayer.getUUID());

        if (arenaServerPlayer == null) return;

        arena.getPlayers().forEach(player -> {
            Player serverPlayer = plugin.getServer().getPlayer(player.getUUID());

            if (serverPlayer == null) return;

            sendPlayerMessage(
                    serverPlayer,
                    MCHunt.getLocalization()
                            .getMessage(
                                    "arena.player_died",
                                    arenaServerPlayer.getName()
                            )
            );
        });
    }

    @Subscribe
    public void switchTeamOrEndGame(PlayerDiedEvent event) {
        ArenaPlayer arenaPlayer = event.arenaPlayer();
        Arena arena = event.arena();

        Player serverPlayer = plugin.getServer().getPlayer(arenaPlayer.getUUID());

        // Set the player's role to seeker before checking the number of hiders
        if (arenaPlayer.getRole() != null && arenaPlayer.getRole().equals(ArenaPlayerRole.HIDER)) {
            arenaPlayer.setRole(ArenaPlayerRole.SEEKER);

            // Undisguise the player if they are disguised
            if (DisguiseAPI.isDisguised(serverPlayer)) {
                DisguiseAPI.undisguiseToAll(serverPlayer);
            }
        }

        if (arena.getHiders().isEmpty()) {
            // End the game if there are no hiders left
            arena.setStatus(ArenaStatus.FINISHED);

            // Usually I wouldn't fire events in listeners, but this one is needed to finish the game
            plugin.getEventBusManager().publishEvent(new ArenaFinishedEvent(arena));

            return;
        }

        // Teleport new seeker to the lobby spawn
        if (serverPlayer != null) {
            serverPlayer.teleport(arena.getLobbySpawn());
            serverPlayer.setHealth(20.0);
            serverPlayer.setFoodLevel(20);

            arenaPlayer.setRespawnCountdown(new Countdown(
                    arena.getRespawnDelay()
            ));
        }
    }
}
