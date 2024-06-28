package com.rexchoppers.mchunt.models;

import com.google.gson.annotations.Expose;
import com.rexchoppers.mchunt.enums.ArenaStatus;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ArenaSetup {
    @Expose
    private UUID playerUuid;

    @Expose
    private String arenaName;

    @Expose
    private ItemStack[] inventory;

    @Expose
    private Location[] arenaSigns;

    @Expose
    private Location locationBoundaryPoint1;

    @Expose
    private Location locationBoundaryPoint2;

    private Map<Location, BlockData> tmpBoundaryTracking = new HashMap<>();

    public ArenaSetup(UUID playerUuid, ItemStack[] inventory) {
        this.playerUuid = playerUuid;
        this.inventory = inventory;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public ItemStack[] getInventory() {
        return inventory;
    }

    public Location getLocationBoundaryPoint1() {
        return locationBoundaryPoint1;
    }

    public void setLocationBoundaryPoint1(Location locationBoundaryPoint1) {
        this.locationBoundaryPoint1 = locationBoundaryPoint1;
    }

    public Location getLocationBoundaryPoint2() {
        return locationBoundaryPoint2;
    }

    public void setLocationBoundaryPoint2(Location locationBoundaryPoint2) {
        this.locationBoundaryPoint2 = locationBoundaryPoint2;
    }

    public Map<Location, BlockData> getTmpBoundaryTracking() {
        if (tmpBoundaryTracking == null) {
            tmpBoundaryTracking = new HashMap<>();
        }
        return tmpBoundaryTracking;
    }

    public void setTmpBoundaryTracking(Map<Location, BlockData> tmpBoundaryTracking) {
        this.tmpBoundaryTracking = tmpBoundaryTracking;
    }

    public void clearTmpBoundaryTracking() {
        this.tmpBoundaryTracking.clear();
    }

    public String getArenaName() {
        return arenaName;
    }

    public void setArenaName(String arenaName) {
        this.arenaName = arenaName;
    }

    public Location[] getArenaSigns() {
        return arenaSigns;
    }

    public void appendArenaSign(Location locationToAdd) {
            Location[] newArray = new Location[this.arenaSigns.length + 1];

            // Copy the contents of the original array to the new array
            System.arraycopy(this.arenaSigns, 0, newArray, 0, this.arenaSigns.length);

            // Add the new element to the end of the new array
            newArray[this.arenaSigns.length] = locationToAdd;

            this.arenaSigns = newArray;
    }

    public void setArenaSigns(Location[] arenaSigns) {
        this.arenaSigns = arenaSigns;
    }
}
