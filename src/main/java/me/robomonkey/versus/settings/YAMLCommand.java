package me.robomonkey.versus.settings;

import me.robomonkey.versus.util.MessageUtil;
import org.bukkit.configuration.ConfigurationSection;

public class YAMLCommand {
    private ConfigurationSection section;

    public YAMLCommand(ConfigurationSection section) {
        this.section = section;
    }

    public String get(String key) {
        ConfigurationSection currSection = section;
        return currSection.getString(key);
    }

    public String get(String key, Placeholder... placeholders) {
        String message = get(key);
        for (Placeholder placeholder : placeholders) {
            message = message.replaceAll(placeholder.holder, placeholder.replacement);
        }
        return MessageUtil.color(message);
    }

}
