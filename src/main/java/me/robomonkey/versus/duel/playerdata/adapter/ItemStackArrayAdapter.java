package me.robomonkey.versus.duel.playerdata.adapter;

import com.google.gson.*;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;

public class ItemStackArrayAdapter implements JsonSerializer<ItemStack[]>, JsonDeserializer<ItemStack[]> {

    @Override
    public ItemStack[] deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonArray jsonItems = jsonElement.getAsJsonArray();
        ItemStack[] items = new ItemStack[jsonItems.size()];
        for (int index = 0; index < jsonItems.size(); index++) {
            items[index] = context.deserialize(jsonItems.get(index), ItemStack.class);
        }
        return items;
    }

    @Override
    public JsonElement serialize(ItemStack[] itemStacks, Type type, JsonSerializationContext context) {
        JsonArray jsonArray = new JsonArray();
        for (ItemStack itemStack : itemStacks) {
            jsonArray.add(context.serialize(itemStack, ItemStack.class));
        }
        return jsonArray;
    }
}
