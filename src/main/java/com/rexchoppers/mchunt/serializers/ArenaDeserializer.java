package com.rexchoppers.mchunt.serializers;

import com.google.gson.*;
import com.rexchoppers.mchunt.enums.ArenaStatus;
import com.rexchoppers.mchunt.models.Arena;
import org.bukkit.Location;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.UUID;

public class ArenaDeserializer implements JsonDeserializer<Arena> {
    @Override
    public Arena deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        UUID uuid = UUID.fromString(jsonObject.get("uuid").getAsString());
        String name = jsonObject.get("name").getAsString();
        ArenaStatus status = jsonObject.has("status") ? ArenaStatus.valueOf(jsonObject.get("status").getAsString()) : ArenaStatus.WAITING;
        String regionId = jsonObject.get("regionId").getAsString();
        UUID createdByUuid = UUID.fromString(jsonObject.get("createdByUuid").getAsString());

        Location[] arenaSigns = context.deserialize(jsonObject.get("arenaSigns"), Location[].class);
        String[] arenaBlocks = context.deserialize(jsonObject.get("arenaBlocks"), String[].class);
        Location lobbySpawn = context.deserialize(jsonObject.get("lobbySpawn"), Location.class);
        Location[] hiderSpawns = context.deserialize(jsonObject.get("hiderSpawns"), Location[].class);
        Location[] seekerSpawns = context.deserialize(jsonObject.get("seekerSpawns"), Location[].class);
        Location afterGameSpawn = context.deserialize(jsonObject.get("afterGameSpawn"), Location.class);
        int minimumPlayers = jsonObject.get("minimumPlayers").getAsInt();
        int maximumPlayers = jsonObject.get("maximumPlayers").getAsInt();
        int seekerCount = jsonObject.get("seekerCount").getAsInt();
        int countdownBeforeStart = jsonObject.get("countdownBeforeStart").getAsInt();
        int countdownAfterEnd = jsonObject.get("countdownAfterEnd").getAsInt();
        int respawnDelay = jsonObject.get("respawnDelay").getAsInt();
        int seekerReleaseDelay = jsonObject.get("seekerReleaseDelay").getAsInt();
        Location locationBoundaryPoint1 = context.deserialize(jsonObject.get("locationBoundaryPoint1"), Location.class);
        Location locationBoundaryPoint2 = context.deserialize(jsonObject.get("locationBoundaryPoint2"), Location.class);

        return new Arena(
                uuid,
                name,
                regionId,
                createdByUuid,
                arenaSigns,
                arenaBlocks,
                lobbySpawn,
                hiderSpawns,
                seekerSpawns,
                afterGameSpawn,
                minimumPlayers,
                maximumPlayers,
                seekerCount,
                countdownBeforeStart,
                countdownAfterEnd,
                respawnDelay,
                seekerReleaseDelay,
                locationBoundaryPoint1,
                locationBoundaryPoint2
        );
    }
}