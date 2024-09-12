package me.robomonkey.versus.duel.eventlisteners;

import me.robomonkey.versus.util.EffectUtil;
import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class FireworkExplosionListener implements Listener {

    @EventHandler
    public void onExplosion(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Firework) {
            Firework firework = (Firework) event.getDamager();
            if (firework.hasMetadata(EffectUtil.fireworkNoDamageFlag)) event.setCancelled(true);
        }
    }
}
