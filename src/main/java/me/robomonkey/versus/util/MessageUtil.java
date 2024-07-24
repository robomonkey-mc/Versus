package me.robomonkey.versus.util;

import me.robomonkey.versus.Versus;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class MessageUtil {

    /**
     * Sends a player a clickable message where the button is contained within %button%
     * Example: "Click %button% to do X!
     */
    public static TextComponent getClickableMessage(String message, String commandText, String hoverText){
        String[] splitMessage = message.split("%button%", 3);
        if(splitMessage.length<2){
            return null;
        }
        String beforeText = splitMessage[0];
        String afterText = splitMessage[1];

        TextComponent selectButton = createButton("SELECT", commandText, hoverText);
        TextComponent mainMessage = new TextComponent(Versus.color(beforeText));
        mainMessage.addExtra(selectButton);
        mainMessage.addExtra(Versus.color(afterText));
        return mainMessage;
    }

    /**
     * Sends a player a clickable message where the button is contained within %button%
     * Example: "Click %button% to do X!
     */
    public static TextComponent getClickableMessage(String message, String commandText){
        String[] splitMessage = message.split("%button%", 3);
        if(splitMessage.length<2){
            //TODO: Add default setting in this case
        }
        String beforeText = splitMessage[0];
        String afterText = splitMessage[1];

        TextComponent selectButton = createButton("SELECT", commandText);
        TextComponent mainMessage = new TextComponent(Versus.color(beforeText));
        mainMessage.addExtra(selectButton);
        mainMessage.addExtra(Versus.color(afterText));
        return mainMessage;
    }

    /**
     * Returns a button component with the given text on both click and hover events.
     */
    public static TextComponent createButton(String buttontText, String commandOnClick, String hoverText){
        TextComponent newButton = new TextComponent(buttontText);
        newButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, commandOnClick));
        newButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverText).create()));
        return newButton;
    }

    /**
     * Returns a button component with the given text on click event.
     */
    public static TextComponent createButton(String buttontText, String commandOnClick){
        TextComponent newButton = new TextComponent(buttontText);
        newButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, commandOnClick));
        return newButton;
    }

    public static String color(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String error(String message){
        return color("&c&lError: &4" + message);
    }
}
