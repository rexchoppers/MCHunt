package com.rexchoppers.mchunt.events.internal;

import java.util.UUID;

public record PlayerLeftArenaEvent(UUID arenaUuid, UUID playerUuid) {}