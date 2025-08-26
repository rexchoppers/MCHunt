package com.rexchoppers.mchunt.listeners;

import com.google.common.eventbus.Subscribe;
import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.enums.ArenaPlayerRole;
import com.rexchoppers.mchunt.enums.ArenaStatus;
import com.rexchoppers.mchunt.events.internal.ArenaFinishedEvent;
import com.rexchoppers.mchunt.events.internal.ArenaResetCountdownEndedEvent;
import com.rexchoppers.mchunt.managers.LocalizationManager;
import com.rexchoppers.mchunt.models.Arena;
import com.rexchoppers.mchunt.models.ArenaPlayer;
import com.rexchoppers.mchunt.models.Countdown;
import me.libraryaddict.disguise.DisguiseAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

import static com.rexchoppers.mchunt.util.PlayerUtil.sendPlayerAudibleMessage;

public record ArenaResetCountdownEndedListener(MCHunt plugin) {

    @Subscribe
    public void teleportPlayers(ArenaResetCountdownEndedEvent event) {
        Arena arena = event.arena();
        List<ArenaPlayer> players = event.players();

        String message = MCHunt.getLocalization()
                .getMessage("arena.teleporting_to_lobby");

        players.forEach(player -> {
            Player serverPlayer = Bukkit.getPlayer(player.getUUID());

            if (serverPlayer != null) {
                sendPlayerAudibleMessage(
                        serverPlayer,
                        message
                );

                // Un-disguise the player if they are disguised
                if (DisguiseAPI.isDisguised(serverPlayer)) {
                    DisguiseAPI.undisguiseToAll(serverPlayer);
                }

                // @TODO: There must be a better way to do this
                // Show the player to all other players
                Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
                    // Don't hide the player from themselves
                    if (onlinePlayer.getUniqueId().equals(player.getUUID())) return;

                    onlinePlayer.showPlayer(plugin, serverPlayer);
                });

                serverPlayer.teleport(arena.getAfterGameSpawn());
                serverPlayer.getInventory().clear();
                serverPlayer.setHealth(20.0);
                serverPlayer.setFoodLevel(20);
            }
        });
    }

    @Subscribe
    public void resetArena(ArenaResetCountdownEndedEvent event) {
        Arena arena = event.arena();

        // Reset arena status
        // Empty the arena player list
        arena.getPlayers().clear();
        arena.setStatus(ArenaStatus.WAITING);

        arena.setCurrentGameTime(0);
        arena.setStartCountdown(null);
        arena.setResetCountdown(null);
        arena.setSeekersReleased(false);

        // Update arena signs
        plugin.getSignManager().initArenaSigns(arena);
    }
}
