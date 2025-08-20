package com.rexchoppers.mchunt.listeners;

import com.google.common.eventbus.Subscribe;
import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.enums.ArenaPlayerRole;
import com.rexchoppers.mchunt.events.internal.ArenaSeekersReleasedEvent;
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

public record ArenaSeekersReleasedListener(MCHunt plugin) {
    @Subscribe
    public void broadcastSeekersReleased(ArenaSeekersReleasedEvent event) {
        Arena arena = event.arena();

        arena.getPlayers().forEach(player -> {
            // Broadcast that the seekers have been released
            Player serverPlayer = Bukkit.getPlayer(player.getUUID());

            if (serverPlayer == null) return;

            sendPlayerAudibleMessage(
                    serverPlayer,
                    new LocalizationManager(MCHunt.getCurrentLocale())
                            .getMessage("arena.seekers_released")
            );
        });
    }

    @Subscribe
    public void giveSeekersItems(ArenaSeekersReleasedEvent event) {
        Arena arena = event.arena();

        arena.getPlayers().stream()
                .filter(player -> player.getRole().equals(ArenaPlayerRole.SEEKER))
                .map(player -> Bukkit.getPlayer(player.getUUID()))
                .filter(player -> player != null && player.isOnline())
                .forEach(player -> {
                    plugin.getItemManager().setSeekerItems(player);
                });
    }
}
