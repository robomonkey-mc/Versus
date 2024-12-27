package me.robomonkey.versus.kit;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.buttons.SGButtonListener;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.menu.SGMenu;
import com.samjakob.spigui.toolbar.SGToolbarButtonType;
import me.robomonkey.versus.Versus;
import me.robomonkey.versus.util.MessageUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.stream.IntStream;

public class KitSelectionGUI {

    private KitManager kitManager = KitManager.getInstance();
    private Player viewer;
    private Kit selectedKit;
    private SGMenu mainMenu;

    private static SGButton EMPTY = new SGButton(new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).name(" ").build());

    public KitSelectionGUI(Player viewer, BiConsumer<Kit, Player> onSelect) {
        this.viewer = viewer;
        mainMenu = Versus.spiGUI.create("Kits (Page {currentPage}/{maxPage})", 2);
        mainMenu.setAutomaticPaginationEnabled(true);
        mainMenu.setToolbarBuilder((slot, page, type, sgMenu) -> {
            if (type == SGToolbarButtonType.CURRENT_BUTTON) {
                ItemStack confirmIcon = new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).amount(1).name("&a&lCONFIRM").lore("&7Confirm your choice of kit.").build();
                SGButton confirmButton = new SGButton(confirmIcon).withListener(listener -> {
                    if (selectedKit == null) selectedKit = KitManager.getInstance().getDefaultKit();
                    onSelect.accept(selectedKit, viewer);
                    viewer.closeInventory();
                });
                return confirmButton;
            } else if (type == SGToolbarButtonType.UNASSIGNED || sgMenu.getMaxPage() == 1) {
                return EMPTY;
            } else {
                return Versus.spiGUI.getDefaultToolbarBuilder().buildToolbarButton(slot, page, type, sgMenu);
            }
        });
        kitManager.verifyDefaultKit();
        selectedKit = kitManager.getDefaultKit();
        loadKits();
    }

    public void open() {
        viewer.openInventory(mainMenu.getInventory());
    }

    private void toggleSelection(Kit kit) {
        if (kit.equals(selectedKit)) {
            selectedKit = null;
        } else {
            selectedKit = kit;
        }
    }

    public void loadKits() {
        mainMenu.clearAllButStickiedSlots();
        kitManager.getAllKits().stream()
                .forEach(kit -> {
                    boolean selected = kit.equals(selectedKit);
                    mainMenu.addButton(getKitButton(kit, selected));
                });
    }

    private SGButton getKitButton(Kit kit, boolean selected) {
        SGButtonListener listener = (inventoryClickEvent) -> {
            if (inventoryClickEvent.getClick().isLeftClick()) {
                toggleSelection(kit);
                loadKits();
                mainMenu.refreshInventory(viewer);
            } else if (inventoryClickEvent.getClick().isRightClick()) {
                openViewingGUI(kit);
            }
        };
        ItemStack displayItem = kit.getDisplayItem();
        ItemStack kitIcon = new ItemBuilder(displayItem.getType())
                .name(MessageUtil.color("&p" + kit.getName()))
                .lore(selected ? "&a&lSELECTED" : "&7UNSELECTED",
                        "",
                        MessageUtil.color("&pRight-click&s to view."),
                        MessageUtil.color("&pLeft-Click &sto select &p" + kit.getName() + "&s!"))
                .build();
        SGButton kitButton = new SGButton(kitIcon);
        kitButton.withListener(listener);
        return kitButton;
    }

    public void openViewingGUI(Kit kit) {
        SGMenu viewingGUI = Versus.spiGUI.create("Viewing " + kit.getName(), 6);
        viewingGUI.setBlockDefaultInteractions(false);

        Versus.log("set to "+viewingGUI.areDefaultInteractionsBlocked());
        IntStream.range(45, 54).forEach(index -> {
            viewingGUI.setButton(index, EMPTY);
        });
        ItemStack exitIcon = new ItemBuilder(Material.BARRIER).amount(1)
                .name("&c&lSave and exit")
                .lore("&7Return to viewing all kits.").build();
        SGButton exitButton = new SGButton(exitIcon).withListener(inventoryClickEvent -> {
            inventoryClickEvent.setResult(Event.Result.DENY);
            updateKit(viewingGUI, kit);
            this.open();
        });
        viewingGUI.setButton(49, exitButton);
        for (int index = 0; index < kit.getItems().length; index++) {
            SGButton itemButton = new SGButton(kit.getItems()[index]);
            itemButton.withListener((event) -> {
                event.setResult(Event.Result.ALLOW);
            });
            viewingGUI.setButton(index, itemButton);
        }
        viewer.openInventory(viewingGUI.getInventory());
    }

    public void updateKit(SGMenu viewingMenu, Kit selectedKit) {
        viewingMenu.refreshInventory(viewer);
        ItemStack[] newItems = new ItemStack[41];
        ItemStack[] guiContents = viewingMenu.getInventory().getContents();
        for (int index = 0; index < 41; index++) {
            newItems[index] = guiContents[index];
        }
        if(!selectedKit.getItems().equals(newItems)) {
            Versus.log("Items are different.");
            Versus.log(Arrays.deepToString(selectedKit.getItems()));
            Versus.log(Arrays.deepToString(newItems));
            selectedKit.setItems(newItems);
            kitManager.update(selectedKit);
        }
    }
}
