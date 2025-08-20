package com.rexchoppers.mchunt.listeners;

import com.google.common.eventbus.Subscribe;
import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.enums.ArenaPlayerRole;
import com.rexchoppers.mchunt.events.internal.ArenaStartedEvent;
import com.rexchoppers.mchunt.items.ItemBuilder;
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
import org.bukkit.inventory.ItemStack;

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

        ArenaPlayer[] seekers = arena.getPlayers().stream()
                .filter(player -> player.getRole() != null && player.getRole().equals(ArenaPlayerRole.SEEKER))
                .toArray(ArenaPlayer[]::new);

        // Broadcast to all players but the seekers that the seekers have been selected
        String seekerList = String.join(", ",
                arena.getPlayers().stream()
                        .filter(player -> player.getRole() != null && player.getRole().equals(ArenaPlayerRole.SEEKER))
                        .map(player -> Bukkit.getPlayer(player.getUUID()).getName())
                        .toList());

        // Remove the last comma and space if there are multiple seekers
        if (seekerList.endsWith(", ")) {
            seekerList = seekerList.substring(0, seekerList.length() - 2);
        }

        String seekerMessage = new LocalizationManager(MCHunt.getCurrentLocale())
                .getMessage("arena.seeker_selected_single", seekerList);

        if (seekers.length > 1) {
            seekerMessage = new LocalizationManager(MCHunt.getCurrentLocale())
                    .getMessage("arena.seeker_selected_multiple", seekerList);
        }

        // Set the rest of the players as hiders, skip them
        String finalSeekerMessage = seekerMessage;

        arena.getPlayers().forEach(hider -> {
            Player player = Bukkit.getPlayer(hider.getUUID());

            // Skip seekers already
            if (hider.getRole() != null && hider.getRole().equals(ArenaPlayerRole.SEEKER)) return;

            hider.setRole(ArenaPlayerRole.HIDER);

            // Broadcast the message to all players in the arena apart from the seekers
            sendPlayerAudibleMessage(
                    player,
                    finalSeekerMessage
            );

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

            // Set block at end of player's hotbar to the disguise material
            ItemStack disguiseHotbarItem = new ItemBuilder(plugin)
                    .setMaterial(disguiseMaterial)
                    .setDroppable(false)
                    .setMovable(false)
                    .build();

            player.getInventory().setItem(8, disguiseHotbarItem);

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
