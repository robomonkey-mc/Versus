package me.robomonkey.versus.util;

import me.robomonkey.versus.Versus;
import me.robomonkey.versus.settings.Setting;
import me.robomonkey.versus.settings.Settings;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import javax.sound.sampled.Line;

public class MessageUtil {

    /**
     * Sends a player a clickable message where the button is contained within %button%
     * Example: "Click %button% to do X!
     */
    public static String LINE = Settings.getMessage(Setting.LINE);
    public static String PRIMARY = Settings.getMessage(Setting.PRIMARY_COLOR);
    public static String HIGHLIGHTED = Settings.getMessage(Setting.HIGHLIGHTED_COLOR);
    public static String BOLD = Settings.getMessage(Setting.BOLD_COLOR);
    public static String SUBTLE = Settings.getMessage(Setting.SUBTLE_COLOR);

    public static TextComponent getClickableMessage(String message, String commandText, String hoverText, String buttonText){
        String[] splitMessage = message.split("%button%", 3);
        if(splitMessage.length<2){
            return null;
        }
        String beforeText = splitMessage[0];
        String afterText = splitMessage[1];

        TextComponent selectButton = createButton(color(buttonText), commandText, hoverText);
        TextComponent mainMessage = new TextComponent(MessageUtil.color(beforeText));
        mainMessage.addExtra(selectButton);
        mainMessage.addExtra(MessageUtil.color(afterText));
        return mainMessage;
    }

    /**
     * Sends a player a clickable message where the button is contained within %button%
     * Example: "Click %button% to do X!
     */
    public static TextComponent getClickableMessage(String message, String commandText, String buttonText){
        String[] splitMessage = message.split("%button%", 3);
        if(splitMessage.length<2){
            //TODO: Add default setting in this case
        }
        String beforeText = splitMessage[0];
        String afterText = splitMessage[1];

        TextComponent selectButton = createButton(color(buttonText), commandText);
        TextComponent mainMessage = new TextComponent(MessageUtil.color(beforeText));
        mainMessage.addExtra(selectButton);
        mainMessage.addExtra(MessageUtil.color(afterText));
        return mainMessage;
    }

    /**
     * Returns a button component with the given text on both click and hover events.
     */
    public static TextComponent createButton(String buttonText, String commandOnClick, String hoverText){
        TextComponent newButton = new TextComponent(color(buttonText));
        newButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, commandOnClick));
        newButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverText).create()));
        return newButton;
    }

    /**
     * Returns a button component with the given command ran on click event.
     */
    public static TextComponent createButton(String buttonText, String commandOnClick){
        TextComponent newButton = new TextComponent(color(buttonText));
        newButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, commandOnClick));
        return newButton;
    }

    /**
     * Returns a componnet with the given hoverText on hover.
     */
    public static TextComponent getHoverText(String text, String hoverText){
        TextComponent newButton = new TextComponent(color(text));
        newButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverText).create()));
        return newButton;
    }

    public static String color(String message){
        message = message.replaceAll("&p", PRIMARY)
                .replaceAll("&h", HIGHLIGHTED)
                .replaceAll("&bold", BOLD)
                .replaceAll("&s", SUBTLE);
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String get(String message){
        message = (Settings.getBoolean(Setting.PREFIX_ENABLED))? Settings.getMessage(Setting.PREFIX) + message: message;
        return color(message);
    }

    public static String error(String message){
        return color(Settings.getMessage(Setting.ERROR_PREFIX) + message);
    }

}
