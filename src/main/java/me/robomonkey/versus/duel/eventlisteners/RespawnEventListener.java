package me.robomonkey.versus.duel.eventlisteners;

import me.robomonkey.versus.Versus;
import me.robomonkey.versus.duel.DuelManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class RespawnEventListener implements Listener {

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Versus.log(("Player respawned"));
        Player player = event.getPlayer();
        DuelManager duelManager = DuelManager.getInstance();
        if(duelManager.hasStoredInventory(player)) {
            Versus.log(("had inventory"));
            duelManager.restoreInventory(player);
        }
    }
}
