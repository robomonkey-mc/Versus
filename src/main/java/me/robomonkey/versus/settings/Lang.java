package me.robomonkey.versus.settings;

import me.robomonkey.versus.Versus;
import me.robomonkey.versus.command.AbstractCommand;
import me.robomonkey.versus.util.MessageUtil;
import org.apache.commons.io.IOUtils;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Set;

public class Lang {

    public static File file;
    public static YamlConfiguration config;
    private static final String FILE_PATH = "lang.yml";
    
    public static void load() {
        file = new File(Versus.getInstance().getDataFolder(), FILE_PATH);
        if(!file.exists()) {
            Versus.getInstance().saveResource(FILE_PATH, false);
        }
        config = YamlConfiguration.loadConfiguration(file);
        verifyUpdated();
    }

    private static void verifyUpdated() {
        InputStream defaultLangStream = Versus.getInstance().getResource(FILE_PATH);
        File defaultLangFile = new File(FILE_PATH);
        try (OutputStream output = new FileOutputStream(defaultLangFile)) {
            defaultLangStream.transferTo(output);
        } catch (IOException ioException) {
            ioException.printStackTrace();
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
        Versus.getInstance().saveResource(FILE_PATH, true);
        file = new File(Versus.getInstance().getDataFolder(), FILE_PATH);
        config = YamlConfiguration.loadConfiguration(new File(Versus.getInstance().getDataFolder(), FILE_PATH));
        currentValues.entrySet().stream().forEach(entry -> {
            String key = entry.getKey();
            Object value = entry.getValue();
            if(!config.isConfigurationSection(key) && value != null) {
                config.set(entry.getKey(), value);
            }
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
