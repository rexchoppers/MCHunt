package com.rexchoppers.mchunt.enums;

public enum ArenaPlayerRole {
    SEEKER("seeker"),
    HIDER("hider"),
    RESPAWNING_AS_HIDER("respawning-as-hider");

    private final String key;

    ArenaPlayerRole(String key) {
        this.key = key;
    }

    // Getter for the key
    public String getKey() {
        return key;
    }
}
