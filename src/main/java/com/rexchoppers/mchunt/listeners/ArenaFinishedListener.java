package com.rexchoppers.mchunt.listeners;

import com.google.common.eventbus.Subscribe;
import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.enums.ArenaPlayerRole;
import com.rexchoppers.mchunt.enums.ArenaStatus;
import com.rexchoppers.mchunt.events.internal.ArenaFinishedEvent;
import com.rexchoppers.mchunt.events.internal.ArenaStartedEvent;
import com.rexchoppers.mchunt.items.ItemBuilder;
import com.rexchoppers.mchunt.managers.LocalizationManager;
import com.rexchoppers.mchunt.models.Arena;
import com.rexchoppers.mchunt.models.ArenaPlayer;
import com.rexchoppers.mchunt.models.Countdown;
import com.rexchoppers.mchunt.util.TimeUtil;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.DisguiseConfig;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MiscDisguise;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static com.rexchoppers.mchunt.util.PlayerUtil.sendPlayerAudibleMessage;

public record ArenaFinishedListener(MCHunt plugin) {
    @Subscribe
    public void broadcastGameInformation(ArenaFinishedEvent event) {
        Arena arena = event.arena();

        // If there are no hiders, declare seekers as winners
        List<ArenaPlayer> hiders = arena.getPlayers().stream()
                .filter(player -> player.getRole().equals(ArenaPlayerRole.HIDER))
                .toList();

        List<ArenaPlayer> seekers = arena.getPlayers().stream()
                .filter(player -> player.getRole().equals(ArenaPlayerRole.SEEKER))
                .toList();

        String message = null;
        if (hiders.isEmpty()) {
           message = MCHunt.getLocalization()
                   .getMessage("arena.seekers_win");
        }

        if (seekers.isEmpty()) {
            message = MCHunt.getLocalization()
                    .getMessage("arena.hiders_win");
        }

        if (message != null) {
            String finalMessage = message;
            arena.getPlayers().forEach(player -> {
                Player serverPlayer = Bukkit.getPlayer(player.getUUID());

                if (serverPlayer != null) {
                    sendPlayerAudibleMessage(
                            serverPlayer,
                            finalMessage
                    );
                }
            });
        }

        int timeLeft = Math.max(0, arena.getCurrentGameTime());
        int elapsed = Math.max(0, arena.getGameLength() - timeLeft);
        String formatted = TimeUtil.formatTime(elapsed);

        arena.getPlayers().forEach(player -> {
            Player serverPlayer = Bukkit.getPlayer(player.getUUID());
            if (serverPlayer != null) {
                sendPlayerAudibleMessage(serverPlayer,  MCHunt.getLocalization()
                        .getMessage("arena.game_ended_in_time", formatted));
            }
        });
    }

    @Subscribe
    public void startEndCountdown(ArenaFinishedEvent event) {
        Arena arena = event.arena();

        arena.setStatus(ArenaStatus.COUNTDOWN_RESET);
        arena.setResetCountdown(
                new Countdown(arena.getCountdownAfterEnd())
        );
    }

    @Subscribe
    public void updateArenaSigns(ArenaFinishedEvent event) {
        Arena arena = event.arena();
        plugin.getSignManager().initArenaSigns(arena);
    }
}
