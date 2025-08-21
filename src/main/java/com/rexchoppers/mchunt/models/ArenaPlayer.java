package com.rexchoppers.mchunt.models;

import com.rexchoppers.mchunt.enums.ArenaPlayerRole;
import org.bukkit.Location;

import java.util.UUID;

public class ArenaPlayer {
    private UUID uuid;

    private ArenaPlayerRole role;

    // For location tracking
    private Location lastLocation = null;
    private long lastMovement = 0;

    public ArenaPlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setRole(ArenaPlayerRole role) {
        this.role = role;
    }

    public ArenaPlayerRole getRole() {
        return role;
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(Location lastLocation) {
        this.lastLocation = lastLocation;
    }

    public long getLastMovement() {
        return lastMovement;
    }

    public void setLastMovement(long lastMovement) {
        this.lastMovement = lastMovement;
    }

    public void updateMovement(Location current) {
        if (!current.toVector().equals(lastLocation.toVector())) {
            lastMovement = System.currentTimeMillis();
            lastLocation = current.clone();
        }
    }

    public boolean hasBeenStillFor(long ms) {
        return (System.currentTimeMillis() - lastMovement) >= ms;
    }
}
