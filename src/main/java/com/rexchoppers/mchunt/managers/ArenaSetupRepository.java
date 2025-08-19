package com.rexchoppers.mchunt.managers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.events.internal.ArenaSetupUpdatedEvent;
import com.rexchoppers.mchunt.exceptions.PlayerAlreadyInArenaSetupException;
import com.rexchoppers.mchunt.models.ArenaSetup;
import com.rexchoppers.mchunt.repositories.Repository;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
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



    /**
     *         this.plugin.getEventBusManager().publishEvent(new ArenaSetupUpdatedEvent(arenaSetup.getUUID()));
     */
}
