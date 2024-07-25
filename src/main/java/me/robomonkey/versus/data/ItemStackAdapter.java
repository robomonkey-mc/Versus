package me.robomonkey.versus.data;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import me.robomonkey.versus.Versus;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Type;
import java.util.Map;

public class ItemStackAdapter implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {

    @Override
    public ItemStack deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        Type mapType = TypeToken.get(Map.class).getType();
        Map<String, Object> map = Versus.getGSON().fromJson(jsonElement, mapType);
        map.putIfAbsent("v", Bukkit.getUnsafe().getDataVersion());

        if(map.containsKey("meta")) {
            Map<String, Object> itemMeta = (Map<String, Object>) map.get("meta");
            ConfigurationSerializable deserializedMeta = context.deserialize(Versus.getGSON().toJsonTree(itemMeta), ConfigurationSerializable.class);
            map.remove("meta");
            ItemStack is = ItemStack.deserialize(map);
            is.setItemMeta((ItemMeta) deserializedMeta);
            return is;
        } else {
            return ItemStack.deserialize(map);
        }
    }

    @Override
    public JsonElement serialize(ItemStack itemStack, Type type, JsonSerializationContext context) {
        Map<String, Object> map = itemStack.serialize();
        map.putIfAbsent("v", Bukkit.getUnsafe().getDataVersion());
        if(itemStack.hasItemMeta()) {
            JsonElement itemMeta = context.serialize(itemStack.getItemMeta(), ConfigurationSerializable.class);
            map.put("meta", itemMeta.getAsJsonObject());
        }
        return Versus.getGSON().toJsonTree(map);
    }

}
