package com.rexchoppers.mchunt.serializers;

import com.google.gson.*;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;
import java.util.Map;

public class ItemStackArrayDeserializer implements JsonDeserializer<ItemStack[]> {
    @Override
    public ItemStack[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray jsonArray = json.getAsJsonArray();
        ItemStack[] items = new ItemStack[jsonArray.size()];
        int index = 0;
        for (JsonElement element : jsonArray) {
            if (!element.isJsonNull()) {
                Map<String, Object> itemMap = context.deserialize(element, Map.class);
                items[index++] = ItemStack.deserialize(itemMap);
            } else {
                items[index++] = null;
            }
        }
        return items;
    }
}