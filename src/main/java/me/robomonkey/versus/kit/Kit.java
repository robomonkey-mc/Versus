package me.robomonkey.versus.kit;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Kit {
    private ItemStack displayItem;
    private ItemStack[] items;
    private String name;
    public static ItemStack defaultDisplayItem = new ItemStack(Material.DIAMOND_SWORD, 1);
    public static ItemStack[] defaultKit;

    public Kit(String name, ItemStack[] items) {
        this.name = name;
        this.items = items;
        this.displayItem = defaultDisplayItem;
    }

    public Kit(String name, ItemStack[] items, ItemStack displayItem) {
        this.name = name;
        this.items = items;
        this.displayItem = displayItem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemStack[] getItems() {
        return items;
    }

    public void setItems(ItemStack[] items) {
        this.items = items;
    }

    public Inventory getView() {
        Inventory view = Bukkit.createInventory(null, 54, getName());
        view.setContents(items);
        return view;
    }

    public ItemStack getDisplayItem() {
        return displayItem;
    }

    public void setDisplayItem(ItemStack displayItem) {
        this.displayItem = displayItem;
    }

    public boolean equals(Kit kit) {
        return kit != null && name.equals(kit.getName());
    }
}
