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

        Map<Integer, String> dynamicMessages = new HashMap<>();
        dynamicMessages.put(1, "This is a dynamic message that might be quite long and needs to scroll to fit on the sign.");
        dynamicMessages.put(2, "Another scrolling message that also might be too long for the sign display!");

        ScrollingSign sign = new ScrollingSign(
                new String[] {"Static Line 1", "Dynamic Line 1", "Dynamic Line 2", "Static Line 2"},
                dynamicMessages,
                new int[] {1, 2}, // Lines that have dynamic messages
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
