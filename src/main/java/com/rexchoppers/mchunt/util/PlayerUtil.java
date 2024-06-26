package com.rexchoppers.mchunt.util;

import org.bukkit.entity.Player;

public class PlayerUtil {
    public static void sendPlayerError(Player player, String message) {
        player.playSound(player.getLocation(), "entity.villager.no", 1.0F, 1.0F);
        player.sendMessage(Format.processString("%TAG %e" + message));
    }
}
