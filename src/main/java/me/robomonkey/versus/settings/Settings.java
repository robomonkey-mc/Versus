package me.robomonkey.versus.settings;

import me.robomonkey.versus.Versus;
import me.robomonkey.versus.dependency.Dependencies;
import me.robomonkey.versus.duel.ReturnOption;
import me.robomonkey.versus.util.MessageUtil;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

public class Settings {

    Versus plugin = Versus.getInstance();
    FileConfiguration config;
    final static String configVersion = "1.0";
    private static Settings instance;
    private Map<Setting, Object> unsavedSettingChanges = new HashMap<>();

    public static Map<String, Color> colorMap = Map.ofEntries(
            Map.entry("aqua", Color.AQUA),
            Map.entry("black", Color.BLACK),
            Map.entry("blue", Color.BLUE),
            Map.entry("gray", Color.GRAY),
            Map.entry("green", Color.GREEN),
            Map.entry("lime", Color.LIME),
            Map.entry("orange", Color.ORANGE),
            Map.entry("purple", Color.PURPLE),
            Map.entry("red", Color.RED),
            Map.entry("silver", Color.SILVER),
            Map.entry("teal", Color.TEAL),
            Map.entry("white", Color.WHITE),
            Map.entry("yellow", Color.YELLOW)
    );

    public static List<String> musicOptions = List.of("creative", "credits", "disc_5", "disc_11", "disc_13", "disc_blocks", "disc_cat", "disc_chirp", "disc_creator", "disc_creator_music_box", "disc_far", "disc_mall", "disc_mellohi", "disc_otherside", "disc_pigstep", "disc_precipice", "disc_relic", "disc_stal", "disc_strad", "disc_wait", "disc_ward");

    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    public Settings() {
        registerConfig();
    }

    public void registerConfig() {
        plugin.saveDefaultConfig();
        reloadConfigFromFile();
        loadSettings();
        if (!configVersion.equals(getFileConfigVersion())) {
            Versus.log("Error: Config is outdated. Versus will now force a config update and copy all existing options.");
            updateConfig();
        }
    }

    public void reloadConfig(Runnable after) {
        Bukkit.getScheduler().runTaskAsynchronously(Versus.getInstance(), () -> {
            registerConfig();
            MessageUtil.updateColors();
            Bukkit.getScheduler().runTask(Versus.getInstance(), after);
        });
    }

    public static boolean is(Setting setting) {
        String key = setting.getPath();
        return (Boolean) setting.getValue();
    }

    public static String getMessage(Setting setting) {
        String message = (String) setting.getValue();
        return MessageUtil.color(message);
    }

    /**
     * Returns location if it exists, else returns null;
     * @param setting
     * @return
     */
    public static Location getLocation(Setting setting) {
        ConfigurationSection section = (ConfigurationSection) setting.getValue();
        Double x = section.getDouble("x");
        Double y = section.getDouble("y");
        Double z = section.getDouble("z");
        String worldName = section.getString("world");
        if(worldName == null || z == null || y == null || z == null) {
            Versus.error("Improperly formatted location at '"+setting.toString().toLowerCase()+"' in config.yml.");
            return null;
        }
        World world = Bukkit.getServer().getWorld(worldName);
        if(world == null) {
            Versus.error("Improperly formatted location at '"+setting.toString().toLowerCase()+"' in config.yml. \n"+
                         worldName+" is not a world!");
            return null;
        }
        return new Location(world, x, y, z);
    }

    public static String getStringVersion(Setting setting) {
        return String.valueOf(setting.getValue());
    }

    public static <T> T getDefaultValue(Setting setting) {
        return (T) setting.getDefaultValue();
    }

    public static ReturnOption getReturnOption(Setting setting) {
        String option = (String) setting.getValue();
        try {
            return ReturnOption.valueOf(option.toUpperCase());
        } catch (IllegalArgumentException e) {
            Versus.log("Config option named: '" + setting.toString().toLowerCase() + "' is not set properly. " +
                    "Please review config.yml file.");
            return ReturnOption.SPAWN;
        }
    }

    public static Color getColor(Setting setting) {
        String colorStr = (String) setting.getValue();
        if (colorMap.containsKey(colorStr)) {
            return colorMap.get(colorStr);
        } else {
            String newKey = colorMap.keySet().stream()
                    .max(Comparator.comparingInt(colorKey -> colorKey.compareToIgnoreCase(colorStr)))
                    .get();
            return colorMap.get(newKey);
        }
    }

    public static Sound getSong(Setting setting) {
        try {
            String songStr = "MUSIC_" + setting.getValue().toString().toUpperCase();
            return Sound.valueOf(songStr);
        } catch (Exception e) {
            e.printStackTrace();
            String defaultSongStr = "MUSIC_" + setting.getDefaultValue().toString().toUpperCase();
            return Sound.valueOf(defaultSongStr);
        }
    }

    public static String getMessage(Setting setting, Placeholder... placeholders) {
        String message = (String) setting.getValue();
        for (Placeholder placeholder : placeholders) {
            message = message.replaceAll(placeholder.holder, placeholder.replacement);
        }
        return MessageUtil.color(message);
    }

    public static String getDefaultMessage(Setting setting, Placeholder... placeholders) {
        String message = (String) setting.getDefaultValue();
        for (Placeholder placeholder : placeholders) {
            message = message.replaceAll(placeholder.holder, placeholder.replacement);
        }
        return MessageUtil.color(message);
    }

    public List<String> getStringList(Setting setting) {
        return config.getStringList(setting.getPath());
    }

    public static int getNumber(Setting setting) {
        return (int) setting.getValue();
    }

    public static <T> T get(Setting setting, Class<T> type) {
        return (T) setting.getValue();
    }

    public static boolean isSetting(String name) {
        try {
            Setting.valueOf(name);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    private void loadSettings() {
        Setting[] settings = Setting.values();
        reloadConfigFromFile();
        boolean shouldUpdate = false;
        for (Setting setting : settings) {
            try {
                loadSetting(setting);
            } catch (Exception e) {
                shouldUpdate = true;
            }
        }
        if (shouldUpdate) updateConfig();
    }

    private void reloadConfigFromFile() {
        File file = new File(plugin.getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration(file);
    }

    private void saveConfigToFile() {
        File file = new File(plugin.getDataFolder(), "config.yml");
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateConfig() {
        Versus.log("Updating plugin config...");
        plugin.saveResource("config.yml", true);
        reloadConfigFromFile();
        Setting[] settings = Setting.values();
        for (Setting setting : settings) {
            saveSetting(setting);
        }
        saveConfigToFile();
        Versus.log("Config has been updated.");
    }

    public void changeSetting(Setting setting, Object newValue) {
        unsavedSettingChanges.put(setting, newValue);
    }

    /**
     * Saves all setting to the config, saves the config to the file, and reloads certain classes as necessary.
     *
     * @return A list of all settings changed.
     */
    public void saveSettingsChanges(Consumer<List<Setting>> after) {
        Bukkit.getScheduler().runTaskAsynchronously(Versus.getInstance(), () -> {
            try {
                unsavedSettingChanges.keySet().stream().forEach(setting -> {
                    String key = setting.getPath();
                    Object value = unsavedSettingChanges.get(setting);
                    if(setting.getType() == Setting.Type.LOCATION) value = parseLocationFromCLI(value);
                    if(value == null) return;
                    Versus.log("Changing " + setting + " to " + value);
                    setting.setValue(value);
                    config.set(key, value);
                });
                saveConfigToFile();
                List<Setting> changedCommands = List.copyOf(unsavedSettingChanges.keySet());
                Bukkit.getScheduler().runTask(Versus.getInstance(), () -> after.accept(changedCommands));
                unsavedSettingChanges.clear();
                MessageUtil.updateColors();
                Dependencies.refresh(Bukkit.getServer());
            } catch (Exception e) {
                e.printStackTrace();
                Bukkit.getScheduler().runTask(Versus.getInstance(), () -> after.accept(null));
            }
        });
    }

    /**
     * Converts a string like '1 2 3 world' into a Map which can easily be saved as a ConfigurationSection.
     * @param location
     * @return Returns null if unsuccessful
     */
    private Map<String, Object> parseLocationFromCLI(Object location) {
        if((location instanceof String) == false) return null;
        String locationArgument = (String) location;
        String[] arguments = locationArgument.split(" ");

        if (arguments.length < 4) return null;
        Double x = Double.parseDouble(arguments[0]);
        Double y = Double.parseDouble(arguments[1]);
        Double z = Double.parseDouble(arguments[2]);
        String worldName = arguments[3];

        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("x", x);
        linkedHashMap.put("y", y);
        linkedHashMap.put("z", z);
        linkedHashMap.put("world", worldName);
        return linkedHashMap;
    }

    private void loadSetting(Setting setting) throws Exception {
        String key = setting.getPath();
        if (!config.isSet(key) || config.get(key) == null) {
            Versus.error("The config option: '" + key + "' is not in the config.");
            throw new Exception("Key is not set in config");
        }
        Object configValue = config.get(key);
        Boolean successful = setting.setValue(configValue);
        if (!successful) {
            Versus.error("The config option named '" + key + "' is not set to a proper value. For " +
                    "example, you may be passing in a number when text is expected, or a true/false value " +
                    "where a number is expected. Please check your config.yml for any improper bindings.");
        }
    }

    /**
     * Attempts to convert value into an appropriate object.
     * Returns null if unsuccessful.
     *
     * @param value   The string value of the setting.
     * @param setting The setting in question.
     * @return The new value, returned as a generic object.
     */
    public static Object tryConvertFromString(String value, Setting setting) {
        Object newValue;
        switch (setting.getType()) {
            case NUMBER:
                try {
                    newValue = Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    return null;
                }
                break;
            case BOOLEAN:
                if (value.equals("true")) {
                    newValue = true;
                } else if (value.equals("false")) {
                    newValue = false;
                } else {
                    newValue = null;
                }
                break;
            default:
                newValue = value;
                break;
        }
        return newValue;
    }

    public void saveSetting(Setting setting) {
        String key = setting.getPath();
        Object value = setting.getValue();
        config.set(key, value);
        saveConfigToFile();
    }

    private String getFileConfigVersion() {
        return config.isSet("config-version") ? config.getString("config-version") : "0.0";
    }

}
