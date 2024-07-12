package me.robomonkey.versus;

import me.robomonkey.versus.arena.ArenaManager;
import me.robomonkey.versus.duel.DuelManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class Versus extends JavaPlugin {

    private ArenaManager arenaManager;

    private DuelManager duelManager;

    private static Versus instance;

    public static void log(String message){
        Bukkit.getServer().getLogger().info(message);
    }

    public static Versus getInstance() { return instance; }

    @Override
    public void onEnable() {
        // Plugin startup logic
        log("Versus has been enabled! YAY");


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        log("Versus has been enabled!");
    }

}
