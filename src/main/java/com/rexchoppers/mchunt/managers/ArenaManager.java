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

    private void init() {
        File dir = new File(directoryPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        arenas = load();

        // Load signs
        for (Arena arena : arenas) {
            this.plugin.getSignManager().initArenaSigns(arena);
        }
    }

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
        if (arenas == null) {
            arenas = new ArrayList<>();
        }

        // Replace in-memory instance if a different object with the same UUID was provided
        for (int i = 0; i < arenas.size(); i++) {
            if (arenas.get(i).getUUID().equals(arena.getUUID())) {
                if (arenas.get(i) != arena) {
                    arenas.set(i, arena);
                }
                // Refresh signs to reflect any updated arena state (name, players, status, sign locations, etc.)
                this.plugin.getSignManager().initArenaSigns(arena);
                saveArena(arena);
                return;
            }
        }

        // If the arena wasn't present (edge case), add it, init signs, and persist
        arenas.add(arena);
        this.plugin.getSignManager().initArenaSigns(arena);
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

    private List<Arena> load() {
        Gson gson = plugin.getGson();
        File dir = new File(directoryPath);
        File[] files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".json"));
        if (files == null || files.length == 0) {
            return new ArrayList<>();
        }

        List<Arena> loaded = new ArrayList<>();
        Arrays.sort(files); // stable order
        for (File f : files) {
            try (FileReader reader = new FileReader(f)) {
                Arena arena = gson.fromJson(reader, Arena.class);
                if (arena != null) {
                    loaded.add(arena);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return loaded;
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
