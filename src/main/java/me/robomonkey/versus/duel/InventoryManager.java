package me.robomonkey.versus.duel;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import me.robomonkey.versus.Versus;
import me.robomonkey.versus.util.JsonUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class InventoryManager {
    private Map<UUID, ItemStack[]> inventoryMap;
    private File dataFile;

    public InventoryManager() {
        inventoryMap = new HashMap<>();
        dataFile = JsonUtil.getDataFile(Versus.getInstance(), "inventory.json");
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

    public void loadInventoryMap() {
        Bukkit.getScheduler().runTaskAsynchronously(Versus.getInstance(), () -> {
            try {
                Map<UUID, ItemStack[]> itemsMap = JsonUtil.readObject(inventoryMap.getClass(), dataFile);
                if(itemsMap != null) {
                    inventoryMap = itemsMap;
                }else{
                    Versus.log("Inventory map is null");
                }
            } catch (Exception e) {
                Versus.error("Failed to read dueling inventory data.");
            }
        });
    }

    public void saveInventoryMap() {
        Type mapType = new TypeToken<Map<UUID, ItemStack[]>>(){}.getType();
        
        Bukkit.getScheduler().runTaskAsynchronously(Versus.getInstance(), () -> {
            try {
                JsonUtil.writeObject(mapType, inventoryMap, dataFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }


}
