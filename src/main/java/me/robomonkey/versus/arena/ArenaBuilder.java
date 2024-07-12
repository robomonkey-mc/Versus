package me.robomonkey.versus.arena;

import me.robomonkey.versus.Versus;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.checkerframework.common.returnsreceiver.qual.This;

import java.util.ArrayList;
import java.util.List;

public class ArenaBuilder {

    public void handleNextPhase(Player p) {
        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F);

    }

    /**
     * Sends a player a clickable message where the button is contained within %button%
     * Example: "Click %button% to do X!
     */
    private TextComponent getClickableMessage(String message, String commandText, String hoverText){
        String commandOnClick = "/" + commandText;
        String[] splitMessage = message.split("%button%", 3);
        if(splitMessage.length<2){
            //TODO: Add default setting in this case
        }
        String beforeText = splitMessage[0];
        String afterText = splitMessage[1];

        TextComponent selectButton = createButton("SELECT", commandOnClick, hoverText);
        TextComponent mainMessage = new TextComponent(Versus.color(beforeText));
        mainMessage.addExtra(selectButton);
        mainMessage.addExtra(Versus.color(afterText));
        return mainMessage;
    }

    private TextComponent createButton(String buttontText, String commandOnClick, String hoverText){
        TextComponent newButton = new TextComponent(buttontText);
        newButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, commandOnClick));
        newButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverText).create()));
        return newButton;
    }


}
