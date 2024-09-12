package me.robomonkey.versus.util;

import me.robomonkey.versus.settings.Setting;
import me.robomonkey.versus.settings.Settings;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;

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

    public static void updateColors() {
        LINE = Settings.getMessage(Setting.LINE);
        PRIMARY = Settings.getMessage(Setting.PRIMARY_COLOR);
        HIGHLIGHTED = Settings.getMessage(Setting.HIGHLIGHTED_COLOR);
        BOLD = Settings.getMessage(Setting.BOLD_COLOR);
        SUBTLE = Settings.getMessage(Setting.SUBTLE_COLOR);
    }

    public static TextComponent getClickableMessage(String message, String commandText, String hoverText, String buttonText) {
        String[] splitMessage = message.split("%button%", 3);
        if (splitMessage.length < 2) {
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

    public static TextComponent getClickableMessageBetween(String message, String commandText, String hoverText, String buttonEndpoint) {
        String[] messageSegments = message.split(buttonEndpoint);
        String buttonText = "";
        int buttonTextIndex;

        if (message.startsWith(buttonEndpoint)) buttonTextIndex = 1;
        else if (message.endsWith(buttonEndpoint)) buttonTextIndex = messageSegments.length - 1;
        else buttonTextIndex = 1;
        TextComponent messageWithButton = new TextComponent("");
        for (int index = 0; index < messageSegments.length; index++) {
            if (index == buttonTextIndex) {
                buttonText = messageSegments[buttonTextIndex];
                TextComponent button = createButton(color(buttonText), commandText, hoverText);
                messageWithButton.addExtra(button);
            } else {
                messageWithButton.addExtra(color(messageSegments[index]));
            }
        }
        return messageWithButton;
    }

    /**
     * Sends a player a clickable message where the button is contained within %button%
     * Example: "Click %button% to do X!
     */
    public static TextComponent getClickableMessage(String message, String commandText, String buttonText) {
        String[] splitMessage = message.split("%button%", 3);
        if (splitMessage.length < 2) {
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
    public static TextComponent createButton(String buttonText, String commandOnClick, String hoverText) {
        TextComponent newButton = new TextComponent(color(buttonText));
        newButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, commandOnClick));
        newButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverText).create()));
        return newButton;
    }

    /**
     * Returns a button component with the given command ran on click event.
     */
    public static TextComponent createButton(String buttonText, String commandOnClick) {
        TextComponent newButton = new TextComponent(color(buttonText));
        newButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, commandOnClick));
        return newButton;
    }

    /**
     * Returns a componnet with the given hoverText on hover.
     */
    public static TextComponent getHoverText(String text, String hoverText) {
        TextComponent newButton = new TextComponent(color(text));
        newButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverText).create()));
        return newButton;
    }

    public static String color(String message) {
        message = message.replaceAll("&p", PRIMARY)
                .replaceAll("&h", HIGHLIGHTED)
                .replaceAll("&bold", BOLD)
                .replaceAll("&s", SUBTLE);
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String get(String message) {
        message = (Settings.is(Setting.PREFIX_ENABLED)) ? Settings.getMessage(Setting.PREFIX) + message : message;
        return color(message);
    }

    public static String error(String message) {
        return color(Settings.getMessage(Setting.ERROR_PREFIX) + message);
    }

}
