package me.robomonkey.versus.duel.menu;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.buttons.SGButtonListener;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.menu.SGMenu;
import me.robomonkey.versus.Versus;
import me.robomonkey.versus.arena.Arena;
import me.robomonkey.versus.arena.ArenaManager;
import me.robomonkey.versus.duel.DuelOption;
import me.robomonkey.versus.duel.options.bets.DuelOptions;
import me.robomonkey.versus.kit.Kit;
import me.robomonkey.versus.kit.KitManager;
import me.robomonkey.versus.settings.Settings;
import me.robomonkey.versus.util.MessageUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.w3c.dom.css.Counter;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;

public class MainDuelMenu {
    //TODO remember to cache all menu buttons in lang.yml
    private SGMenu mainMenu;
    private Player viewer;
    private DuelOptions options = DuelOptions.DEFAULT;
    private Consumer<List<DuelOption>> onCompletion;
    private final IntStream acceptCols = IntStream.of(0,3);
    private final IntStream denyCols = IntStream.of(6,9);
    private final IntStream buttonCols = IntStream.of(3,6);
    private SGButtonListener onConfirm;
    private SGButtonListener onCancel;

    private enum Type {
        COUNTER,
        TOGGLE,
        LIST,
        COMPLEX;
    }

    private static final Map<DuelOption, Type> types = Map.ofEntries(
            Map.entry(DuelOption.CUSTOM_HEALTH,Type.COUNTER),
            Map.entry(DuelOption.ECONOMY_BETTING,Type.COMPLEX),
            Map.entry(DuelOption.ITEM_BETTING,Type.COMPLEX),
            Map.entry(DuelOption.SELECT_ARENA,Type.LIST),
            Map.entry(DuelOption.SELECT_KIT,Type.LIST),
            Map.entry(DuelOption.USE_OWN_KIT,Type.TOGGLE)
    );

    public MainDuelMenu(Player viewer, Consumer<DuelOptions> onCompletion) {
        this.viewer = viewer;
        onConfirm = (inventoryClickEvent) -> onCompletion.accept(options);
        onCancel = (inventoryClickEvent) -> viewer.closeInventory();
        mainMenu = Versus.spiGUI.create("Duel", 6);
        populateGUI();
    }

    private void populateGUI() {
        populateDuelOptions();
        SGButton confirmButton = Settings.getButton("confirm_button").withListener(onConfirm);
        SGButton cancelButton = Settings.getButton("cancel_button").withListener(onCancel);
        populateWith(acceptCols, confirmButton);
        populateWith(denyCols, cancelButton);
    }

    private void populateWith(IntStream rowRange, SGButton button) {
       rowRange.forEach((row) -> {
           IntStream.of(0,9).forEach((col) -> {
               mainMenu.setButton(indexOf(row, col), button);
           });
       });
    }

    private int indexOf(int row, int column) {
        return (row * 9) + column;
    }

    private void populateDuelOptions() {
        Iterator<DuelOption> enabledOptions = Settings.getEnabledDuelOptions().iterator();
        Iterator<Integer> indices = IntStream.of(13,4,12,14,3,5,21,23).iterator();
        while(enabledOptions.hasNext()) {
            DuelOption option = enabledOptions.next();
            int index = indices.next();
            SGButton button;
            switch (types.get(option)) {
                case TOGGLE:
                    Boolean startingValue = (Boolean) Settings.getDefaultValue(option);
                    button = new ToggleButton(new ItemBuilder(Settings.getButton(option).getIcon()), startingValue);
                    break;
                case COUNTER:
                    Integer startingCount = (Integer) Settings.getDefaultValue(option);
                    button = new CounterButton(new ItemBuilder(Settings.getButton(option).getIcon()), startingCount, 20, 0);
                    break;
                default:
                    button = Settings.getButton(option);
                    break;
            }
            SGButtonListener listener = getBehavior(button, option);
            button.withListener(listener);
            mainMenu.setButton(index, button);
        }
    }

    /*TODO Note: This todo actually has to do with config.yml, but remember to decide the appearance of the title. (Perhaps
    the title could say PlayerOne vs. PlayerTwo)*/
    private SGButtonListener getBehavior(SGButton button, DuelOption option) {
        switch (option) {
            case SELECT_ARENA:
                return getSelectArenaBehavior();
            case USE_OWN_KIT:
                return (inventoryClickEvent) -> {
                    ToggleButton toggleable = (ToggleButton) button;
                    toggleable.toggle();
                    options.setUseInventory(toggleable.getValue());
                };
            case SELECT_KIT:
                return getSelectKitBehavior();
            case CUSTOM_HEALTH:
                return (inventoryClickEvent) -> {
                    CounterButton cbutton = (CounterButton) button;
                    cbutton.click(inventoryClickEvent);
                    options.setMaxHealth(cbutton.getValue());
                };
            case ECONOMY_BETTING:
                return (inventoryClickEvent) -> {};
            case ITEM_BETTING:
                return (inventoryClickEvent) -> {};
        }
        return null;
    }

    private SGButtonListener getSelectArenaBehavior() {
        final Runnable onExit = () -> viewer.openInventory(mainMenu.getInventory());
        final String name = "Choose an arena.";
        final Consumer<Arena> onSelect = (arena) -> options.setArena(arena);
        final Function<Arena, SGButton> buttonizer = arena -> {
            //TODO make this language compatible
            ItemStack arenaIcon = new ItemBuilder(arena.getIcon())
                    .name(MessageUtil.color("&f")+arena.getName())
                    .lore(MessageUtil.color(arena.isAvailable()? "&aAvailable": "&cOccupied"))
                    .build();
            return new SGButton(arenaIcon);
        };
        ListMenu<Arena> arenaListMenu = new ListMenu<>(
            name,
            onSelect,
            onExit,
            ArenaManager.getInstance().getAllArenas(),
            buttonizer
        );
        return (inventoryClickEvent) -> viewer.openInventory(arenaListMenu.getInventory());
    }

    private SGButtonListener getSelectKitBehavior() {
        final Runnable onExit = () -> viewer.openInventory(mainMenu.getInventory());
        final String name = "Choose a kit.";
        final Consumer<Kit> onSelect = (kit) -> options.setKit(kit.getItems());
        final Function<Kit, SGButton> buttonizer = kit -> {
            ItemStack kitIcon = new ItemBuilder(kit.getDisplayItem())
                    .name(MessageUtil.color("&f")+kit.getName())
                    .build();
            return new SGButton(kitIcon);
        };
        final ListMenu<Kit> kitListMenu = new ListMenu<>(
            name,
            onSelect,
            onExit,
            KitManager.getInstance().getAllKits(),
            buttonizer
        );
        return (inventoryClickEvent) -> viewer.openInventory(kitListMenu.getInventory());
    }

    public void open() {
        viewer.openInventory(mainMenu.getInventory());
    }
}
