package com.rexchoppers.mchunt.serializers;

import com.google.gson.*;
import com.rexchoppers.mchunt.enums.ArenaStatus;
import org.bukkit.Location;

import java.lang.reflect.Type;
import java.util.Map;

public class LocationSerializer implements JsonSerializer<Location> {
    @Override
    public JsonElement serialize(Location src, Type typeOfSrc, JsonSerializationContext context) {
        Map<String, Object> serializedData = src.serialize();

        JsonObject jsonLocation = new JsonObject();

        for (Map.Entry<String, Object> entry : serializedData.entrySet()) {
            JsonElement element = context.serialize(entry.getValue());
            jsonLocation.add(entry.getKey(), element);
        }

        return jsonLocation;
    }
}
