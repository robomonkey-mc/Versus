package me.robomonkey.versus.duel.eventlisteners;

import me.robomonkey.versus.duel.DuelManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathEventListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        //Registers death if player is dueling.
        if (DuelManager.getInstance().isDueling(player)) {
            e.getDrops().clear();
            e.setDeathMessage("");
            DuelManager.getInstance().registerDuelistDeath(player, false);
        }
    }


}
