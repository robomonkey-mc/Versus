package me.robomonkey.versus.duel.menu;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.menu.SGMenu;
import me.robomonkey.versus.Versus;
import me.robomonkey.versus.settings.Settings;
import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class ListMenu<T> {

    private SGMenu menu;
    private List<T> items;
    private Runnable onExit;

    public ListMenu(String title, Consumer<T> onSelect, Runnable onExit, List<T> items, Function<T, SGButton> buttonizer) {
        int rows = Math.min(Math.min(6, items.size() / 9), 2);
        this.menu = Versus.spiGUI.create(title + " {currentPage} / {maxPage}", rows);
        this.items = items;
        this.onExit = onExit;
        menu.setAutomaticPaginationEnabled(true);
        generateToolbar();
        items.forEach(item -> {
            SGButton button = buttonizer.apply(item);
            button.withListener((inventoryClickEvent) -> onSelect.accept(item));
            menu.addButton(button);
        });
       
    }

    private void generateToolbar() {
        SGButton forwardButton = Settings.getButton("forward_button");
        SGButton backButton = Settings.getButton("back_button");
        SGButton exitMenuButton = Settings.getButton("exit_menu_button")
                .withListener(inventoryClickEvent -> onExit.run());
        SGButton placeholderButton = Settings.getButton("placeholder_button");
        menu.setToolbarBuilder((slot, page, type, menu) -> {
            switch (type) {
                case NEXT_BUTTON:
                    return forwardButton;
                case PREV_BUTTON:
                    return backButton;
                case CURRENT_BUTTON:
                    return exitMenuButton;
                default:
                    return placeholderButton;
            }
        });
    }

    public Inventory getInventory() {
        return menu.getInventory();
    }
}
