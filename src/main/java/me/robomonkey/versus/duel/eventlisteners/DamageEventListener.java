package me.robomonkey.versus.duel.eventlisteners;

import me.robomonkey.versus.duel.DuelManager;
import me.robomonkey.versus.settings.Setting;
import me.robomonkey.versus.settings.Settings;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageEventListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) return;
        if (!DuelManager.getInstance().isDueling((Player) event.getEntity())) return;
        if (!Settings.is(Setting.INSTANT_RESPAWN)) return;
        Player duelist = (Player) event.getEntity();
        //Simulates death if damage > duelist health.
        if (event.getDamage() > duelist.getHealth()) {
            event.setCancelled(true);
            DuelManager.getInstance().registerDuelistDeath(duelist, true);
        }
    }


}
