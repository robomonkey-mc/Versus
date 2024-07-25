package me.robomonkey.versus.duel.eventlisteners;

import me.robomonkey.versus.Versus;
import me.robomonkey.versus.util.EffectUtil;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Arrays;

public class FireworkExplosionListener implements Listener {

    @EventHandler
    public void onExplosion(EntityDamageByEntityEvent event) {
        if(event.getDamager().getType() == EntityType.FIREWORK_ROCKET) {
            Firework firework = (Firework) event.getDamager();
            if(firework.hasMetadata(EffectUtil.fireworkNoDamageFlag)) event.setCancelled(true);
        }
    }
}
