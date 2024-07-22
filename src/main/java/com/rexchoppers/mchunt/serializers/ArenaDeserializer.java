package com.rexchoppers.mchunt.serializers;

import com.google.gson.*;
import com.rexchoppers.mchunt.enums.ArenaStatus;
import com.rexchoppers.mchunt.models.Arena;
import org.bukkit.Location;

import java.lang.reflect.Type;
import java.util.UUID;

import static com.rexchoppers.mchunt.models.Arena.*;

public class ArenaDeserializer implements JsonDeserializer<Arena> {
    @Override
    public Arena deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        UUID uuid = context.deserialize(jsonObject.get("uuid"), UUID.class);
        String name = context.deserialize(jsonObject.get("name"), String.class);
        String regionId = context.deserialize(jsonObject.get("regionId"), String.class);
        UUID createdByUuid = context.deserialize(jsonObject.get("createdByUuid"), UUID.class);
        Location[] arenaSigns = context.deserialize(jsonObject.get("arenaSigns"), Location[].class);
        String[] arenaBlocks = context.deserialize(jsonObject.get("arenaBlocks"), String[].class);
        Location lobbySpawn = context.deserialize(jsonObject.get("lobbySpawn"), Location.class);
        Location[] hiderSpawns = context.deserialize(jsonObject.get("hiderSpawns"), Location[].class);
        Location[] seekerSpawns = context.deserialize(jsonObject.get("seekerSpawns"), Location[].class);
        Location afterGameSpawn = context.deserialize(jsonObject.get("afterGameSpawn"), Location.class);
        int minimumPlayers = getValueOrDefault(jsonObject, "minimumPlayers", 0);
        int maximumPlayers = getValueOrDefault(jsonObject, "maximumPlayers", 0);
        int seekerCount = getValueOrDefault(jsonObject, "seekerCount", DEFAULT_SEEKER_COUNT);
        int countdownBeforeStart = getValueOrDefault(jsonObject, "countdownBeforeStart", DEFAULT_COUNTDOWN_BEFORE_START);
        int countdownAfterEnd = getValueOrDefault(jsonObject, "countdownAfterEnd", DEFAULT_COUNTDOWN_AFTER_END);
        int respawnDelay = getValueOrDefault(jsonObject, "respawnDelay", 0);
        int seekerReleaseDelay = getValueOrDefault(jsonObject, "seekerReleaseDelay", DEFAULT_SEEKER_RELEASE_DELAY); // Assuming 20 is the default value
        int gameLength = getValueOrDefault(jsonObject, "gameLength", DEFAULT_GAME_LENGTH);
        Location locationBoundaryPoint1 = context.deserialize(jsonObject.get("locationBoundaryPoint1"), Location.class);
        Location locationBoundaryPoint2 = context.deserialize(jsonObject.get("locationBoundaryPoint2"), Location.class);

        Arena arena = new Arena(
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
                gameLength,
                locationBoundaryPoint1,
                locationBoundaryPoint2
        );

        if (arena.getStatus() == null) {
            arena.setStatus(ArenaStatus.WAITING);
        }

        return arena;
    }

    private int getValueOrDefault(JsonObject jsonObject, String memberName, int defaultValue) {
        JsonElement element = jsonObject.get(memberName);
        return element != null && !element.isJsonNull() ? element.getAsInt() : defaultValue;
    }
}
