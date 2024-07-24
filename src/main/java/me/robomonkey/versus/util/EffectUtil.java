package me.robomonkey.versus.util;

import me.robomonkey.versus.Versus;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EffectUtil {

    public static void playSound(Player p, Sound sound){

    }

    public static void spawnFireWorks(Location loc, int amount, Integer power) {
        Firework firework = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK_ROCKET);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();
        fireworkMeta.addEffect(FireworkEffect.builder().withColor(Color.ORANGE).flicker(true).trail(true).build());
        fireworkMeta.setPower(power);
        firework.setFireworkMeta(fireworkMeta);
        firework.detonate();
        for(int i=0; i<amount; i++) {
            Firework firework1 = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK_ROCKET);
            firework1.setFireworkMeta(fireworkMeta);
            firework1.detonate();
        }
    }

    public static void spawnFireWorksDelayed(Location loc, int amount, Integer power, Long delayTicks) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(Versus.getInstance(), () -> {
            spawnFireWorks(loc, amount, power);
        }, delayTicks);
    }

    public static void freezePlayer(Player player) {
        player.setWalkSpeed(0);
        player.setSprinting(false);
        player.setFlySpeed(0);
    }

    public static void unfreezePlayer(Player player){
        player.setInvulnerable(false);
        player.setWalkSpeed(0.2F);
        player.setFlySpeed(0.1F);
    }
}
