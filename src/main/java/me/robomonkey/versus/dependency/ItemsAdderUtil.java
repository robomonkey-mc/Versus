package me.robomonkey.versus.dependency;

import com.google.common.base.Preconditions;
import dev.lone.itemsadder.api.CustomStack;
import me.robomonkey.versus.Versus;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class ItemsAdderUtil {
    public static final String ID_KEY = "ItemsAdderID";
    public static final String ITEM_KEY = "VanillaItem";

    public static void save(ItemStack item, int index, ConfigurationSection inventorySection) {
        CustomStack stack = CustomStack.byItemStack(item);
        if (stack.byItemStack(item) == null) return;
        ConfigurationSection customItemSection = inventorySection.createSection(String.valueOf(index));
        customItemSection.set(ID_KEY, stack.getNamespacedID());
        customItemSection.set(ITEM_KEY, item);
    }

    public static ItemStack tryLoad(ConfigurationSection parentSection, String relativeKey) {
        Preconditions.checkArgument(isItem(parentSection, relativeKey), "Trying to load custom item that is not custom item.");
        ConfigurationSection itemSection = parentSection.getConfigurationSection(relativeKey);
        String namespacedID = itemSection.getString(ID_KEY);
        ItemStack vanillaItem = itemSection.getItemStack(ITEM_KEY);
        if (!Dependencies.ITEMS_ADDER_ENABLED) {
            return vanillaItem;
        }
        CustomStack stack = CustomStack.getInstance(namespacedID);
        if (stack == null) {
            Versus.error("Failed to load ItemsAdderItem with the ID '" + namespacedID + "'.");
            return vanillaItem;
        } else {
            Versus.error("Successfully loaded ItemsAdderItem with the ID '" + namespacedID + "'.");
            stack.getItemStack().setItemMeta(vanillaItem.getItemMeta());
            return stack.getItemStack();
        }
    }

    public static boolean isItem(ConfigurationSection parentSection, String itemKey) {
        return parentSection.isConfigurationSection(itemKey) && parentSection.getConfigurationSection(itemKey).contains(ID_KEY);
    }
}
