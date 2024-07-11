package me.robomonkey.versus;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Versus extends JavaPlugin {

    public static void log(String message){
        Bukkit.getServer().getLogger().info(message);
    }
    @Override
    public void onEnable() {
        // Plugin startup logic
        log("Versus has been enabled!");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        log("Versus has been enabled!");

    }
}
