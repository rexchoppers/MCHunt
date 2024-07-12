package com.rexchoppers.mchunt.events.internal;

import com.rexchoppers.mchunt.models.Arena;
import com.rexchoppers.mchunt.models.ArenaSetup;

import java.util.UUID;

public record ArenaCreatedEvent(
        ArenaSetup arenaSetup,
        Arena arena
) {}
