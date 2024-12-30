package me.robomonkey.versus.duel.menu;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import me.robomonkey.versus.settings.Setting;
import me.robomonkey.versus.settings.Settings;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ToggleButton extends SGButton {
    /**
     * Creates an SGButton with the specified {@link ItemStack} as it's 'icon' in the inventory.
     *
     * @param icon The desired 'icon' for the SGButton.
     */

    private final String ENABLED_TEXT = Settings.getMessage(Setting.ENABLED_TEXT);
    private final String DISABLED_TEXT = Settings.getMessage(Setting.DISABLED_TEXT);
    private SGButton button;
    private boolean value;
    private Runnable refreshFunction;

    public ToggleButton(ItemBuilder icon, boolean startingValue,) {
        super(withExtraLore(icon,
                startingValue ? Settings.getMessage(Setting.ENABLED_TEXT) : Settings.getMessage(Setting.DISABLED_TEXT))
                .get());
        this.value = startingValue;
        this.button = button;

        String newToggleLore = value? ENABLED_TEXT: DISABLED_TEXT;
        ItemBuilder builder = new ItemBuilder(button.getIcon());
        List<String> replacementLore = builder.getLore();
        replacementLore.add(newToggleLore);
        builder.lore(replacementLore);
        button.setIcon(builder.get());
    }

    private static ItemBuilder withExtraLore(ItemBuilder icon, String lore) {
        icon.getLore().add(lore);
        return icon;
    }

    public boolean getValue() {
        return this.value;
    }

    public void toggle() {
        value = !value;
        String newToggleLore = value? ENABLED_TEXT: DISABLED_TEXT;
        ItemBuilder builder = new ItemBuilder(button.getIcon());
        List<String> replacementLore = builder.getLore();
        replacementLore.set(replacementLore.size() - 1, newToggleLore);
        builder.lore(replacementLore);
        button.setIcon(builder.get());
    }



}
