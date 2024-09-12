package me.robomonkey.versus.kit;

import dev.lone.itemsadder.api.CustomStack;
import me.robomonkey.versus.Versus;
import me.robomonkey.versus.dependency.Dependencies;
import me.robomonkey.versus.dependency.ItemsAdderUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class KitData {
    private final Versus plugin;
    private final YamlConfiguration kitsData;
    private final File kitsFile;

    public KitData() {
        plugin = Versus.getInstance();
        kitsFile = new File(plugin.getDataFolder(), "kits.yml");
        verifyDataFile();
        kitsData = YamlConfiguration.loadConfiguration(kitsFile);
    }

    public YamlConfiguration get() {
        return kitsData;
    }

    /**
     * Returns whether a player is in a passive or active state. Returns false if no data is available.
     */
    public Kit getKit(String kitName) {
        try {
            ConfigurationSection kitSection = kitsData.getConfigurationSection(kitName);
            if (kitSection == null) return null;
            ConfigurationSection kitInventorySection = kitSection.getConfigurationSection("Kit");
            if (kitInventorySection == null) return null;
            String name = kitSection.getString("Name");
            ItemStack[] items = getInventory(kitInventorySection);
            ItemStack displayItem = kitSection.getItemStack("DisplayItem");
            Kit loadedKit = new Kit(name, items, displayItem);
            return loadedKit;
        } catch (Exception e) {
            Versus.error("Failed retrieving kit " + kitName + ". Check kits.yml for errors.");
            return null;
        }
    }

    public ArrayList<Kit> getAllKits() {
        ArrayList<Kit> loadedKits = new ArrayList<>();
        kitsData.getKeys(false).stream().forEach(key -> {
            Kit loadedKit = getKit(key);
            if (loadedKit != null) {
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
        ConfigurationSection inventorySection = kitSection.createSection("Kit");
        saveInventory(kit.getItems(), inventorySection);
        saveDataToFile();
    }

    public void deleteKit(Kit kit) {
        if (!kitsData.isConfigurationSection(kit.getName())) return;
        ConfigurationSection kitSection = kitsData.getConfigurationSection(kit.getName());
        kitsData.set(kitSection.getCurrentPath(), null);
    }

    public void deleteKit(String kitName) {
        if (!kitsData.isConfigurationSection(kitName)) return;
        ConfigurationSection kitSection = kitsData.getConfigurationSection(kitName);
        kitsData.set(kitSection.getCurrentPath(), null);
    }

    public void saveDataToFile() {
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

    private void saveInventory(ItemStack[] items, ConfigurationSection inventorySection) {
        for (int index = 0; index < items.length; index++) {
            ItemStack item = items[index];
            if (item != null) {
                if (Dependencies.ITEMS_ADDER_ENABLED
                        && CustomStack.byItemStack(item) != null)
                    ItemsAdderUtil.save(item, index, inventorySection);
                else inventorySection.set(String.valueOf(index), item);
            }
        }
    }

    /**
     * @param inventorySection The configurationSection that contains the desired ItemStacks.
     * @return will return empty array of itemstacks if the inventory is improperly formatted or does not exist in the config.
     */
    private ItemStack[] getInventory(ConfigurationSection inventorySection) throws Exception {
        try {
            ItemStack[] newInventory = new ItemStack[41];
            inventorySection.getKeys(false).forEach(key -> {
                int index = Integer.parseInt(key);
                ItemStack item = getItemStack(inventorySection, key);
                newInventory[index] = item;
            });
            return newInventory;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Kit item data missing or improperly formatted. Please check kits.yml for formatting.");
        }
    }

    private ItemStack getItemStack(ConfigurationSection inventorySection, String relativeKey) {
        if (ItemsAdderUtil.isItem(inventorySection, relativeKey)) {
            return ItemsAdderUtil.tryLoad(inventorySection, relativeKey);
        } else {
            return inventorySection.getItemStack(relativeKey);
        }
    }

}
