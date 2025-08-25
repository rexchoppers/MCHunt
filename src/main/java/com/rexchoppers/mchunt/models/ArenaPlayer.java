package com.rexchoppers.mchunt.models;

import com.rexchoppers.mchunt.enums.ArenaPlayerRole;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.UUID;

public class ArenaPlayer {
    private UUID uuid;

    private ArenaPlayerRole role;

    // For location tracking
    private Location lastLocation = null;
    private long lastMovement = 0;

    private Material disguiseMaterial;

    private boolean disguiseLocked = false;

    private Location disguiseLocation = null;

    private Countdown respawnCountdown = null;

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

    public long getLastMovement() {
        return lastMovement;
    }

    public void updateMovement(Location current) {
        if (this.lastLocation == null) {
            this.lastLocation = current.clone();
            this.lastMovement = System.currentTimeMillis();
            return;
        }

        if (!current.toVector().equals(lastLocation.toVector())) {
            this.lastMovement = System.currentTimeMillis();
            this.lastLocation = current.clone();
        }
    }

    public void resetMovement() {
        this.lastLocation = null;
        this.lastMovement = 0;
    }

    public boolean hasBeenStillFor(long ms) {
        return (System.currentTimeMillis() - lastMovement) >= ms;
    }

    public Material getDisguiseMaterial() {
        return disguiseMaterial;
    }

    public void setDisguiseMaterial(Material disguiseMaterial) {
        this.disguiseMaterial = disguiseMaterial;
    }

    public boolean isDisguiseLocked() {
        return disguiseLocked;
    }

    public void setDisguiseLocked(boolean disguiseLocked) {
        this.disguiseLocked = disguiseLocked;
    }

    public boolean hasMovedRecently() {
        return !hasBeenStillFor(500);
    }

    public Location getDisguiseLocation() {
        return disguiseLocation;
    }

    public void setDisguiseLocation(Location disguiseLocation) {
        this.disguiseLocation = disguiseLocation;
    }

    public Countdown getRespawnCountdown() {
        return respawnCountdown;
    }

    public void setRespawnCountdown(Countdown respawnCountdown) {
        this.respawnCountdown = respawnCountdown;
    }
}
