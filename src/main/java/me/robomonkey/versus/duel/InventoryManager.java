package me.robomonkey.versus.duel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import me.robomonkey.versus.Versus;
import me.robomonkey.versus.data.InventoryData;
import me.robomonkey.versus.data.ItemStackAdapter;
import me.robomonkey.versus.data.ItemStackArrayAdapter;
import me.robomonkey.versus.util.JsonUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class InventoryManager {
    private Map<UUID, ItemStack[]> inventoryMap;
    private File dataFile;
    private Gson inventoryGSON;
    public InventoryManager() {
        Versus.log("Constructing inventory manager class");
        inventoryMap = new HashMap<>();
        dataFile = JsonUtil.getDataFile(Versus.getInstance(), "inventory.json");
        inventoryGSON = getGson();

    }

    private Gson getGson() {
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        builder.registerTypeAdapter(ItemStack.class, new ItemStackAdapter());
        builder.registerTypeAdapter(ItemStack[].class, new ItemStackArrayAdapter());
        Gson gson = builder.create();
        return gson;
    }

    public void addInventory(Player player){
        UUID playerID = player.getUniqueId();
        ItemStack[] items = player.getInventory().getContents();
        inventoryMap.put(playerID, items);
        Versus.log("Saved inventory of "+player.getName());
        Versus.log(Arrays.deepToString(items));
    }

    public void removeInventory(UUID id) {
        inventoryMap.remove(id);
        saveInventoryMap();
    }

    /**
     * Retrieves a player's saved inventory AND removes that inventory from inventory.json storage.
     */
    public ItemStack[] retrieveInventory(Player player) {
        ItemStack[] items = getInventory(player);
        removeInventory(player.getUniqueId());
        return items;
    }

    /**
     * Returns an array of itemstacks saved in Versus json file if available, otherwise returns null.
     * @param player
     * @return An array of itemstacks last associated with that player.
     */
    public ItemStack[] getInventory(Player player) {
        return inventoryMap.get(player.getUniqueId());
    }

    public boolean contains(Player player) {
        return inventoryMap.containsKey(player.getUniqueId());
    }

    public void saveInventoryMap() {
        //TODO FIX THIS METHOD
        Type inventoryMapType = new TypeToken<Map<UUID, ItemStack[]>>(){}.getType();
        ItemStack[] testArray = inventoryMap.values().iterator().next();
        ItemStack testItem = testArray[0];
        /*inventoryMap.forEach((id, items) -> {
            InventoryData newData = new InventoryData(items);
            Versus.log("DATA "+newData.toString());
            JsonElement jsonInventory = gson.toJsonTree(newData);
            Versus.log("JSON "+gson.toJson(jsonInventory));
            serializedMap.put(id, jsonInventory);
        });
        Bukkit.getScheduler().runTaskAsynchronously(Versus.getInstance(), () -> {

        });*/

        try {
            FileWriter writer = new FileWriter(dataFile);
            Versus.log("Attempting to save individual ItemStack");
            Versus.log(inventoryGSON.toJson(testItem, ItemStack.class));
            Versus.log("Attempting to save individual Array");
            Versus.log(inventoryGSON.toJson(testArray, ItemStack[].class));
            Versus.log("Attempting to save entire ItemStack map");
            Versus.log(inventoryGSON.toJson(inventoryMap, inventoryMapType));
            inventoryGSON.toJson(inventoryMap, inventoryMapType, writer);
        } catch (Exception e) {
            Versus.log("Object writing failed!");
            e.printStackTrace();
        }
    }


    public void loadInventoryMap() {
        Versus.log("Attempting to load inventory map");
        Bukkit.getScheduler().runTaskAsynchronously(Versus.getInstance(), () -> {
            try {
                Type mapType = new TypeToken<Map<UUID, ItemStack[]>>(){}.getType();
                FileReader reader = new FileReader(dataFile);
                Map<UUID, ItemStack[]> loadedMap = inventoryGSON.fromJson(reader, mapType);
                if(loadedMap != null) {
                    inventoryMap = loadedMap;
                    Versus.log("Inventories loaded successfully from file.");
                } else {
                    Versus.log("Inventory map is null");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Versus.error("Failed to read dueling inventory data.");
            }
        });
    }


}
