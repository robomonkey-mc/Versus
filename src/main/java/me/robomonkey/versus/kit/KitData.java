package me.robomonkey.versus.kit;

import me.robomonkey.versus.Versus;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class KitData {
    private Versus plugin;
    private YamlConfiguration kitsData;
    private File kitsFile;

    public KitData(){
        plugin = Versus.getInstance();
        kitsFile = new File(plugin.getDataFolder(), "kits.yml");
        verifyDataFile();
        kitsData = YamlConfiguration.loadConfiguration(kitsFile);
    }

    public YamlConfiguration get(){
        return kitsData;
    }

    /**
     * Returns whether a player is in a passive or active state. Returns false if no data is available.
     */
    public Kit getKit(String kitName) {
        try {
            if(!kitsData.isConfigurationSection(kitName)) return null;
            ConfigurationSection kitSection = kitsData.getConfigurationSection(kitName);
            ConfigurationSection kitInventorySection = kitSection.getConfigurationSection("Kit");
            String name = kitSection.getString("Name");
            ItemStack[] items = getInventory(kitInventorySection);
            ItemStack displayItem = kitSection.getItemStack("DisplayItem");
            Kit loadedKit = new Kit(name, items, displayItem);
            return loadedKit;
        } catch (Exception e) {
            Versus.log("Failed retrieving kit "+kitName+". Check kits.yml for errors.");
            return null;
        }
    }

    public ArrayList<Kit> getAllKits() {
        ArrayList<Kit> loadedKits = new ArrayList<>();
        kitsData.getKeys(false).stream().forEach(key -> {
            Kit loadedKit = getKit(key);
            if(loadedKit != null) {
                loadedKits.add(loadedKit);
            }
        });
        return loadedKits;
    }

    /**
     * Saves a true or false passiveState for a player in the passive.yml data folder.
     */
    public void saveKit(Kit kit) {
        ConfigurationSection kitSection = kitsData.createSection(kit.getName());
        kitSection.set("Name", kit.getName());
        kitSection.set("DisplayItem", kit.getDisplayItem());
        saveInventory(kit.getItems(), kitSection, "Kit");
    }

    public void deleteKit(Kit kit) {
        if(!kitsData.isConfigurationSection(kit.getName())) return;
        ConfigurationSection kitSection = kitsData.getConfigurationSection(kit.getName());
        kitsData.set(kitSection.getCurrentPath(), null);
    }

    public void saveDataToFile(){
        Versus.log(kitsData.getKeys(true).toString());
        try {
            kitsData.save(kitsFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void verifyDataFile() {
        try {
            kitsFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveInventory(ItemStack[] items, ConfigurationSection kitSection, String inventorySection) {
        for(int index=0; index<items.length; index++) {
            ItemStack item = items[index];
            if(item!=null){
                kitSection.set(String.valueOf(index), item);
            }
        }
        kitSection.set(inventorySection, items);
    }

    /**
     * @param inventorySection The configurationSection that contains the desired ItemStacks.
     * @return will return empty array of itemstacks if the inventory is improperly formatted or does not exist in the config.
     */
    private ItemStack[] getInventory(ConfigurationSection inventorySection) throws Exception {
        try {
            ItemStack[] newInventory = new ItemStack[36];
            inventorySection.getKeys(false).forEach(key -> {
                int index = Integer.parseInt(key);
                ItemStack item = inventorySection.getItemStack(key);
                newInventory[index] = item;
            });
            return newInventory;
        } catch (Exception e) {
            throw new Exception("Kit item data missing or improperly formatted. Please check kits.yml for formatting.");
        }
    }

}
