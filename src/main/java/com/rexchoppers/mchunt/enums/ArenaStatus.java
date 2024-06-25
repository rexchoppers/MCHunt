package com.rexchoppers.mchunt.enums;

public enum ArenaStatus {
    // Admin has turned the arena offline
    OFFLINE("offline"),

    // Arena is waiting for players to join
    WAITING("waiting"),

    // Arena is counting down to start
    COUNTDOWN_START("countdown_start"),

    // Arena is in progress
    IN_PROGRESS("in_progress"),

    // Arena is counting down to end
    COUNTDOWN_END("countdown_end");

    private final String key;

    ArenaStatus(String key) {
        this.key = key;
    }

    // Getter for the key
    public String getKey() {
        return key;
    }
}
