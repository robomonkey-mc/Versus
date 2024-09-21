package me.robomonkey.versus.settings;

import me.robomonkey.versus.Versus;
import me.robomonkey.versus.command.AbstractCommand;
import me.robomonkey.versus.util.MessageUtil;
import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

public class Lang {

    public static File file;
    public static YamlConfiguration config;

    public static void load() {
        file = new File(Versus.getInstance().getDataFolder(), "lang.yml");
        if(!file.exists()) {
            Versus.getInstance().saveResource("lang.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(file);
        verifyUpdated();
    }

    private static void verifyUpdated() {
        InputStream defaultLangStream = Versus.getInstance().getResource("lang.yml");
        File defaultLangFile = new File("lang.yml");
        try {
            FileUtils.copyInputStreamToFile(defaultLangStream, defaultLangFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        YamlConfiguration defaultYamlConfig = YamlConfiguration.loadConfiguration(defaultLangFile);
        Set<String> keys = defaultYamlConfig.getKeys(true);
        for(String key: keys) {
            if (!config.contains(key)) {
                updateConfig();
                break;
            }
        }
    }

    private static void updateConfig() {
        Versus.log("Updating lang.yml file to include new options.");
        Map<String, Object> currentValues = config.getValues(true);
        Versus.getInstance().saveResource("lang.yml", true);
        // file = new File(Versus.getInstance().getDataFolder(), "lang.yml");
        // config = YamlConfiguration.loadConfiguration(file);
        // currentValues.entrySet().stream().forEach(entry -> {
        //    config.set(entry.getKey(), entry.getValue());
        // });
        saveToFile();
    }

    private static void saveToFile() {
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        return new YAMLCommand(config.getConfigurationSection("commands."+getKey(command)));
    }

    private static String getKey(AbstractCommand command) {
        StringBuilder keyBuilder = new StringBuilder() ;
        keyBuilder.append(command.getOriginalCommand());
        AbstractCommand currParent = command.getParent();
        while(currParent != null) {
            keyBuilder.insert(0, currParent.getOriginalCommand() + ".");
            currParent = currParent.getParent();
        }
        return keyBuilder.toString();
    }

    public static boolean has(AbstractCommand command) {
        return config.isConfigurationSection("commands."+getKey(command));
    }
}
