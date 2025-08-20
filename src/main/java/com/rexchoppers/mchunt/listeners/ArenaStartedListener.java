package com.rexchoppers.mchunt.listeners;

import com.google.common.eventbus.Subscribe;
import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.enums.ArenaPlayerRole;
import com.rexchoppers.mchunt.events.internal.ArenaStartedEvent;
import com.rexchoppers.mchunt.managers.LocalizationManager;
import com.rexchoppers.mchunt.models.Arena;
import com.rexchoppers.mchunt.models.ArenaPlayer;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.DisguiseConfig;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MiscDisguise;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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

        // Set the rest of the players as hiders, skip them
        arena.getPlayers().forEach(hider -> {
            Player player = Bukkit.getPlayer(hider.getUUID());

            // Skip seekers already
            if (hider.getRole() != null && hider.getRole().equals(ArenaPlayerRole.SEEKER)) return;

            hider.setRole(ArenaPlayerRole.HIDER);

            // Get blocks for disguise
            String[] blocks = arena.getArenaBlocks();

            // Randomly set user's disguise
            String randomBlock = blocks[(int) (Math.random() * blocks.length)];
            Material disguiseMaterial = Material.getMaterial(randomBlock.toUpperCase());

            if (disguiseMaterial != null) {
                MiscDisguise disguise = new MiscDisguise(DisguiseType.FALLING_BLOCK, disguiseMaterial);
                disguise.setNotifyBar(DisguiseConfig.NotifyBar.NONE);
                DisguiseAPI.disguiseToAll(player, disguise);
            } else {
                // Fallback to a default block if the material is invalid
                MiscDisguise disguise = new MiscDisguise(DisguiseType.FALLING_BLOCK, Material.STONE);
                disguise.setNotifyBar(DisguiseConfig.NotifyBar.NONE);
                DisguiseAPI.disguiseToAll(player, disguise);
            }

            player.teleport(arena.getHiderSpawns()[(int) (Math.random() * arena.getHiderSpawns().length)]);

            // Send a message to the hider
            sendPlayerAudibleMessage(
                    player,
                    new LocalizationManager(MCHunt.getCurrentLocale())
                            .getMessage("arena.hider", disguiseMaterial.toString())
            );
        });
    }

    @Subscribe
    public void updateArenaSigns(ArenaStartedEvent event) {
        Arena arena = event.arena();
        plugin.getSignManager().initArenaSigns(arena);
    }
}
