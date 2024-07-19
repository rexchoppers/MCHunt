package com.rexchoppers.mchunt.models;

import com.google.gson.annotations.Expose;
import com.rexchoppers.mchunt.enums.ArenaPlayerRole;
import com.rexchoppers.mchunt.enums.ArenaStatus;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Arena {
    @Expose
    private UUID uuid;

    @Expose
    private String name;

    private ArenaStatus status;

    @Expose
    private String regionId;

    @Expose
    private UUID createdByUuid;

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
    private int respawnDelay;

    @Expose
    private Location locationBoundaryPoint1;

    @Expose
    private Location locationBoundaryPoint2;
    
    // Game variables

    // Complete list of players
    private List<ArenaPlayer> players;

    public Arena(
            UUID uuid,
            String name,
            String regionId,
            UUID createdByUuid,
            Location[] arenaSigns,
            String[] arenaBlocks,
            Location lobbySpawn,
            Location[] hiderSpawns,
            Location[] seekerSpawns,
            Location afterGameSpawn,
            int minimumPlayers,
            int maximumPlayers,
            int seekerCount,
            int countdownBeforeStart,
            int countdownAfterEnd,
            int respawnDelay,
            Location locationBoundaryPoint1,
            Location locationBoundaryPoint2
    ) {
        this.uuid = uuid;
        this.name = name;
        this.status = ArenaStatus.WAITING;
        this.regionId = regionId;
        this.createdByUuid = createdByUuid;
        this.arenaSigns = arenaSigns;
        this.arenaBlocks = arenaBlocks;
        this.lobbySpawn = lobbySpawn;
        this.hiderSpawns = hiderSpawns;
        this.seekerSpawns = seekerSpawns;
        this.afterGameSpawn = afterGameSpawn;
        this.minimumPlayers = minimumPlayers;
        this.maximumPlayers = maximumPlayers;
        this.seekerCount = seekerCount;
        this.countdownBeforeStart = countdownBeforeStart;
        this.countdownAfterEnd = countdownAfterEnd;
        this.respawnDelay = respawnDelay;
        this.locationBoundaryPoint1 = locationBoundaryPoint1;
        this.locationBoundaryPoint2 = locationBoundaryPoint2;

        this.players = new ArrayList<>();
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArenaStatus getStatus() {
        return status;
    }

    public void setStatus(ArenaStatus status) {
        this.status = status;
    }

    public String getRegionId() {
        return regionId;
    }

    public UUID getCreatedByUuid() {
        return createdByUuid;
    }

    public Location[] getArenaSigns() {
        return arenaSigns;
    }

    public void setArenaSigns(Location[] arenaSigns) {
        this.arenaSigns = arenaSigns;
    }

    public String[] getArenaBlocks() {
        return arenaBlocks;
    }

    public void setArenaBlocks(String[] arenaBlocks) {
        this.arenaBlocks = arenaBlocks;
    }

    public Location getLobbySpawn() {
        return lobbySpawn;
    }

    public void setLobbySpawn(Location lobbySpawn) {
        this.lobbySpawn = lobbySpawn;
    }

    public Location[] getHiderSpawns() {
        return hiderSpawns;
    }

    public void setHiderSpawns(Location[] hiderSpawns) {
        this.hiderSpawns = hiderSpawns;
    }

    public Location[] getSeekerSpawns() {
        return seekerSpawns;
    }

    public void setSeekerSpawns(Location[] seekerSpawns) {
        this.seekerSpawns = seekerSpawns;
    }

    public Location getAfterGameSpawn() {
        return afterGameSpawn;
    }

    public void setAfterGameSpawn(Location afterGameSpawn) {
        this.afterGameSpawn = afterGameSpawn;
    }

    public int getMinimumPlayers() {
        return minimumPlayers;
    }

    public void setMinimumPlayers(int minimumPlayers) {
        this.minimumPlayers = minimumPlayers;
    }

    public int getMaximumPlayers() {
        return maximumPlayers;
    }

    public void setMaximumPlayers(int maximumPlayers) {
        this.maximumPlayers = maximumPlayers;
    }

    public int getSeekerCount() {
        return seekerCount;
    }

    public void setSeekerCount(int seekerCount) {
        this.seekerCount = seekerCount;
    }

    public int getCountdownBeforeStart() {
        return countdownBeforeStart;
    }

    public void setCountdownBeforeStart(int countdownBeforeStart) {
        this.countdownBeforeStart = countdownBeforeStart;
    }

    public int getCountdownAfterEnd() {
        return countdownAfterEnd;
    }

    public void setCountdownAfterEnd(int countdownAfterEnd) {
        this.countdownAfterEnd = countdownAfterEnd;
    }

    public int getRespawnDelay() {
        return respawnDelay;
    }

    public void setRespawnDelay(int respawnDelay) {
        this.respawnDelay = respawnDelay;
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

    public List<ArenaPlayer> getPlayers() {
        return players;
    }

    public void addPlayer(ArenaPlayer player) {
        players.add(player);
    }

    public boolean isPlayerInArena(Player player) {
        return players.stream().anyMatch(p -> p.getUUID().equals(player.getUniqueId()));
    }
}
