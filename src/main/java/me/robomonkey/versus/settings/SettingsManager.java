package me.robomonkey.versus.settings;

import me.robomonkey.versus.Versus;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class SettingsManager {

    Versus plugin = Versus.getInstance();
    FileConfiguration config;
    final static String configVersion = "1.0";
    private static SettingsManager instance;

    public static SettingsManager getInstance(){
        if(instance==null){
            instance = new SettingsManager();
        }
        return instance;
    }

    public SettingsManager(){
        registerConfig();
    }

    public void registerConfig() {
        plugin.saveDefaultConfig();
        reloadConfigFromFile();
        loadSettings();
        if(!configVersion.equals(getFileConfigVersion())){
            Versus.log("Error: Config is outdated. Versus will now force a config update and copy all existing options.");
            updateConfig();
        }
    }

    public boolean getBoolean(Setting setting){
        String key = setting.toString();
        return (Boolean) setting.value;
    }

    public String getMessage(Setting setting){
        String key = setting.toString();
        return (String) setting.value;
    }

    public String getMessage(Setting setting, String placeholder, String newValue){
        String key = setting.toString();
        String message = (String) setting.value;
        placeholder = "%" + placeholder + "%";
        String substitued = message.replaceAll(placeholder,newValue);
        return substitued;
    }

    public int getNumber(Setting setting){
        String key = setting.toString();
        return (int) setting.getValue();
    }

    private void loadSettings(){
        Setting[] settings = Setting.values();
        reloadConfigFromFile();
        boolean shouldUpdate = false;
        for(Setting setting: settings){
            try {
                loadSetting(setting);
            } catch (Exception e) {
                shouldUpdate = true;
            }
        }
        if(shouldUpdate) updateConfig();
    }

    private void reloadConfigFromFile(){
        File file = new File(plugin.getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration(file);
    }

    private void saveConfigToFile(){
        File file = new File(plugin.getDataFolder(), "config.yml");
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateConfig(){
        Versus.log("Updating plugin config...");
        plugin.saveResource("config.yml", true);
        reloadConfigFromFile();
        Setting[] settings = Setting.values();
        for(Setting setting: settings){
            saveSetting(setting);
        }
        saveConfigToFile();
        Versus.log("Config has been updated");
    }

    private void loadSetting(Setting setting) throws Exception {
        String key = setting.toString();
        if(config.isSet(key)){
            Object configValue = config.get(key);
            if (setting.setValue(configValue)) {
            } else {
                Versus.log("Error: The config option named '"+key+"' is not set to a proper value. For example, you may be passing in a number when text is expected, or a true/false value where a number is expected. Please check your config.yml for any improper bindings.");
            }
        }
        else
        {
            Versus.log("Error: The config option: '"+key+"' is not in the config. Versus will force a config update and restore default values.");
            throw new Exception("Key is not set in config.");
        }
    }

    private void saveSetting(Setting setting){
        String key = setting.toString();
        Object value = setting.getValue();
        config.set(key, value);
    }

    private String getFileConfigVersion(){
        if(!config.isSet("ConfigVersion")) return null;
        else return config.getString("ConfigVersion");
    }

}
