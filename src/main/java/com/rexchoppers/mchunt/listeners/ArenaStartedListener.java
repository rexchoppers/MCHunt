package com.rexchoppers.mchunt.listeners;

import com.google.common.eventbus.Subscribe;
import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.enums.ArenaPlayerRole;
import com.rexchoppers.mchunt.events.internal.ArenaStartedEvent;
import com.rexchoppers.mchunt.managers.LocalizationManager;
import com.rexchoppers.mchunt.models.Arena;
import com.rexchoppers.mchunt.models.ArenaPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static com.rexchoppers.mchunt.util.PlayerUtil.sendPlayerAudibleMessage;

public record ArenaStartedListener(MCHunt plugin) {
    @Subscribe
    public void assignTeams(ArenaStartedEvent event) {
        Arena arena = event.arena();

        // Get count of players in arena
        int playerCount = arena.getPlayers().size();

        // Get the seekers count on game start
        int seekerCount = arena.getSeekerCount();

        // Randomly set the seekers in the arena
        for (int i = 0; i < seekerCount; i++) {
            int randomIndex = (int) (Math.random() * playerCount);
            ArenaPlayer seeker = arena.getPlayers().get(randomIndex);
            Player player = Bukkit.getPlayer(seeker.getUUID());
            seeker.setRole(ArenaPlayerRole.SEEKER);

            // Send a message to the seeker
            sendPlayerAudibleMessage(
                    player,
                    new LocalizationManager(MCHunt.getCurrentLocale())
                            .getMessage("arena.seeker")
            );
        }

        // Set the rest of the players as hiders
        arena.getPlayers().forEach(player -> {
            if (player.getRole().equals(ArenaPlayerRole.HIDER)) return;

            // If they've already been
            if (player.getRole().equals(ArenaPlayerRole.SEEKER)) return;

            player.setRole(ArenaPlayerRole.HIDER);

            Bukkit.getServer().getPlayer(player.getUUID()).teleport(
                    arena.getHiderSpawns()[(int) (Math.random() * arena.getHiderSpawns().length)]);
        });

    }

    @Subscribe
    public void updateArenaSigns(ArenaStartedEvent event) {
        Arena arena = event.arena();
        plugin.getSignManager().initArenaSigns(arena);
    }
}
