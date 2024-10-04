package me.robomonkey.versus.duel.eventlisteners;

import me.robomonkey.versus.duel.Duel;
import me.robomonkey.versus.duel.DuelManager;
import me.robomonkey.versus.duel.DuelState;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveEventListener implements Listener {

    DuelManager duelManager = DuelManager.getInstance();

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (duelManager.isDueling(player)) {
            Duel currentDuel = DuelManager.getInstance().getDuel(player);
            if (currentDuel.getState() == DuelState.COUNTDOWN && isMoving(event)) {
                event.setCancelled(true);
            }
        }
    }

    public boolean isMoving(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();
        if (to.getX() != from.getX() || to.getY() != from.getY() || to.getZ() != from.getZ()) return true;
        return false;
    }
}
