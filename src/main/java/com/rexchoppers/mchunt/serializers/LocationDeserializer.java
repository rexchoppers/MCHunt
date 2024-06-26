package com.rexchoppers.mchunt.serializers;

import com.google.gson.*;
import org.bukkit.Location;

import java.lang.reflect.Type;
import java.util.Map;

public class LocationDeserializer implements JsonDeserializer<Location> {
    @Override
    public Location deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        // Deserialize the JsonObject back to a Map
        Map<String, Object> locationMap = context.deserialize(jsonObject, Map.class);

        // Use Bukkit's Location.deserialize method to create a Location object
        return Location.deserialize(locationMap);
    }
}