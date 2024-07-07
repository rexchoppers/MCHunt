package com.rexchoppers.mchunt.models;

import com.google.gson.annotations.Expose;
import com.rexchoppers.mchunt.enums.ArenaStatus;
import org.bukkit.Location;

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
}
