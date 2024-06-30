package com.rexchoppers.mchunt.managers;

import com.rexchoppers.mchunt.models.ArenaSetup;
import com.rexchoppers.mchunt.signs.ScrollingSign;
import com.rexchoppers.mchunt.util.Format;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class SignManager {
    private Map<UUID, List<ScrollingSign>> arenaSetupSigns = new HashMap<>();
    private Map<UUID, ScrollingSign> arenaSigns = new HashMap<>();

    public SignManager() {
    }

    public void addArenaSetupSign(ArenaSetup arenaSetup, Location location) {
        List<ScrollingSign> currentArenaSetupSignsList = getArenaSetupSignsByArenaSetupUUID(arenaSetup.getUUID());

        Player player = Bukkit.getPlayer(arenaSetup.getPlayerUuid());

        Map<Integer, String> dynamicMessages = new HashMap<>();
        dynamicMessages.put(3, Format.processString("%nArena currently being setup" + (player != null ? " by %a" + player.getName() : "")));


        ScrollingSign sign = new ScrollingSign(
                new String[] {
                        Format.processString("%TAG"),
                        Format.processString("%eSetup Mode"),
                        Format.processString("%a" + arenaSetup.getArenaName()),
                        "DYN",
                },
                dynamicMessages,
                new int[] {3},
                location
        );

        currentArenaSetupSignsList.add(sign);

        arenaSetupSigns.put(arenaSetup.getUUID(), currentArenaSetupSignsList);
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
