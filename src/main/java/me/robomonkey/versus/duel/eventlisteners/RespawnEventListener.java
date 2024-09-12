package me.robomonkey.versus.duel.eventlisteners;

import me.robomonkey.versus.Versus;
import me.robomonkey.versus.duel.DuelManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class RespawnEventListener implements Listener {

    DuelManager duelManager = DuelManager.getInstance();

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (duelManager.hasStoredData(player)) {
            Bukkit.getScheduler().runTask(Versus.getInstance()
                    , () -> duelManager.restoreData(player, false));
        }
    }
}
