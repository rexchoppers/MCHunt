package com.rexchoppers.mchunt.serializers;

import com.google.gson.*;
import org.bukkit.Location;

import java.io.IOException;
import java.lang.reflect.Type;

public class LocationSerializer implements JsonSerializer<Location> {
    @Override
    public JsonElement serialize(Location src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src.serialize());
    }
}
