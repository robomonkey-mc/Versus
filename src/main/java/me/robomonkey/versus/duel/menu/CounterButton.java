package me.robomonkey.versus.duel.menu;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CounterButton extends SGButton {
    /**
     * Creates an SGButton with the specified {@link ItemStack} as it's 'icon' in the inventory.
     *
     * @param icon The desired 'icon' for the SGButton.
     */

    private int value;
    private int min;
    private int max;
    private Runnable refreshFunction;

    public CounterButton(ItemBuilder icon, int startingValue, int maxInclusive, int minInclusive) {
        super(withExtraLore(icon,
                ""+startingValue)
                .get());
        this.value = startingValue;
        this.min = minInclusive;
        this.max = maxInclusive;

        ItemBuilder builder = new ItemBuilder(getIcon());
        List<String> replacementLore = builder.getLore();
        replacementLore.add(""+value);
        builder.lore(replacementLore);
        setIcon(builder.get());
    }

    private static ItemBuilder withExtraLore(ItemBuilder icon, String lore) {
        icon.getLore().add(lore);
        return icon;
    }

    public int getValue() {
        return this.value;
    }

    public void click(InventoryClickEvent event) {
        if (event.getClick() == ClickType.RIGHT) {
            if (value >= min) value = value - 1;
        } else if (event.getClick() == ClickType.LEFT) {
            if (value <= max) value = value + 1;
        };
        ItemBuilder builder = new ItemBuilder(getIcon());
        List<String> replacementLore = builder.getLore();
        replacementLore.set(replacementLore.size() - 1, ""+value);
        builder.lore(replacementLore);
        setIcon(builder.get());
    }



}
