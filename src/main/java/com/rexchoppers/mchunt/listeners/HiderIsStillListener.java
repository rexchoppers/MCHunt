package com.rexchoppers.mchunt.listeners;

import com.google.common.eventbus.Subscribe;
import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.enums.ArenaPlayerRole;
import com.rexchoppers.mchunt.events.internal.ArenaSeekersReleasedEvent;
import com.rexchoppers.mchunt.events.internal.HiderIsStillEvent;
import com.rexchoppers.mchunt.managers.LocalizationManager;
import com.rexchoppers.mchunt.models.Arena;
import com.rexchoppers.mchunt.models.ArenaPlayer;
import me.libraryaddict.disguise.DisguiseAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static com.rexchoppers.mchunt.util.PlayerUtil.sendPlayerAudibleMessage;

public record HiderIsStillListener(MCHunt plugin) {

    @Subscribe
    public void setBlock(HiderIsStillEvent event) {
        ArenaPlayer hider = event.hider();

        Player serverPlayer = Bukkit.getPlayer(hider.getUUID());

        if (serverPlayer == null || !serverPlayer.isOnline()) {
            return;
        }

        if (DisguiseAPI.isDisguised(serverPlayer)) {
            DisguiseAPI.undisguiseToAll(serverPlayer);
        }

        Bukkit.getOnlinePlayers().forEach(player -> {
            if (player.getUniqueId().equals(hider.getUUID())) return;

            player.hidePlayer(plugin, serverPlayer);


        });
    }
}
