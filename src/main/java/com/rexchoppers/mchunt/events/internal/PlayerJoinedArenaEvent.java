package com.rexchoppers.mchunt.events.internal;

import java.util.UUID;

public record ArenaSetupPlayerJoinedEvent(UUID arenaSetupUuid, UUID playerUuid) {
}
