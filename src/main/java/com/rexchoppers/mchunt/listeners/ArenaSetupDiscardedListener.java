package com.rexchoppers.mchunt.listeners;

import com.google.common.eventbus.Subscribe;
import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.events.internal.ArenaSetupDiscardedEvent;
import com.rexchoppers.mchunt.events.internal.ArenaSetupPlayerJoinedEvent;
import com.rexchoppers.mchunt.events.internal.ArenaSetupUpdatedEvent;
import com.rexchoppers.mchunt.models.ArenaSetup;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ArenaSetupDiscardedListener {
    private final MCHunt plugin;

    public ArenaSetupDiscardedListener(MCHunt plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void restorePlayerInventory(ArenaSetupDiscardedEvent event){
        ArenaSetup arenaSetup = event.arenaSetup();

        Player player = plugin.getServer().getPlayer(arenaSetup.getPlayerUuid());

        if (player == null) return;

        player.getInventory().clear();
        player.getInventory().setContents(arenaSetup.getInventory());
    }

    @Subscribe
    public void removeArenaSetupSigns(ArenaSetupDiscardedEvent event){
        ArenaSetup arenaSetup = event.arenaSetup();

        for (Location location : arenaSetup.getArenaSigns()) {
            location.getBlock().setType(Material.AIR);
        }
    }


}