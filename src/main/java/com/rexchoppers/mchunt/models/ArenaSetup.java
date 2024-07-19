package com.rexchoppers.mchunt.models;

import com.google.gson.annotations.Expose;
import com.rexchoppers.mchunt.enums.ArenaStatus;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ArenaSetup {
    @Expose
    private UUID uuid;

    @Expose
    private UUID playerUuid;

    @Expose
    private String arenaName;

    @Expose
    private ItemStack[] inventory;

    @Expose
    private Location[] arenaSigns;

    @Expose
    private String[] arenaBlocks;

    @Expose
    private Location lobbySpawn;

    @Expose
    private Location[] hiderSpawns;

    @Expose
    private Location[] seekerSpawns;

    @Expose
    private Location afterGameSpawn;

    @Expose
    private int minimumPlayers;

    @Expose
    private int maximumPlayers;

    @Expose
    private int seekerCount;

    @Expose
    private int countdownBeforeStart;

    @Expose
    private int countdownAfterEnd;

    @Expose
    private int respawnDelay = 5;

    @Expose
    private int seekerReleaseDelay;

    @Expose
    private Location locationBoundaryPoint1;

    @Expose
    private Location locationBoundaryPoint2;

    private Map<Location, BlockData> tmpBoundaryTracking = new HashMap<>();

    public ArenaSetup(
            UUID uuid,
            UUID playerUuid,
            ItemStack[] inventory
    ) {
        this.uuid = uuid;
        this.playerUuid = playerUuid;
        this.inventory = inventory;
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public ItemStack[] getInventory() {
        return inventory;
    }

    public Location getLocationBoundaryPoint1() {
        return locationBoundaryPoint1;
    }

    public void setLocationBoundaryPoint1(Location locationBoundaryPoint1) {
        this.locationBoundaryPoint1 = locationBoundaryPoint1;
    }

    public Location getLocationBoundaryPoint2() {
        return locationBoundaryPoint2;
    }

    public void setLocationBoundaryPoint2(Location locationBoundaryPoint2) {
        this.locationBoundaryPoint2 = locationBoundaryPoint2;
    }

    public Map<Location, BlockData> getTmpBoundaryTracking() {
        if (tmpBoundaryTracking == null) {
            tmpBoundaryTracking = new HashMap<>();
        }
        return tmpBoundaryTracking;
    }

    public void setTmpBoundaryTracking(Map<Location, BlockData> tmpBoundaryTracking) {
        this.tmpBoundaryTracking = tmpBoundaryTracking;
    }

    public void clearTmpBoundaryTracking() {
        this.tmpBoundaryTracking.clear();
    }

    public String getArenaName() {
        if (arenaName == null || arenaName.isEmpty()) {
            return "Arena";
        }

        return arenaName;
    }

    public void setArenaName(String arenaName) {
        this.arenaName = arenaName;
    }

    public Location[] getArenaSigns() {
        if (arenaSigns == null) {
            arenaSigns = new Location[0];
        }

        // Convert the array to a list allowing nulls, then filter out nulls
        List<Location> arenaSignsList = new ArrayList<>(Arrays.asList(arenaSigns));
        arenaSignsList.removeIf(Objects::isNull);
        arenaSigns = arenaSignsList.toArray(new Location[0]);

        return arenaSigns;
    }

    public void appendArenaSign(Location locationToAdd) {
            Location[] currentArenaSigns = this.getArenaSigns();
            Location[] newArray = new Location[this.arenaSigns.length + 1];

            // Copy the contents of the original array to the new array
            System.arraycopy(currentArenaSigns, 0, newArray, 0, currentArenaSigns.length);

            // Add the new element to the end of the new array
            newArray[currentArenaSigns.length] = locationToAdd;

            this.setArenaSigns(newArray);
    }

    public void removeArenaSign(Location locationToRemove) {
        Location[] currentArenaSigns = this.getArenaSigns();

        if (currentArenaSigns == null || currentArenaSigns.length == 0) {
            this.setArenaSigns(new Location[0]);
            return;
        }

        Location[] newArray = new Location[currentArenaSigns.length - 1];
        int j = 0;
        for (Location currentArenaSign : currentArenaSigns) {
            if (currentArenaSign == null) continue;

            if (currentArenaSign.equals(locationToRemove)) {
                continue;
            }

            newArray[j] = currentArenaSign;
            j++;
        }

        this.setArenaSigns(newArray);
    }

    public void setArenaSigns(Location[] arenaSigns) {
        this.arenaSigns = arenaSigns;
    }

    public String[] getArenaBlocks() {
        if (arenaBlocks == null) {
            arenaBlocks = new String[0];
        }

        return arenaBlocks;
    }

    public void setArenaBlocks(String[] arenaBlocks) {
        this.arenaBlocks = arenaBlocks;
    }

    public void appendArenaBlock(String blockToAdd) {
        String[] currentArenaBlocks = this.getArenaBlocks();
        String[] newArray = new String[this.arenaBlocks.length + 1];

        // Copy the contents of the original array to the new array
        System.arraycopy(currentArenaBlocks, 0, newArray, 0, currentArenaBlocks.length);

        // Add the new element to the end of the new array
        newArray[currentArenaBlocks.length] = blockToAdd;

        this.setArenaBlocks(newArray);
    }

    public void removeArenaBlock(String blockToRemove) {
        String[] currentArenaBlocks = this.getArenaBlocks();

        if (currentArenaBlocks == null || currentArenaBlocks.length == 0) {
            this.setArenaBlocks(new String[0]);
            return;
        }

        String[] newArray = new String[currentArenaBlocks.length - 1];
        int j = 0;
        for (String currentArenaBlock : currentArenaBlocks) {
            if (currentArenaBlock == null) continue;

            if (currentArenaBlock.equals(blockToRemove)) {
                continue;
            }

            newArray[j] = currentArenaBlock;
            j++;
        }

        this.setArenaBlocks(newArray);
    }

    public Location getLobbySpawn() {
        return lobbySpawn;
    }

    public void setLobbySpawn(Location lobbySpawn) {
        this.lobbySpawn = lobbySpawn;
    }

    public Location[] getHiderSpawns() {
        if (this.hiderSpawns == null) {
            this.hiderSpawns = new Location[0];
        }

        return hiderSpawns;
    }

    public void setHiderSpawns(Location[] hiderSpawns) {
        this.hiderSpawns = hiderSpawns;
    }

    public void appendHiderSpawn(Location locationToAdd) {
        Location[] currentHiderSpawns = this.getHiderSpawns();
        Location[] newArray = new Location[this.hiderSpawns.length + 1];

        // Copy the contents of the original array to the new array
        System.arraycopy(currentHiderSpawns, 0, newArray, 0, currentHiderSpawns.length);

        // Add the new element to the end of the new array
        newArray[currentHiderSpawns.length] = locationToAdd;

        this.setHiderSpawns(newArray);
    }

    public void removeHiderSpawn(Location locationToRemove) {
        Location[] currentHiderSpawns = this.getHiderSpawns();

        if (currentHiderSpawns == null || currentHiderSpawns.length == 0) {
            this.setHiderSpawns(new Location[0]);
            return;
        }

        Location[] newArray = new Location[currentHiderSpawns.length - 1];
        int j = 0;
        for (Location currentHiderSpawn : currentHiderSpawns) {
            if (currentHiderSpawn == null) continue;

            if (currentHiderSpawn.equals(locationToRemove)) {
                continue;
            }

            newArray[j] = currentHiderSpawn;
            j++;
        }

        this.setHiderSpawns(newArray);
    }

    public Location[] getSeekerSpawns() {
        if (this.seekerSpawns == null) {
            this.seekerSpawns = new Location[0];
        }

        return seekerSpawns;
    }

    public void setSeekerSpawns(Location[] seekerSpawns) {
        this.seekerSpawns = seekerSpawns;
    }

    public void appendSeekerSpawn(Location locationToAdd) {
        Location[] currentSeekerSpawns = this.getSeekerSpawns();
        Location[] newArray = new Location[this.seekerSpawns.length + 1];

        // Copy the contents of the original array to the new array
        System.arraycopy(currentSeekerSpawns, 0, newArray, 0, currentSeekerSpawns.length);

        // Add the new element to the end of the new array
        newArray[currentSeekerSpawns.length] = locationToAdd;

        this.setSeekerSpawns(newArray);
    }

    public void removeSeekerSpawn(Location locationToRemove) {
        Location[] currentSeekerSpawns = this.getSeekerSpawns();

        if (currentSeekerSpawns == null || currentSeekerSpawns.length == 0) {
            this.setSeekerSpawns(new Location[0]);
            return;
        }

        Location[] newArray = new Location[currentSeekerSpawns.length - 1];
        int j = 0;
        for (Location currentSeekerSpawn : currentSeekerSpawns) {
            if (currentSeekerSpawn == null) continue;

            if (currentSeekerSpawn.equals(locationToRemove)) {
                continue;
            }

            newArray[j] = currentSeekerSpawn;
            j++;
        }

        this.setSeekerSpawns(newArray);
    }

    public Location getAfterGameSpawn() {
        return afterGameSpawn;
    }

    public void setAfterGameSpawn(Location afterGameSpawn) {
        this.afterGameSpawn = afterGameSpawn;
    }

    public int getMinimumPlayers() {
        if (minimumPlayers < 2) {
            minimumPlayers = 2;
        }

        return minimumPlayers;
    }

    public void setMinimumPlayers(int minimumPlayers) {
        this.minimumPlayers = minimumPlayers;
    }

    public int getSeekerCount() {
        if (seekerCount < 1) {
            seekerCount = 1;
        }

        return seekerCount;
    }

    public void setSeekerCount(int seekerCount) {
        this.seekerCount = seekerCount;
    }

    public int getCountdownBeforeStart() {
        if (countdownBeforeStart < 1) {
            countdownBeforeStart = 10;
        }

        return countdownBeforeStart;
    }

    public void setCountdownBeforeStart(int countdownBeforeStart) {
        this.countdownBeforeStart = countdownBeforeStart;
    }

    public int getCountdownAfterEnd() {
        if (countdownAfterEnd < 1) {
            countdownAfterEnd = 10;
        }

        return countdownAfterEnd;
    }

    public void setCountdownAfterEnd(int countdownAfterEnd) {

        this.countdownAfterEnd = countdownAfterEnd;
    }

    public int getRespawnDelay() {
        if (respawnDelay < 1) {
            respawnDelay = 5;
        }

        return respawnDelay;
    }

    public void setRespawnDelay(int respawnDelay) {
        this.respawnDelay = respawnDelay;
    }

    public int getMaximumPlayers() {
        if (maximumPlayers < 2) {
            maximumPlayers = 2;
        }

        return maximumPlayers;
    }

    public void setMaximumPlayers(int maximumPlayers) {
        this.maximumPlayers = maximumPlayers;
    }

    public int getSeekerReleaseDelay() {
        if (seekerReleaseDelay == 0) {
            return 20;
        }

        return seekerReleaseDelay;
    }

    public void setSeekerReleaseDelay(int seekerReleaseDelay) {
        this.seekerReleaseDelay = seekerReleaseDelay;
    }
}
