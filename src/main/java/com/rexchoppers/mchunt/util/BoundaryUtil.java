package com.rexchoppers.mchunt.util;

import com.rexchoppers.mchunt.models.ArenaSetup;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import java.util.Map;

public class BoundaryUtil {
    public void drawArenaBoundary(Player player, ArenaSetup arenaSetup) {
        Location corner1 = arenaSetup.getLocationBoundaryPoint1();
        Location corner2 = arenaSetup.getLocationBoundaryPoint2();

        int topBlockX = Math.max(corner1.getBlockX(), corner2.getBlockX());
        int bottomBlockX = Math.min(corner1.getBlockX(), corner2.getBlockX());

        int topBlockZ = Math.max(corner1.getBlockZ(), corner2.getBlockZ());
        int bottomBlockZ = Math.min(corner1.getBlockZ(), corner2.getBlockZ());

        int y = corner1.getBlockY();

        // Retrieve the tracking map from the arena setup
        Map<Location, BlockData> boundaryTracking = arenaSetup.getTmpBoundaryTracking();

        for (int x = bottomBlockX; x <= topBlockX; x++) {
            updateBoundary(player, boundaryTracking, new Location(player.getWorld(), x, y, bottomBlockZ), Material.GLOWSTONE);
            updateBoundary(player, boundaryTracking, new Location(player.getWorld(), x, y, topBlockZ), Material.GLOWSTONE);
        }

        for (int z = bottomBlockZ + 1; z < topBlockZ; z++) {
            updateBoundary(player, boundaryTracking, new Location(player.getWorld(), bottomBlockX, y, z), Material.GLOWSTONE);
            updateBoundary(player, boundaryTracking, new Location(player.getWorld(), topBlockX, y, z), Material.GLOWSTONE);
        }

        arenaSetup.setTmpBoundaryTracking(boundaryTracking);
    }

    private void updateBoundary(Player player, Map<Location, BlockData> boundaryTracking, Location location, Material newMaterial) {
        if (!boundaryTracking.containsKey(location)) {
            boundaryTracking.put(location, location.getBlock().getBlockData());
        }
        player.sendBlockChange(location, newMaterial.createBlockData());
    }

    public void clearTemporaryBoundary(Player player, ArenaSetup arenaSetup) {
        Map<Location, BlockData> boundaryTracking = arenaSetup.getTmpBoundaryTracking();
        for (Map.Entry<Location, BlockData> entry : boundaryTracking.entrySet()) {
            player.sendBlockChange(entry.getKey(), entry.getValue());
        }
        arenaSetup.clearTmpBoundaryTracking();
    }
}
