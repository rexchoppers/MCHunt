package com.rexchoppers.mchunt.models;

import com.google.gson.annotations.Expose;
import com.rexchoppers.mchunt.enums.ArenaStatus;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

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
    private String[] arenaBlocks;

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
        if (arenaSigns == null) {
            arenaSigns = new Location[0];
        }

        // Convert the array to a list allowing nulls, then filter out nulls
        List<Location> arenaSignsList = new ArrayList<>(Arrays.asList(arenaSigns));
        arenaSignsList.removeIf(Objects::isNull);
        arenaSigns = arenaSignsList.toArray(new Location[0]);

        return arenaSigns;
    }

    public void appendArenaSign(Location locationToAdd) {
            Location[] currentArenaSigns = this.getArenaSigns();
            Location[] newArray = new Location[this.arenaSigns.length + 1];

            // Copy the contents of the original array to the new array
            System.arraycopy(currentArenaSigns, 0, newArray, 0, currentArenaSigns.length);

            // Add the new element to the end of the new array
            newArray[currentArenaSigns.length] = locationToAdd;

            this.setArenaSigns(newArray);
    }

    public void removeArenaSign(Location locationToRemove) {
        Location[] currentArenaSigns = this.getArenaSigns();

        if (currentArenaSigns == null || currentArenaSigns.length == 0) {
            this.setArenaSigns(new Location[0]);
            return;
        }

        Location[] newArray = new Location[currentArenaSigns.length - 1];
        int j = 0;
        for (Location currentArenaSign : currentArenaSigns) {
            if (currentArenaSign == null) continue;

            if (currentArenaSign.equals(locationToRemove)) {
                continue;
            }

            newArray[j] = currentArenaSign;
            j++;
        }

        this.setArenaSigns(newArray);
    }

    public void setArenaSigns(Location[] arenaSigns) {
        this.arenaSigns = arenaSigns;
    }

    public String[] getArenaBlocks() {
        if (arenaBlocks == null) {
            arenaBlocks = new String[0];
        }

        return arenaBlocks;
    }

    public void setArenaBlocks(String[] arenaBlocks) {
        this.arenaBlocks = arenaBlocks;
    }

    public void appendArenaBlock(String blockToAdd) {
        String[] currentArenaBlocks = this.getArenaBlocks();
        String[] newArray = new String[this.arenaBlocks.length + 1];

        // Copy the contents of the original array to the new array
        System.arraycopy(currentArenaBlocks, 0, newArray, 0, currentArenaBlocks.length);

        // Add the new element to the end of the new array
        newArray[currentArenaBlocks.length] = blockToAdd;

        this.setArenaBlocks(newArray);
    }

    public void removeArenaBlock(String blockToRemove) {
        String[] currentArenaBlocks = this.getArenaBlocks();

        if (currentArenaBlocks == null || currentArenaBlocks.length == 0) {
            this.setArenaBlocks(new String[0]);
            return;
        }

        String[] newArray = new String[currentArenaBlocks.length - 1];
        int j = 0;
        for (String currentArenaBlock : currentArenaBlocks) {
            if (currentArenaBlock == null) continue;

            if (currentArenaBlock.equals(blockToRemove)) {
                continue;
            }

            newArray[j] = currentArenaBlock;
            j++;
        }

        this.setArenaBlocks(newArray);
    }
}
