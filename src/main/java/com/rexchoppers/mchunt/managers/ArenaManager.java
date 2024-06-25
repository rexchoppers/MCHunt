package com.rexchoppers.mchunt.managers;

import com.google.gson.Gson;
import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.exceptions.ArenaExistsException;
import com.rexchoppers.mchunt.models.Arena;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import com.google.gson.reflect.TypeToken;

public class ArenaManager {
    private final MCHunt plugin;
    private final String filePath;

    private List<Arena> arenas;

    public ArenaManager(MCHunt plugin, String filePath) {
        this.plugin = plugin;
        this.filePath = filePath;

        init();
    }

    private void init() {
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }

        if (!file.exists()) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("[]");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean createArena(Arena arena) throws ArenaExistsException {
        List<Arena> arenas = load();

        // Check if arena exists with the same name
        if (getArenaByName(arenas, arena.getName()).isPresent()) {
            throw new ArenaExistsException(arena.getName());
        }

        arenas.add(arena);
        save(arenas);

        return true;
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
        try (FileReader reader = new FileReader(filePath)) {
            Type type = new TypeToken<List<Arena>>() {}.getType();
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void save(List<Arena> arenas) {
        Gson gson = plugin.getGson();
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(arenas, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Arena> getArenas() {
        return arenas;
    }
}
