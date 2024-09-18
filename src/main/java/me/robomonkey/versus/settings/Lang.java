package me.robomonkey.versus.settings;

import me.robomonkey.versus.Versus;
import me.robomonkey.versus.command.AbstractCommand;
import me.robomonkey.versus.util.MessageUtil;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

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
        verifyUpdated();
    }

    private static void verifyUpdated() {
        Set<String> keys = config.getDefaults().getKeys(true);
        for(String key: keys) {
            if (!config.contains(key)) {
                updateConfig();
                break;
            }
        }
    }

    private static void updateConfig() {
        Versus.log("Updating lang.yml file to include new options.");
        Map<String, Object> currentValues = config.getDefaults().getValues(true);
        Versus.getInstance().saveResource("lang.yml", true);
        file = new File(Versus.getInstance().getDataFolder(), "lang.yml");
        config = YamlConfiguration.loadConfiguration(file);
        currentValues.entrySet().stream().forEach(entry -> {
            config.set(entry.getKey(), entry.getValue());
        });
        saveToFile();
    }

    private static void saveToFile() {
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void verify() {
        Versus.getInstance().saveResource("lang.yml", false);
    }

    public static String get(String path) {
        return config.getString(path);
    }

    public static String get(Error error) {
        return config.getString(error.getPath());
    }

    public static String get(String key, Placeholder... placeholders) {
        String message = get(key);
        for (Placeholder placeholder : placeholders) {
            message = message.replaceAll(placeholder.holder, placeholder.replacement);
        }
        return MessageUtil.color(message);
    }

    public static String get(Error error, Placeholder... placeholders) {
        String message = config.getString(error.getPath());
        for (Placeholder placeholder : placeholders) {
            message = message.replaceAll(placeholder.holder, placeholder.replacement);
        }
        return MessageUtil.color(message);
    }

    public static YAMLCommand of(AbstractCommand command) {
        String key = command.getOriginalCommand();
        AbstractCommand currParent = command.getParent();
        while(currParent != null) {
            key = currParent.getCommand() + ".";
            currParent = currParent.getParent();
        }
        return new YAMLCommand(config.getConfigurationSection("commands."+key));
    }

    public static boolean has(AbstractCommand command) {
        String key = command.getCommand();
        AbstractCommand currParent = command.getParent();
        while(currParent != null) {
            key = currParent.getCommand() + ".";
            currParent = currParent.getParent();
        }
        return config.isConfigurationSection("commands."+key);
    }
}
