package com.rexchoppers.mchunt.listeners;

import com.google.common.eventbus.Subscribe;
import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.enums.ArenaStatus;
import com.rexchoppers.mchunt.events.internal.HiderHasMovedEvent;
import com.rexchoppers.mchunt.events.internal.HiderIsStillEvent;
import com.rexchoppers.mchunt.managers.LocalizationManager;
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

public record HiderHasMovedListener(MCHunt plugin) {

    @Subscribe
    public void setHiderDisguise(HiderHasMovedEvent event) {
        ArenaPlayer hider = event.hider();
        Player serverPlayer = Bukkit.getPlayer(hider.getUUID());

        // Set as disguised
        hider.setDisguiseLocked(false);

        Bukkit.getOnlinePlayers().forEach(player -> {
            // Don't hide the player from themselves
            if (player.getUniqueId().equals(hider.getUUID())) return;

            player.showPlayer(plugin, serverPlayer);

            if (hider.getDisguiseLocation() != null) {
                player.sendBlockChange(
                        hider.getDisguiseLocation(),
                        Material.AIR.createBlockData()
                );
            }
        });

        Material disguiseMaterial = hider.getDisguiseMaterial();

        MiscDisguise disguise = new MiscDisguise(DisguiseType.FALLING_BLOCK, disguiseMaterial);
        disguise.setNotifyBar(DisguiseConfig.NotifyBar.NONE);
        DisguiseAPI.disguiseToAll(serverPlayer, disguise);

        hider.setDisguiseLocation(null);

        hider.resetMovement();
    }

    @Subscribe
    public void sendMessage(HiderHasMovedEvent event) {
        ArenaPlayer hider = event.hider();
        Player serverPlayer = Bukkit.getPlayer(hider.getUUID());

        // Only send message if the player is online and the game is in progress
        if (serverPlayer != null && event.arena().getStatus().equals(ArenaStatus.IN_PROGRESS)) {
            sendPlayerAudibleMessage(
                    serverPlayer,
                    MCHunt.getLocalization()
                            .getMessage("player.hider.moved_unhidden")
            );
        }
    }
}
