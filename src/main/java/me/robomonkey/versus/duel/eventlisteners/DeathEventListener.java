package me.robomonkey.versus.duel.eventlisteners;

import me.robomonkey.versus.Versus;
import me.robomonkey.versus.duel.DuelManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathEventListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        if(DuelManager.getInstance().isDueling(player)) {
            Versus.log("Player is dueling");
            player.getNearbyEntities(3,3,0).stream()
                    .filter(entity -> entity.getType() == EntityType.ITEM)
                    .forEach(Entity::remove);
            DuelManager.getInstance().registerDuelistDeath(e);
        }
    }
}
