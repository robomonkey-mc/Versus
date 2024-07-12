package me.robomonkey.versus.arena;

import me.robomonkey.versus.util.MessageUtil;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class ArenaEditor {

    ArenaProperty currentProperty;
    Arena targetArena;

    public ArenaEditor(Arena a){
        currentProperty = ArenaProperty.CENTER_LOCATION;
    }

    void displayInstructionalMessage(ArenaProperty property, Player p){
        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F);
        String clickableMessage = "&cClick &c&l%button%&c to select the &4"+property.toFriendlyString();
        String command = property.toString();
        TextComponent setPropertyMessage = MessageUtil.getClickableMessage(clickableMessage, command);
        p.spigot().sendMessage(setPropertyMessage);
    }

    private boolean isAllPropertiesCompleted(){
        return currentProperty == null;
    }

    void finalizeArena() {
        targetArena.setEnabled(true);
    }


}
