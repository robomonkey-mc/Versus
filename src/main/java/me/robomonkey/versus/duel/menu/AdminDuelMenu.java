package me.robomonkey.versus.duel.menu;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.buttons.SGButtonListener;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.menu.SGMenu;
import me.robomonkey.versus.Versus;
import me.robomonkey.versus.duel.DuelOption;
import me.robomonkey.versus.kit.Kit;
import me.robomonkey.versus.settings.Settings;
import me.robomonkey.versus.util.EffectUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class AdminDuelMenu {
    private SGMenu mainMenu;
    private static final SGButton EMPTY = new SGButton(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name(" ").build());
    private static final SGButton GREEN = new SGButton(new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).name("&a&oENABLED ").build());
    private static final SGButton RED = new SGButton(new ItemBuilder(Material.RED_STAINED_GLASS_PANE).name("&c&oDISABLED ").build());
    private Player viewer;
    private int nextEnabledIndex = 10;
    private int nextDisabledIndex = 14;
    public Set<DuelOption> selectedOptions = new HashSet<>();
    private Consumer onCompletion;

    public AdminDuelMenu(Player viewer, Consumer<List<DuelOption>> onCompletion) {
        this.viewer = viewer;
        this.onCompletion = onCompletion;
        mainMenu = Versus.spiGUI.create("Click an item to swap.", 6);
        populateGUI();
    }

    public void populateGUI() {
        populateButtons();
        populateBorder();
        SGButton explanationButton = new SGButton(new ItemBuilder(Material.BOOK)
                .flag(ItemFlag.HIDE_ENCHANTS)
                .name("&6Each item represents a different choice players can make before dueling.")
                .lore(
                    "&7Settings on the left will be included when a player types /duel.",
                    "&7Settings on the right will be excluded.",
                    "&7If the left side is empty, players won't see any options.",
                    "&7Click on an item to enabled/disable it!!"
                )
                .flag(ItemFlag.HIDE_ENCHANTS)
                .enchant(Enchantment.LUCK, 1)
                .build());
        SGButton finalizationButton = new SGButton(new ItemBuilder(Material.CRAFTING_TABLE)
                .name("&aSave")
                .flag(ItemFlag.HIDE_ENCHANTS)
                .enchant(Enchantment.LUCK, 1)
                .build())
                .withListener((event) -> {
                    viewer.closeInventory();
                    onCompletion.accept(selectedOptions);
                });
        SGButton resetButton = new SGButton(new ItemBuilder(Material.BARRIER)
                .name("&cReset")
                .flag(ItemFlag.HIDE_ENCHANTS)
                .enchant(Enchantment.LUCK, 1)
                .build())
                .withListener((event) -> reset());
        mainMenu.setButton(22, explanationButton);
        mainMenu.setButton(31, resetButton);
        mainMenu.setButton(40, finalizationButton);
    }

    private void populateBorder() {
        for (int slot = 0; slot < 9; slot++) {
            mainMenu.setButton(slot * 9, GREEN);
            mainMenu.setButton((slot * 9) + 8, RED);
        }
        for (int slot = 1; slot < 4; slot++) {
            mainMenu.setButton(slot, GREEN);
            mainMenu.setButton(slot + 45, GREEN);
        }
        for (int slot = 5; slot < 8; slot++) {
            mainMenu.setButton(slot, RED);
            mainMenu.setButton(slot + 45, RED);
        }
        for (int slot = 0; slot < 9; slot++) {
            mainMenu.setButton((slot * 9) + 4, EMPTY);
        }
    }

    private void reset() {
        mainMenu.clearAllButStickiedSlots();
        nextEnabledIndex = 10;
        nextDisabledIndex = 14;
        selectedOptions.clear();
        populateGUI();
        mainMenu.refreshInventory(viewer);
    }

    public void populateButtons() {
        Arrays.stream(DuelOption.values()).forEach((option) -> {
            SGButton button = Settings.getButton(option);
            SGButtonListener listener = (inventoryClickEvent) -> {
                if (inventoryClickEvent.getClick().isLeftClick()) {
                    EffectUtil.playSound(viewer, Sound.UI_BUTTON_CLICK);
                    switchSides(button, inventoryClickEvent.getSlot(), option);
                    mainMenu.refreshInventory(viewer);
                }
            };
            moveToDisabledSide(button.withListener(listener), -1);
        });
    }

    public void switchSides(SGButton button, int currSlot, DuelOption optionToToggle) {
        if (selectedOptions.contains(optionToToggle)) {
            EffectUtil.playSound(viewer, Sound.BLOCK_GLASS_BREAK);
            EffectUtil.playSound(viewer, Sound.BLOCK_BEACON_DEACTIVATE, 3f);
            moveToDisabledSide(button, currSlot);
            selectedOptions.add(optionToToggle);
        } else {
            EffectUtil.playSound(viewer, Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
            moveToEnabledSide(button, currSlot);
            selectedOptions.remove(optionToToggle);
        }
    }

    public void open() {
        viewer.openInventory(mainMenu.getInventory());
    }

    public void openViewingGUI(Kit kit) {
        SGMenu viewingGUI = Versus.spiGUI.create("Viewing " + kit.getName(), 6);
        IntStream.range(45, 54).forEach(index -> {
            viewingGUI.setButton(index, EMPTY);
        });
        ItemStack exitIcon = new ItemBuilder(Material.BARRIER).amount(1)
                .name("&c&lExit")
                .lore("&7Return to viewing all kits.").build();
        SGButton exitButton = new SGButton(exitIcon).withListener(inventoryClickEvent -> this.open());
        viewingGUI.setButton(49, exitButton);
        for (int index = 0; index < kit.getItems().length; index++) {
            SGButton itemButton = new SGButton(kit.getItems()[index]);
            viewingGUI.setButton(index, itemButton);
        }
        viewer.openInventory(viewingGUI.getInventory());
    }

    public void moveToEnabledSide(SGButton button, int currSlot) {
        if(currSlot >= 0) {
            mainMenu.removeButton(currSlot);
            decrementNextDisabledIndex();
            SGButton replacementButton = mainMenu.getButton(nextDisabledIndex);
            if (replacementButton != null) {
                mainMenu.removeButton(nextDisabledIndex);
                mainMenu.setButton(currSlot, replacementButton);
            }
        }
        mainMenu.setButton(nextEnabledIndex, button);
        incrementNextEnabledIndex();
    }

    public void moveToDisabledSide(SGButton button, int currSlot) {
        if(currSlot >= 0) {
            mainMenu.removeButton(currSlot);
            decrementNextEnabledIndex();
            SGButton replacementButton = mainMenu.getButton(nextEnabledIndex);
            if (replacementButton != null) {
                mainMenu.removeButton(nextEnabledIndex);
                mainMenu.setButton(currSlot, replacementButton);
            }
        }
        mainMenu.setButton(nextDisabledIndex, button);
        Versus.log("CurrIndex: "+nextDisabledIndex);
        incrementNextDisabledIndex();
        Versus.log("Later Index: "+nextDisabledIndex);
    }

    private void incrementNextDisabledIndex() {
        if(nextDisabledIndex % 9 == 7) {
            nextDisabledIndex += 7;
        } else {
            nextDisabledIndex += 1;
        }
    }

    private void decrementNextDisabledIndex() {
        if(nextDisabledIndex == 14) {
            return;
        }
        if(nextDisabledIndex % 9 == 5) {
            nextDisabledIndex -= 7;
        } else {
            nextDisabledIndex -= 1;
        }
    }

    private void incrementNextEnabledIndex() {
        if(nextEnabledIndex % 9 == 3) {
            nextEnabledIndex += 7;
        } else {
            nextEnabledIndex += 1;
        }
    }

    private void decrementNextEnabledIndex() {
        if(nextEnabledIndex == 10) {
            return;
        }
        if(nextEnabledIndex % 9 == 1) {
            nextEnabledIndex -= 7;
        } else {
            nextEnabledIndex -= 1;
        }
    }
}
