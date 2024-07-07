package com.rexchoppers.mchunt.managers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rexchoppers.mchunt.MCHunt;
import com.rexchoppers.mchunt.events.internal.ArenaSetupUpdatedEvent;
import com.rexchoppers.mchunt.exceptions.PlayerAlreadyInArenaSetupException;
import com.rexchoppers.mchunt.models.Arena;
import com.rexchoppers.mchunt.models.ArenaSetup;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ArenaSetupManager {
    private final MCHunt plugin;
    private final String filePath;

    private List<ArenaSetup> arenaSetups;

    public ArenaSetupManager(
            MCHunt plugin,
            String filePath
    ) {
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

        arenaSetups = load();

        // Load signs
        for (ArenaSetup arenaSetup : arenaSetups) {
            this.plugin.getSignManager().initArenaSetupSigns(arenaSetup);
        }
    }

    public Optional<ArenaSetup> getArenaSetupByUUID(UUID uuid) {
        return arenaSetups.stream()
                .filter(arenaSetup -> arenaSetup.getUUID().equals(uuid))
                .findFirst();
    }

    public void createArenaSetup(ArenaSetup arenaSetup) throws PlayerAlreadyInArenaSetupException {
        // Check if arena setup already exists
        if(getArenaSetupByPlayerUuid(arenaSetups, arenaSetup.getPlayerUuid()).isPresent()) {
            throw new PlayerAlreadyInArenaSetupException();
        }

        arenaSetups.add(arenaSetup);
        save(arenaSetups);
    }

    public void removeArenaSetup(UUID uuid) {
        Optional<ArenaSetup> existingArenaSetup = getArenaSetupByUUID(uuid);
        existingArenaSetup.ifPresent(setup -> arenaSetups.remove(setup));
        save(arenaSetups);
    }

    public void updateArenaSetup(ArenaSetup arenaSetup) {
        Optional<ArenaSetup> existingArenaSetup = getArenaSetupByPlayerUuid(arenaSetups, arenaSetup.getPlayerUuid());
        existingArenaSetup.ifPresent(setup -> arenaSetups.remove(setup));
        arenaSetups.add(arenaSetup);
        save(arenaSetups);

        this.plugin.getEventBusManager().publishEvent(new ArenaSetupUpdatedEvent(arenaSetup.getUUID()));
    }

    private void save(List<ArenaSetup> arenaSetups) {
        Gson gson = plugin.getGson();
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(arenaSetups, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Optional<ArenaSetup> getArenaSetupByPlayerUuid(List<ArenaSetup> arenaSetups, UUID playerUuid) {
        if (arenaSetups == null) {
            return Optional.empty();
        }

        return arenaSetups.stream()
                .filter(arenaSetup -> arenaSetup.getPlayerUuid().equals(playerUuid))
                .findFirst();
    }

    private List<ArenaSetup> load() {
        Gson gson = plugin.getGson();
        try (FileReader reader = new FileReader(filePath)) {
            Type type = new TypeToken<List<ArenaSetup>>() {}.getType();
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<ArenaSetup> getArenaSetups() {
        return arenaSetups;
    }
}
