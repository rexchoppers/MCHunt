package com.rexchoppers.mchunt.managers;

import com.google.gson.Gson;
import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.models.Arena;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ArenaManager {
    private final MCHunt plugin;
    private final String directoryPath;

    private List<Arena> arenas;

    public ArenaManager(MCHunt plugin, String directoryPath) {
        this.plugin = plugin;
        this.directoryPath = directoryPath;

        init();
    }


    // Load signs
       /* for (Arena arena : arenas) {
            this.plugin.getSignManager().initArenaSigns(arena);
        }*/

    public Optional<Arena> getArenaByUUID(UUID uuid) {
        if (arenas == null) {
            return Optional.empty();
        }

        return arenas.stream()
                .filter(arena -> arena.getUUID().equals(uuid))
                .findFirst();
    }

    public void createArena(Arena arena) {
        if (getArenaByUUID(arena.getUUID()).isPresent()) {
            return;
        }

        saveArena(arena);
        arenas.add(arena);
    }

    public void updateArena(Arena arena) {
        // No-op replacement since arenas holds the same reference; just persist
        saveArena(arena);
    }

    public Optional<Arena> getArenaByName(List<Arena> arenas, String name) {
        if (arenas == null) {
            return Optional.empty();
        }

        return arenas.stream()
                .filter(arena -> arena.getName().equals(name))
                .findFirst();
    }


    private File getArenaFile(UUID uuid) {
        return new File(directoryPath, uuid.toString() + ".json");
    }

    public void saveArena(Arena arena) {
        Gson gson = plugin.getGson();
        File file = getArenaFile(arena.getUUID());
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(arena, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveAll() {
        if (arenas == null) return;
        for (Arena arena : arenas) {
            saveArena(arena);
        }
    }

    public List<Arena> getArenas() {
        return arenas;
    }

    public boolean isPlayerInArena(UUID playerUUID) {
        return arenas.stream()
                .anyMatch(arena -> arena.getPlayers().stream()
                        .anyMatch(player -> player.getUUID().equals(playerUUID)));
    }

    public Optional<Arena> getArenaByPlayerUUID(UUID playerUUID) {
        return arenas.stream()
                .filter(arena -> arena.getPlayers().stream()
                        .anyMatch(player -> player.getUUID().equals(playerUUID)))
                .findFirst();
    }
}
