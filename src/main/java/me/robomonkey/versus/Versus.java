package me.robomonkey.versus;

import me.robomonkey.versus.arena.ArenaManager;
import me.robomonkey.versus.arena.command.RootArenaCommand;
import me.robomonkey.versus.duel.DuelManager;
import me.robomonkey.versus.duel.command.RootDuelCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class Versus extends JavaPlugin {

    private ArenaManager arenaManager;
    private DuelManager duelManager;
    private static Versus instance;
    public static String prefix = "[Versus]";

    public static void log(String message){
        Bukkit.getServer().getLogger().info(message);
    }

    public static void error(String message) {
        log("Error: "+message);
    }

    public static String color(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static Versus getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        log("Versus has been enabled!");
        instance = this;
        duelManager = DuelManager.getInstance();
        arenaManager = ArenaManager.getInstance();
        arenaManager.loadArenas();
        registerCommands();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        log("Versus has been disabled!");
        arenaManager.saveAllArenas();
    }

    public void registerCommands() {
        new RootArenaCommand();
        new RootDuelCommand();
    }

}
