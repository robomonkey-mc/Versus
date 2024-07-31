package me.robomonkey.versus.duel.inventory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.robomonkey.versus.Versus;
import me.robomonkey.versus.util.JsonUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.*;

public class InventoryManager {
    private Map<UUID, ItemStack[]> inventoryMap;
    private File dataFile;
    private Gson inventoryGSON;
    public InventoryManager() {
        inventoryMap = new HashMap<>();
        dataFile = JsonUtil.getDataFile(Versus.getInstance(), "inventory.json");
        inventoryGSON = Versus.getGSON();

    }

    public void addInventory(Player player){
        UUID playerID = player.getUniqueId();
        ItemStack[] items = player.getInventory().getContents();
        inventoryMap.put(playerID, items);
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
        try {
            FileWriter writer = new FileWriter(dataFile);
            inventoryGSON.toJson(inventoryMap, inventoryMapType, writer);
            writer.close();
        } catch (Exception e) {
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
                }
            } catch (Exception e) {
                e.printStackTrace();
                Versus.error("Failed to read dueling inventory data.");
            }
        });
    }


}
