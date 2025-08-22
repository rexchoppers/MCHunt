package com.rexchoppers.mchunt.events.internal;

import com.rexchoppers.mchunt.models.Arena;
import com.rexchoppers.mchunt.models.ArenaPlayer;

import java.util.List;

public record ArenaResetCountdownEndedEvent(
        Arena arena,
        List<ArenaPlayer> players
) {
}
