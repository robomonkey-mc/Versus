package me.robomonkey.versus.kit;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.buttons.SGButtonListener;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.menu.SGMenu;
import com.samjakob.spigui.toolbar.SGToolbarButtonType;
import me.robomonkey.versus.Versus;
import me.robomonkey.versus.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.BiConsumer;

public class KitManager {
    private KitData kitData;
    private static KitManager instance;

    public KitManager() {
        kitData = new KitData();
        verifyDefaultKit();
    }

    public static KitManager getInstance() {
        if(instance==null){
            instance = new KitManager();
        }
        return instance;
    }

    public List<Kit> getAllKits() {
        return kitData.getAllKits();
    }

    public static ItemStack[] getDefaultItems() {
        ItemStack[] kitItems = new ItemStack[41];
        kitItems[0] =  new ItemStack(Material.DIAMOND_AXE, 1);
        kitItems[1] =  new ItemStack(Material.CROSSBOW);
        kitItems[2] =  new ItemStack(Material.ARROW, 5);
        kitItems[36] =  new ItemStack(Material.DIAMOND_BOOTS);
        kitItems[37] =  new ItemStack(Material.DIAMOND_LEGGINGS);
        kitItems[38] =   new ItemStack(Material.DIAMOND_CHESTPLATE);
        kitItems[39] =  new ItemStack(Material.DIAMOND_HELMET);
        kitItems[40] =  new ItemStack(Material.SHIELD);
        return kitItems;
    }

    public void add(Kit kit) {
        kitData.saveKit(kit);
    }

    public void add(String name, ItemStack[] items, ItemStack displayItem) {
        Kit kit = new Kit(name, items, displayItem);
        kitData.saveKit(kit);
    }

    public void add(String name, ItemStack[] items) {
        Kit kit = new Kit(name, items);
        kitData.saveKit(kit);
    }

    public boolean contains(String name) {
        return kitData.getKit(name) != null;
    }

    public Kit getKit(String kitName) {
        Kit retrievedKit = kitData.getKit(kitName);
        return retrievedKit == null? kitData.getKit("Default"): retrievedKit;
    }

    public boolean isKit(String kitName) {
        Kit retrievedKit = kitData.getKit(kitName);
        return retrievedKit != null;
    }

    public void remove(String kit) {
        kitData.deleteKit(kit);
    }

    void verifyDefaultKit() {
        if(kitData.getKit("Default") == null) {
            add("Default", getDefaultItems(), new ItemStack(Material.DIAMOND_SWORD));
        }
    }

    Kit getDefaultKit() {
        return kitData.getKit("Default");
    }
}
