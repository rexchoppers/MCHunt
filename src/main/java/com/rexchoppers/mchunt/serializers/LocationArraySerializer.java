package com.rexchoppers.mchunt.serializers;

import com.google.gson.*;
import org.bukkit.Location;

import java.lang.reflect.Type;
import java.util.Map;

public class LocationArraySerializer implements JsonSerializer<Location[]> {
    @Override
    public JsonElement serialize(Location[] src, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray jsonArray = new JsonArray();
        for (Location location : src) {
            if (location != null) {
                Map<String, Object> serializedData = location.serialize();
                JsonObject jsonLocation = new JsonObject();
                for (Map.Entry<String, Object> entry : serializedData.entrySet()) {
                    JsonElement element = context.serialize(entry.getValue());
                    jsonLocation.add(entry.getKey(), element);
                }
                jsonArray.add(jsonLocation);
            } else {
                jsonArray.add(JsonNull.INSTANCE);  // Handle null elements in the array
            }
        }
        return jsonArray;
    }
}
