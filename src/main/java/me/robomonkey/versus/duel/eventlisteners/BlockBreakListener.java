package me.robomonkey.versus.duel.eventlisteners;

import me.robomonkey.versus.duel.DuelManager;
import me.robomonkey.versus.settings.Setting;
import me.robomonkey.versus.settings.Settings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!Settings.is(Setting.ALLOW_BLOCK_DESTRUCTION)
                && DuelManager.getInstance().isDueling(player)) {
            event.setCancelled(true);
        }
    }
}
