package com.rexchoppers.mchunt.serializers;

import com.google.gson.*;
import org.bukkit.Location;

import java.lang.reflect.Type;
import java.util.Map;

public class LocationSerializer implements JsonSerializer<Location> {
    @Override
    public JsonElement serialize(Location src, Type typeOfSrc, JsonSerializationContext context) {
        Map<String, Object> serializedData = src.serialize();
        JsonObject jsonLocation = new JsonObject();

        for (Map.Entry<String, Object> entry : serializedData.entrySet()) {
            jsonLocation.add(entry.getKey(), context.serialize(entry.getValue()));
        }

        return jsonLocation;
    }
}
