package com.rexchoppers.mchunt.listeners;

import com.google.common.eventbus.Subscribe;
import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.enums.ArenaPlayerRole;
import com.rexchoppers.mchunt.enums.ArenaStatus;
import com.rexchoppers.mchunt.events.internal.ArenaFinishedEvent;
import com.rexchoppers.mchunt.managers.LocalizationManager;
import com.rexchoppers.mchunt.models.Arena;
import com.rexchoppers.mchunt.models.ArenaPlayer;
import com.rexchoppers.mchunt.models.Countdown;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

import static com.rexchoppers.mchunt.util.PlayerUtil.sendPlayerAudibleMessage;

public record ArenaResetCountdownEndedListener(MCHunt plugin) {
    @Subscribe
    public void startEndCountdown(ArenaFinishedEvent event) {
        Arena arena = event.arena();

        arena.setStatus(ArenaStatus.COUNTDOWN_RESET);
        arena.setResetCountdown(
                new Countdown(arena.getCountdownAfterEnd())
        );
    }
}
