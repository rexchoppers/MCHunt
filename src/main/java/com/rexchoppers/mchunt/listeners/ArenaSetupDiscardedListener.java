package com.rexchoppers.mchunt.listeners;

import com.google.common.eventbus.Subscribe;
import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.events.internal.ArenaSetupDiscardedEvent;
import com.rexchoppers.mchunt.events.internal.ArenaSetupPlayerJoinedEvent;
import com.rexchoppers.mchunt.events.internal.ArenaSetupUpdatedEvent;
import com.rexchoppers.mchunt.models.ArenaSetup;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

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

    @Subscribe
    public void resetArenaSetupBoundary(ArenaSetupDiscardedEvent event){
        ArenaSetup arenaSetup = event.arenaSetup();

        Map<Location, BlockData> blocks = arenaSetup.getTmpBoundaryTracking();
        Player player = plugin.getServer().getPlayer(arenaSetup.getPlayerUuid());

        for (Map.Entry<Location, BlockData> entry : blocks.entrySet()) {
            Location location = entry.getKey();

            // Get the block data from the location
            BlockData blockData = location.getBlock().getBlockData();

            if (player != null && player.isOnline()) {
                player.sendBlockChange(location, blockData);
            }
        }
    }

    @Subscribe
    public void resetLocationMarkers(ArenaSetupDiscardedEvent event){
        ArenaSetup arenaSetup = event.arenaSetup();

        Player player = Bukkit.getPlayer(arenaSetup.getPlayerUuid());
        if (player == null) return;
        if (!player.isOnline()) return;

        player.sendBlockChange(arenaSetup.getLobbySpawn(), arenaSetup.getLobbySpawn().getBlock().getBlockData());
        player.sendBlockChange(arenaSetup.getAfterGameSpawn(), arenaSetup.getAfterGameSpawn().getBlock().getBlockData());

        for (Location location : arenaSetup.getHiderSpawns()) {
            player.sendBlockChange(location, location.getBlock().getBlockData());
        }

        for (Location location : arenaSetup.getSeekerSpawns()) {
            player.sendBlockChange(location, location.getBlock().getBlockData());
        }
    }
}