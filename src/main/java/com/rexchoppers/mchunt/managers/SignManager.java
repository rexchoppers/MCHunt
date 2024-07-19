package com.rexchoppers.mchunt.managers;

import com.rexchoppers.mchunt.models.Arena;
import com.rexchoppers.mchunt.models.ArenaSetup;
import com.rexchoppers.mchunt.signs.ScrollingSign;
import com.rexchoppers.mchunt.util.Format;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class SignManager {
    private Map<UUID, List<ScrollingSign>> arenaSetupSigns = new HashMap<>();
    private Map<UUID, List<ScrollingSign>> arenaSigns = new HashMap<>();

    public SignManager() {
    }

    public void initArenaSetupSigns(ArenaSetup arenaSetup) {
        Player player = Bukkit.getPlayer(arenaSetup.getPlayerUuid());
        Location[] arenaSigns = arenaSetup.getArenaSigns();
        List<ScrollingSign> currentArenaSetupSignsList = new ArrayList<>();

        Map<Integer, String> dynamicMessages = new HashMap<>();
        dynamicMessages.put(3, "%nArena currently being setup" + (player != null ? " by " + player.getName() : ""));

        for (Location location : arenaSigns) {
            currentArenaSetupSignsList.add(new ScrollingSign(
                    new String[] {
                            Format.processString("%TAG"),
                            Format.processString("%a" + arenaSetup.getArenaName()),
                            Format.processString("%eSetup Mode"),
                            "DYN",
                    },
                    dynamicMessages,
                    new int[] {3},
                    location
            ));
        }

        this.arenaSetupSigns.put(arenaSetup.getUUID(), currentArenaSetupSignsList);
    }

    public void initArenaSigns(Arena arena) {
        Location[] arenaSigns = arena.getArenaSigns();
        List<ScrollingSign> currentArenaSignsList = new ArrayList<>();

        Map<Integer, String> dynamicMessages = new HashMap<>();

        for (Location location : arenaSigns) {
            switch (arena.getStatus()) {
                case WAITING:
                    currentArenaSignsList.add(new ScrollingSign(
                            new String[] {
                                    Format.processString("%TAG"),
                                    Format.processString("%a" + arena.getName()),
                                    Format.processString(
                                            "%a" + Integer.toString(arena.getPlayers().size()) + "%n/%a" + Integer.toString(arena.getMaximumPlayers())),
                                    "DYN"
                            },
                            new HashMap<>() {{
                                put(3, "%nWaiting for players. Right click to join.");
                            }},
                            new int[] {3},
                            location
                    ));
                    break;
                case OFFLINE:
                    currentArenaSignsList.add(new ScrollingSign(
                            new String[] {
                                    Format.processString("%TAG"),
                                    Format.processString("%a" + arena.getName()),
                                    Format.processString("%eOffline"),
                            },
                            new HashMap<>(),
                            new int[] {},
                            location
                    ));
                    break;
                case COUNTDOWN_START:
                    currentArenaSignsList.add(new ScrollingSign(
                            new String[] {
                                    Format.processString("%TAG"),
                                    Format.processString("%a" + arena.getName()),
                                    Format.processString(
                                            "%a" + Integer.toString(arena.getPlayers().size()) + "%n/%a" + Integer.toString(arena.getMaximumPlayers())),
                                    Format.processString("%nStarts in %a" + arena.getStartCountdown().getCountdown() + "%ns")
                            },
                            new HashMap<>(),
                            new int[] {},
                            location
                    ));
                    break;
            }
        }

        this.arenaSigns.put(arena.getUUID(), currentArenaSignsList);
    }

    public Map<UUID, List<ScrollingSign>> getArenaSetupSigns() {
        return arenaSetupSigns;
    }

    public void removeArenaSetupScrollingSigns(UUID uuid) {
        arenaSetupSigns.remove(uuid);
    }

    public List<ScrollingSign> getArenaSetupSignsByArenaSetupUUID(UUID uuid) {
        if(arenaSetupSigns.containsKey(uuid)) {
            return arenaSetupSigns.get(uuid);
        }
        return new ArrayList<ScrollingSign>();
    }

    public List<ScrollingSign> getArenaSignsByUUID(UUID uuid) {
        if(arenaSigns.containsKey(uuid)) {
            return arenaSigns.get(uuid);
        }
        return new ArrayList<ScrollingSign>();
    }
}
