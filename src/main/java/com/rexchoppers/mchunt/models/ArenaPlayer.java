package com.rexchoppers.mchunt.models;

import com.rexchoppers.mchunt.enums.ArenaPlayerRole;

import java.util.UUID;

public class ArenaPlayer {
    private UUID uuid;

    private ArenaPlayerRole role;

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
}
