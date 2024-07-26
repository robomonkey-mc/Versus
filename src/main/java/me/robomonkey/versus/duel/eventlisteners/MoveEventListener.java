package me.robomonkey.versus.duel.eventlisteners;

import me.robomonkey.versus.Versus;
import me.robomonkey.versus.duel.DuelManager;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Transformation;

public class MoveEventListener implements Listener {

    DuelManager duelManager = DuelManager.getInstance();

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        Player player = e.getPlayer();
        if(duelManager.isDueling(player)) {
            duelManager.registerMoveEvent(player, e);
        }
    }
}
