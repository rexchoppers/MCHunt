package com.rexchoppers.mchunt.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.rexchoppers.mchunt.enums.ArenaStatus;

import java.lang.reflect.Type;

public class ArenaStatusSerializer implements JsonSerializer<ArenaStatus> {
    @Override
    public JsonElement serialize(ArenaStatus src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getKey());
    }
}
