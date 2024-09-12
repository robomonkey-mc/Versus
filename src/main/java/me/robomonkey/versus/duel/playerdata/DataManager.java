package me.robomonkey.versus.duel.playerdata;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.robomonkey.versus.Versus;
import me.robomonkey.versus.arena.Arena;
import me.robomonkey.versus.util.JsonUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DataManager {
    private Map<UUID, PlayerData> dataMap;
    private File dataFile;
    private Gson inventoryGSON;

    public DataManager() {
        dataMap = new HashMap<>();
        dataFile = JsonUtil.getDataFile(Versus.getInstance(), "inventory.json");
        inventoryGSON = Versus.getGSON();
    }

    public void save(Player player, Arena currentArena) {
        PlayerData data = new PlayerData(player, currentArena);
        dataMap.put(player.getUniqueId(), data);
    }

    private void remove(UUID id) {
        dataMap.remove(id);
        saveDataMap();
    }

    /**
     * Retrieves a player's saved inventory AND removes that inventory from inventory.json storage.
     */
    public PlayerData extractData(Player player) {
        PlayerData data = get(player);
        remove(player.getUniqueId());
        return data;
    }

    /**
     * Returns an array of itemstacks saved in Versus json file if available, otherwise returns null.
     *
     * @return An array of itemstacks last associated with that player.
     */
    public PlayerData get(Player player) {
        return dataMap.get(player.getUniqueId());
    }

    public boolean contains(Player player) {
        return dataMap.containsKey(player.getUniqueId());
    }

    public void saveDataMap() {
        Type playerDataMapType = new TypeToken<Map<UUID, PlayerData>>() {
        }.getType();
        try {
            FileWriter writer = new FileWriter(dataFile);
            inventoryGSON.toJson(dataMap, playerDataMapType, writer);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void loadDataMap() {
        Versus.log("Loading playerdata from inventory.json");
        Bukkit.getScheduler().runTaskAsynchronously(Versus.getInstance(), () -> {
            try {
                Type playerDataMapType = new TypeToken<Map<UUID, PlayerData>>() {
                }.getType();
                FileReader reader = new FileReader(dataFile);
                Map<UUID, PlayerData> loadedMap = inventoryGSON.fromJson(reader, playerDataMapType);
                if (loadedMap != null) {
                    dataMap = loadedMap;
                    Versus.log("Playerdata loaded successfully from file.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Versus.error("Failed to read dueling playerdata.");
            }
        });
    }


}
