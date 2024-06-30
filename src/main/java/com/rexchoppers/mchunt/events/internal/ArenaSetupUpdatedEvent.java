package com.rexchoppers.mchunt.events.internal;

public class ArenaSetupUpdatedEvent {
    private final String arenaSetupUuid;

    public ArenaSetupUpdatedEvent(String arenaSetupUuid) {
        this.arenaSetupUuid = arenaSetupUuid;
    }

    public String getArenaSetupUuid() {
        return arenaSetupUuid;
    }
}
