package me.robomonkey.versus.data;

import com.google.common.collect.Lists;
import com.google.gson.reflect.TypeToken;
import me.robomonkey.versus.Versus;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class InventoryData {

    public List<Map<String, Object>> itemList;

    public InventoryData(ItemStack[] items) {
       itemList = Arrays.stream(items)
                .filter(item -> item !=null && item instanceof ItemStack)
                .map(ItemStack::serialize)
                .collect(Collectors.toList());
    }

    public ItemStack[] toSpigotItems() {
        Object[] itemObjects = itemList.stream().map(ItemStack::deserialize).toArray();
        ItemStack[] items = Arrays.copyOf(itemObjects, itemObjects.length, ItemStack[].class);
        return items;
    }

}
