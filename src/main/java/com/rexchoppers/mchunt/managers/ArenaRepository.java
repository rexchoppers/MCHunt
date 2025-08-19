package com.rexchoppers.mchunt.managers;

import com.google.gson.Gson;
import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.models.Arena;
import com.rexchoppers.mchunt.repositories.Repository;

import java.io.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ArenaRepository extends Repository<Arena> {
    public ArenaRepository(MCHunt plugin, String directory) {
        super(plugin, directory);
    }


    @Override
    public void postInit() {
        for (Arena arena : this.getData()) {
            this.getPlugin().getSignManager().initArenaSigns(arena);
        }
    }

    public Optional<Arena> getArenaByName(List<Arena> arenas, String name) {
        if (arenas == null) {
            return Optional.empty();
        }

        return arenas.stream()
                .filter(arena -> arena.getName().equals(name))
                .findFirst();
    }


    public boolean isPlayerInArena(UUID playerUUID) {
        return this.getData().stream()
                .anyMatch(arena -> arena.getPlayers().stream()
                        .anyMatch(player -> player.getUUID().equals(playerUUID)));
    }

    public Optional<Arena> getArenaPlayerIsIn(UUID playerUUID) {
        return this.getData().stream()
                .filter(arena -> arena.getPlayers().stream()
                        .anyMatch(player -> player.getUUID().equals(playerUUID)))
                .findFirst();
    }
}
