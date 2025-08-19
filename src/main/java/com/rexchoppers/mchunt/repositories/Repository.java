package com.rexchoppers.mchunt.repositories;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rexchoppers.mchunt.MCHunt;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

/**
 * These repositories are used to load and save data from JSON files. All the data will be kept in memory
 * @param <T>
 */
public abstract class Repository<T> {

    private final MCHunt plugin;
    private final String directory;

    private List<T> data;

    public Repository(MCHunt plugin, String directory) {
        this.plugin = plugin;
        this.directory = directory;

        init();
    }

    private void init() {
        File dir = new File(directory);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        data = load();
        this.postInit();
    }

    private List<T> load() {
        Gson gson = plugin.getGson();
        File dir = new File(directory);
        File[] files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".json"));
        if (files == null || files.length == 0) {
            return new ArrayList<>();
        }

        List<T> loaded = new ArrayList<>();
        Arrays.sort(files);

        for (File f : files) {
            try (FileReader reader = new FileReader(f)) {
                Type type = new TypeToken<List<T>>() {}.getType();
                T data = gson.fromJson(reader, type);

                if (data != null) {
                    loaded.add(data);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return loaded;
    }

    public Optional<T> getByUUID(UUID uuid) {
        if (data == null) {
            return Optional.empty();
        }

        return data.stream()
                .filter(item -> item instanceof UUID && item.equals(uuid))
                .findFirst();
    }

    public void create(T item) {
        if (getByUUID(((UUID) item)).isPresent()) {
            return;
        }

        save(item);
        data.add(item);
    }

    public void update(T item) {
        Optional<T> existingItem = getByUUID(((UUID) item));
        existingItem.ifPresent(t -> data.remove(t));
        save(item);
        data.add(item);
    }

    private void save(T item) {
        Gson gson = plugin.getGson();
        File file = new File(directory, item.toString() + ".json");
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(item, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void postInit() {}

    public List<T> getData() {
        return data;
    }

    public MCHunt getPlugin() {
        return plugin;
    }
}
