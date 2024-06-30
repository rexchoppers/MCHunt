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

    public void addArenaSetupSign(ArenaSetup arenaSetup) {
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
                new int[] {1, 2}
        );

        List<ScrollingSign> signs = arenaSetupSigns.getOrDefault(arenaSetup.getUUID(), new ArrayList<>());
        signs.add(sign);
        arenaSetupSigns.put(arenaSetup.getUUID(), signs);
    }

    public void updateArenaSetupSigns(ArenaSetup arenaSetup) {
        // arenaSetupSigns.put(arenaSetup, arenaSetupSigns.get(arenaSetup));
    }


}
