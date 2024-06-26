package com.rexchoppers.mchunt.models;

import com.google.gson.annotations.Expose;
import com.rexchoppers.mchunt.enums.ArenaStatus;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class ArenaSetup {
    @Expose
    private UUID playerUuid;

    @Expose
    private ItemStack[] inventory;

    @Expose
    private Location locationBoundaryPoint1;

    @Expose
    private Location locationBoundaryPoint2;

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
}
