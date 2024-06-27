package com.rexchoppers.mchunt.serializers;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.bukkit.Location;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class LocationArrayDeserializer implements JsonDeserializer<Location[]> {
    @Override
    public Location[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray jsonArray = json.getAsJsonArray();
        Location[] locations = new Location[jsonArray.size()];

        int i = 0;
        for (JsonElement element : jsonArray) {
            if (!element.isJsonNull()) {
                // Deserialize each JsonObject into a Map, then use Location.deserialize to convert it to a Location object
                Map<String, Object> locationMap = context.deserialize(element, Map.class);
                locations[i] = Location.deserialize(locationMap);
            } else {
                locations[i] = null;  // Handle null values explicitly
            }
            i++;
        }
        return locations;
    }
}
