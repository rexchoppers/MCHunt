package com.rexchoppers.mchunt.events.internal;

import com.rexchoppers.mchunt.models.Arena;

import java.util.UUID;

public record ArenaFinishedEvent(
        Arena arena
) {
}
