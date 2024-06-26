package com.rexchoppers.mchunt.util;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class PlayerUtil {
    public static void sendPlayerError(Player player, String message) {
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F);
        player.sendMessage(Format.processString("%TAG %e" + message));
    }

    public static void sendPlayerAudibleMessage(Player player, String message) {
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
        player.sendMessage(Format.processString(message));
    }
}
