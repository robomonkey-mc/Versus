package me.robomonkey.versus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.samjakob.spigui.SpiGUI;
import me.robomonkey.versus.arena.ArenaManager;
import me.robomonkey.versus.arena.command.RootArenaCommand;
import me.robomonkey.versus.duel.command.RootSpectateCommand;
import me.robomonkey.versus.duel.playerdata.adapter.ConfigurationSerializableAdapter;
import me.robomonkey.versus.duel.playerdata.adapter.ItemStackAdapter;
import me.robomonkey.versus.duel.playerdata.adapter.ItemStackArrayAdapter;
import me.robomonkey.versus.duel.DuelManager;
import me.robomonkey.versus.duel.command.RootDuelCommand;
import me.robomonkey.versus.settings.Placeholder;
import me.robomonkey.versus.settings.Setting;
import me.robomonkey.versus.settings.Settings;
import me.robomonkey.versus.util.MessageUtil;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class Versus extends JavaPlugin {

    private static Gson gson;
    private ArenaManager arenaManager;
    private DuelManager duelManager;
    private static Versus instance;
    public static String prefix = "[Versus]";
    public static SpiGUI spiGUI;

    public static void log(String message){
        Bukkit.getServer().getLogger().info(prefix+" "+message);
    }

    public static void error(String message) {
        log("Error: "+message);
    }

    public static Gson getGSON() {
        if(gson == null) {
            GsonBuilder builder = new GsonBuilder()
                    .setPrettyPrinting()
                    .disableHtmlEscaping()
                    .setObjectToNumberStrategy(JsonReader::nextInt)
                    .registerTypeAdapter(ConfigurationSerializable.class, new ConfigurationSerializableAdapter())
                    .registerTypeAdapter(ItemStack.class, new ItemStackAdapter())
                    .registerTypeAdapter(ItemStack[].class, new ItemStackArrayAdapter());
            gson = builder.create();
        }
        return gson;
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
        Settings.getInstance().registerConfig();
        spiGUI = new SpiGUI(this);
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
        new RootSpectateCommand();
    }

}
