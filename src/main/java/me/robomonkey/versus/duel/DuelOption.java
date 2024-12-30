package me.robomonkey.versus.duel;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import me.robomonkey.versus.util.MessageUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Optional;

public enum DuelOption {
    CUSTOM_HEALTH(false),
    SELECT_KIT(false),
    USE_OWN_KIT(true),
    RANDOM_ITEMS(false),
    ITEM_BETTING(false),
    ECONOMY_BETTING(false),
    SELECT_ARENA(false);

    public boolean toggleable;

    DuelOption(boolean toggleable) {
        this.toggleable = toggleable;
    }
}
