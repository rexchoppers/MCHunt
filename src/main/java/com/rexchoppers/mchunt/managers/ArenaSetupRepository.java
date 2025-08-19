package com.rexchoppers.mchunt.managers;

import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.events.internal.ArenaSetupUpdatedEvent;
import com.rexchoppers.mchunt.exceptions.PlayerAlreadyInArenaSetupException;
import com.rexchoppers.mchunt.models.ArenaSetup;
import com.rexchoppers.mchunt.repositories.Repository;

import java.util.Optional;
import java.util.UUID;

public class ArenaSetupRepository extends Repository<ArenaSetup> {
    public ArenaSetupRepository(MCHunt mchunt, String directory) {
        super(mchunt, directory);
    }

    @Override
    public void postInit() {
        for (ArenaSetup arenaSetup : this.getData()) {
            this.getPlugin().getSignManager().initArenaSetupSigns(arenaSetup);
        }
    }

    @Override
    public void postUpdate(ArenaSetup arenaSetup) {
        this.getPlugin().getEventBusManager().publishEvent(new ArenaSetupUpdatedEvent(arenaSetup.getUUID()));
    }

    @Override
    public void create(ArenaSetup arenaSetup) throws Exception {
        if (this.getArenaSetupForPlayer(arenaSetup.getPlayerUuid()).isPresent()) {
            throw new PlayerAlreadyInArenaSetupException();
        }

        super.create(arenaSetup);
    }

    public Optional<ArenaSetup> getArenaSetupForPlayer(UUID playerUuid) {
        return this.getData().stream()
                .filter(arenaSetup -> arenaSetup.getPlayerUuid().equals(playerUuid))
                .findFirst();
    }
}
