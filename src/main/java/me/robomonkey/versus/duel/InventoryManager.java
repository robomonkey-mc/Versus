package me.robomonkey.versus.duel;

import com.google.gson.JsonObject;
import me.robomonkey.versus.Versus;
import me.robomonkey.versus.util.JsonUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InventoryManager {
    private Map<UUID, ItemStack[]> inventoryMap = new HashMap<>();
    private File dataFile;
    private JsonObject inventoryMapJSON;

    public InventoryManager() {
        dataFile = JsonUtil.getDataFile(Versus.getInstance(), "inventory.json");
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

    public void loadInventoryMap() {
        Bukkit.getScheduler().runTaskAsynchronously(Versus.getInstance(), () -> {
            try {
                inventoryMap = JsonUtil.readObject(inventoryMap.getClass(), dataFile);
            } catch (IOException e) {
                Versus.error("Failed to read dueling inventory data.");
            }
        });
    }

    public void saveInventoryMap() {
        Bukkit.getScheduler().runTaskAsynchronously(Versus.getInstance(), () -> {
            try {
                JsonUtil.writeObject(inventoryMap, dataFile, false);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }


}
