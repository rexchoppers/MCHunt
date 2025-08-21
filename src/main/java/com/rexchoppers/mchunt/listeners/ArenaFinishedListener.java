package com.rexchoppers.mchunt.listeners;

import com.google.common.eventbus.Subscribe;
import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.enums.ArenaPlayerRole;
import com.rexchoppers.mchunt.events.internal.ArenaFinishedEvent;
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

public record ArenaFinishedListener(MCHunt plugin) {
    @Subscribe
    public void declareWinner(ArenaFinishedEvent event) {
        
    }

    @Subscribe
    public void updateArenaSigns(ArenaFinishedEvent event) {
        Arena arena = event.arena();
        plugin.getSignManager().initArenaSigns(arena);
    }
}
