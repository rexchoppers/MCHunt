package com.rexchoppers.mchunt.serializers;

import com.google.gson.*;
import com.rexchoppers.mchunt.enums.ArenaStatus;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;

public class ItemStackArraySerializer implements JsonSerializer<ItemStack[]> {
    @Override
    public JsonElement serialize(ItemStack[] src, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray jsonArray = new JsonArray();
        for (ItemStack itemStack : src) {
            if (itemStack != null) {
                JsonElement jsonElement = context.serialize(itemStack.serialize());
                jsonArray.add(jsonElement);
            } else {
                jsonArray.add(JsonNull.INSTANCE);
            }
        }
        return jsonArray;
    }
}
