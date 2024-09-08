package me.robomonkey.versus.duel.eventlisteners;

import me.robomonkey.versus.settings.Setting;
import me.robomonkey.versus.settings.Settings;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

public class InteractEventListener implements Listener {

    @EventHandler
    public void onInteractEvent(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked().getType() != EntityType.PLAYER) return;
        if (!event.getPlayer().isSneaking() || event.getHand() != EquipmentSlot.HAND) return;
        if (!Settings.is(Setting.SHIFT_CLICK_REQUESTING_ENABLED)) return;
        Player requested = (Player) event.getRightClicked();
        Player requesting = event.getPlayer();
        requesting.performCommand("duel " + requested.getName());
    }
}
