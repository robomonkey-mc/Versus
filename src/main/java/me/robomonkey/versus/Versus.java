package me.robomonkey.versus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.samjakob.spigui.SpiGUI;
import me.robomonkey.versus.arena.ArenaManager;
import me.robomonkey.versus.arena.command.RootArenaCommand;
import me.robomonkey.versus.dependency.Dependencies;
import me.robomonkey.versus.duel.DuelManager;
import me.robomonkey.versus.duel.command.RootDuelCommand;
import me.robomonkey.versus.duel.command.RootSpectateCommand;
import me.robomonkey.versus.duel.playerdata.adapter.ConfigurationSerializableAdapter;
import me.robomonkey.versus.duel.playerdata.adapter.ItemStackAdapter;
import me.robomonkey.versus.duel.playerdata.adapter.ItemStackArrayAdapter;
import me.robomonkey.versus.settings.Setting;
import me.robomonkey.versus.settings.Settings;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class Versus extends JavaPlugin {

    private static Gson gson;
    private ArenaManager arenaManager;
    private DuelManager duelManager;
    private static Versus instance;
    public static String prefix = "[Versus]";
    public static int pluginId = 23279;
    public static SpiGUI spiGUI;

    public static void log(String message) {
        Bukkit.getServer().getLogger().info(prefix + " " + message);
    }

    public static void error(String message) {
        log("Error: " + message);
    }

    public static Gson getGSON() {
        if (gson == null) {
            GsonBuilder builder = new GsonBuilder()
                    .setPrettyPrinting()
                    .disableHtmlEscaping()
                    .registerTypeAdapter(ConfigurationSerializable.class, new ConfigurationSerializableAdapter())
                    .registerTypeAdapter(ItemStack.class, new ItemStackAdapter())
                    .registerTypeAdapter(ItemStack[].class, new ItemStackArrayAdapter());
            gson = builder.create();
        }
        return gson;
    }

    public static String color(String message) {
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
        Dependencies.refresh(getServer());
        registerMetrics();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        log("Versus has been disabled!");
        arenaManager.saveAllArenas();
    }

    public void registerCommands() {
        new RootArenaCommand();
        new RootVersusCommand();
        new RootDuelCommand();
        new RootSpectateCommand();
    }

    private void registerMetrics() {
        Metrics metrics = new Metrics(this, pluginId);
        List<Setting> noted = List.of(Setting.FIGHT_MUSIC_ENABLED, Setting.VICTORY_MUSIC_ENABLED, Setting.RETURN_WINNERS, Setting.RETURN_LOSERS, Setting.ANNOUNCE_DUELS, Setting.FIREWORKS_ENABLED, Setting.VICTORY_EFFECTS_ENABLED);
        // Collects config metrics to inform development priorities in the future. Opt out in bstats config.
        noted.stream()
                .forEach(setting -> metrics.addCustomChart(new SimplePie(setting.toString().toLowerCase(), () -> Settings.getStringVersion(setting))));
    }

}
