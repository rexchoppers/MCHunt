package com.rexchoppers.mchunt.models;

import com.rexchoppers.mchunt.enums.ArenaPlayerRole;

import java.util.UUID;

public class ArenaPlayer {
    private UUID uuid;

    private ArenaPlayerRole role;

    public ArenaPlayer(UUID uuid, ArenaPlayerRole role) {
        this.uuid = uuid;
        this.role = role;
    }
}
