package me.robomonkey.versus.duel.menu;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.buttons.SGButtonListener;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.menu.SGMenu;
import me.robomonkey.versus.Versus;
import me.robomonkey.versus.arena.Arena;
import me.robomonkey.versus.arena.ArenaManager;
import me.robomonkey.versus.duel.DuelOption;
import me.robomonkey.versus.duel.options.DuelOptions;
import me.robomonkey.versus.settings.Settings;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

    public MainDuelMenu(Player viewer, Consumer<DuelOptions> onCompletion) {
        this.viewer = viewer;
        onConfirm = (inventoryClickEvent) -> onCompletion.accept(options);
        onCancel = (inventoryClickEvent) -> viewer.closeInventory();
        mainMenu = Versus.spiGUI.create("Duel", 6);
        populateGUI();
    }

    public void populateGUI() {
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

    public void populateDuelOptions() {
        Iterator<DuelOption> enabledOptions = Settings.getEnabledDuelOptions().iterator();
        Iterator<Integer> indices = IntStream.of(13,4,12,14,3,5,21,23).iterator();
        while(enabledOptions.hasNext()) {
            DuelOption option = enabledOptions.next();
            SGButton button = Settings.getButton(option);
            SGButtonListener listener = getBehavior(option);
            button.withListener(listener);
            int index = indices.next();
            mainMenu.setButton(index, button);
        }
    }

    public SGButtonListener getBehavior(DuelOption option) {
        switch (option) {
            case SELECT_ARENA:
                return getSelectArenaBehavior();
            case USE_OWN_KIT:

                return null;
            case SELECT_KIT:

                return null;
            case CUSTOM_HEALTH:

            case ECONOMY_BETTING:

            case ITEM_BETTING:

            case RANDOM_ITEMS:

        }
        return null;
    }

    public SGButtonListener getSelectArenaBehavior() {
        Runnable onExit = () -> viewer.openInventory(mainMenu.getInventory());
        String name = null;
        Consumer<Arena> onSelect = (arena) -> options.setArena(arena);
        Function<Arena, SGButton> buttonizer = arena -> {
            //TODO generate button
            SGButton arenaButton = null;
            return arenaButton;
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
}
