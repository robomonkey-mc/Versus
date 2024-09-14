package me.robomonkey.versus.settings;

import me.robomonkey.versus.Versus;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Lang {

    public static File file;
    public static YamlConfiguration config;

    static {
        load();
    }

    public static void load() {
        verify();
        file = new File(Versus.getInstance().getDataFolder(), "lang.yml");
        config = YamlConfiguration.loadConfiguration(file);
    }

    public static void verify() {
        Versus.getInstance().saveResource("lang.yml", false);
    }

    public static String get(String path) {
        if(file == null || config == null) load();
        return config.getString(path);
    }
}
