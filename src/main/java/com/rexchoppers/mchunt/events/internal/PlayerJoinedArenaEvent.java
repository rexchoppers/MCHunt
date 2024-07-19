package com.rexchoppers.mchunt.events.internal;

import java.util.UUID;

public record PlayerJoinedArenaEvent(UUID arenaUuid, UUID playerUuid) {
}
