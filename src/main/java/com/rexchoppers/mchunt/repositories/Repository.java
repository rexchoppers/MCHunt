package com.rexchoppers.mchunt.repositories;

import com.google.gson.Gson;
import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.util.Identifiable;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * These repositories are used to load and save data from JSON files. All the data will be kept in memory
 * @param <T>
 */
public abstract class Repository<T extends Identifiable> {

    private final MCHunt plugin;
    private final String directory;

    private List<T> data;

    private final Class<T> type;

    public Repository(
            MCHunt plugin,
            String directory,
            Class<T> type
    ) {
        this.plugin = plugin;
        this.directory = directory;
        this.type = type;

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
                .filter(item -> item.getUUID().equals(uuid))
                .findFirst();
    }

    public void create(T item) throws Exception{
        save(item);
        data.add(item);
    }

    public void update(T item) {
        Optional<T> existingItem = getByUUID(item.getUUID());
        existingItem.ifPresent(t -> data.remove(t));

        save(item);
        data.add(item);

        postUpdate(item);
    }

    private void save(T item) {
        Gson gson = plugin.getGson();
        File file = new File(directory, item.getUUID().toString() + ".json");
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(item, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete item from the repository and the file system.
     */
    public void remove(T item) {
        Optional<T> existingItem = getByUUID(item.getUUID());
        if (existingItem.isPresent()) {
            data.remove(existingItem.get());
            File file = new File(directory, item.getUUID().toString() + ".json");
            if (file.exists()) {
                file.delete();
            }

            data.remove(item);
        }
    }

    public void removeByUUID(UUID uuid) {
        Optional<T> item = getByUUID(uuid);
        item.ifPresent(this::remove);
    }

    public abstract void postInit();
    public abstract void postUpdate(T item);

    public List<T> getData() {
        return data;
    }

    public MCHunt getPlugin() {
        return plugin;
    }
}
