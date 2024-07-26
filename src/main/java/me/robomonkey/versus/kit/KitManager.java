package me.robomonkey.versus.kit;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.menu.SGMenu;
import me.robomonkey.versus.Versus;
import me.robomonkey.versus.arena.ArenaManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class KitManager {
    private KitData kitData;
    private SGMenu kitGUI;

    public KitManager() {
        kitData = new KitData();
    }

    public void openKitGUI(Player player) {
        verifyGUI();
        player.openInventory(kitGUI.getInventory());
    }

    public void add(Kit kit) {
        kitData.saveKit(kit);
        generateGUI();
    }

    public void add(String name, ItemStack[] items, ItemStack displayItem) {
        Kit kit = new Kit(name, items, displayItem);
        kit.setName(name);
        kit.setDisplayItem(displayItem);
        kit.setItems(items);
        kitData.saveKit(kit);
        generateGUI();
    }

    public void add(String name, ItemStack[] items) {
        Kit kit = new Kit(name, items);
        kitData.saveKit(kit);
        generateGUI();
    }

    public void remove(Kit kit) {
        kitData.deleteKit(kit);
        generateGUI();
    }

    private void verifyGUI() {
        if(kitGUI==null) {
            generateGUI();
        }
    }

    private void generateGUI() {
        kitGUI = Versus.spiGUI.create("&cKits", 6);
        kitData.getAllKits().stream()
                .forEach(kit -> kitGUI.addButton(getKitButton(kit)));
    }

    private SGButton getKitButton(Kit kit) {
        ItemStack displayItem = kit.getDisplayItem();
        ItemStack icon = new ItemBuilder(displayItem.getType()).name(kit.getName()).build();
        SGButton button = new SGButton(icon);
        button.withListener(event -> {
            Player playerWhoClicked = (Player) event.getWhoClicked();
            ArenaManager.getInstance().notifyKitSelection(kit, playerWhoClicked);
        });
        return button;
    }
}
