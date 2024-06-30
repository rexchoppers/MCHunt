package com.rexchoppers.mchunt.managers;

import com.rexchoppers.mchunt.models.ArenaSetup;
import com.rexchoppers.mchunt.signs.ScrollingSign;
import org.bukkit.Location;

import java.util.*;

public class SignManager {
    private Map<UUID, List<ScrollingSign>> arenaSetupSigns = new HashMap<>();
    private Map<UUID, ScrollingSign> arenaSigns = new HashMap<>();

    public SignManager() {
    }

    public void addArenaSetupSign(UUID uuid, Location location) {
        List<ScrollingSign> currentArenaSetupSignsList = getArenaSetupSignsByArenaSetupUUID(uuid);

        ScrollingSign sign = new ScrollingSign(
                new String[] {
                        "§6§lMCHunt",
                        "§7§oRight click to",
                        "§7§oopen arena setup"
                },
                new String[] {
                        "§6§lMCHunt",
                        "§7§oRight click to",
                        "§7§oopen arena setup"
                },
                new int[] {1, 2},
                location
        );

        currentArenaSetupSignsList.add(sign);

        arenaSetupSigns.put(uuid, currentArenaSetupSignsList);
    }

    public Map<UUID, List<ScrollingSign>> getArenaSetupSigns() {
        return arenaSetupSigns;
    }

    public List<ScrollingSign> getArenaSetupSignsByArenaSetupUUID(UUID uuid) {
        if(arenaSetupSigns.containsKey(uuid)) {
            return arenaSetupSigns.get(uuid);
        }
        return new ArrayList<ScrollingSign>();
    }

}
