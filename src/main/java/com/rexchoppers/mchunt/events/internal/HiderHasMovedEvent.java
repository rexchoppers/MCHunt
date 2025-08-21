package com.rexchoppers.mchunt.events.internal;

import com.rexchoppers.mchunt.models.Arena;
import com.rexchoppers.mchunt.models.ArenaPlayer;

public record HiderHasMovedEvent(
        Arena arena,
        ArenaPlayer hider
) {}