package me.robomonkey.versus.util;

import me.robomonkey.versus.Versus;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import javax.sound.sampled.Line;

public class MessageUtil {

    /**
     * Sends a player a clickable message where the button is contained within %button%
     * Example: "Click %button% to do X!
     */
    public static String LINE = MessageUtil.color("&7&m-----------------------------");

    public static TextComponent getClickableMessage(String message, String commandText, String hoverText, String buttonText){
        String[] splitMessage = message.split("%button%", 3);
        if(splitMessage.length<2){
            return null;
        }
        String beforeText = splitMessage[0];
        String afterText = splitMessage[1];

        TextComponent selectButton = createButton(color(buttonText), commandText, hoverText);
        TextComponent mainMessage = new TextComponent(Versus.color(beforeText));
        mainMessage.addExtra(selectButton);
        mainMessage.addExtra(Versus.color(afterText));
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
        TextComponent mainMessage = new TextComponent(Versus.color(beforeText));
        mainMessage.addExtra(selectButton);
        mainMessage.addExtra(Versus.color(afterText));
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
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String error(String message){
        return color("&c&lError: &4" + message);
    }

}
