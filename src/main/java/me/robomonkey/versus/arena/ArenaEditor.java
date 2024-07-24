package me.robomonkey.versus.arena;

import me.robomonkey.versus.Versus;
import me.robomonkey.versus.util.MessageUtil;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ArenaEditor {
    private Arena targetArena;

    public ArenaEditor(Arena target){
        this.targetArena = target;
    }

    void displayInstructionalMessage(ArenaProperty property, Player p){
        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F);
        p.sendMessage(property.getExplanation());
        String clickableMessage = "&cClick &c&l%button%&c to select the &4"+property.toFriendlyString();
        String command = "/arena set " + targetArena.getName() + " " +property.toString();
        TextComponent setPropertyMessage = MessageUtil.getClickableMessage(clickableMessage, command, command);
        p.spigot().sendMessage(setPropertyMessage);
    }

    public void openEditingMenu(Player player) {
        player.sendMessage(Versus.color("--------------------------"));
        Arrays.stream(ArenaProperty.values()).forEach((property) -> displayInstructionalMessage(property, player));
        String exitButtonText = Versus.color("&4&lEXIT");
        String exitCommand = "/arena edit";
        player.spigot().sendMessage(MessageUtil.createButton(exitButtonText, exitCommand, exitCommand));
        player.sendMessage(Versus.color("--------------------------"));
    }

    public void alterArenaProperty(ArenaProperty property, Player p) {
        targetArena.setLocationProperty(property, p.getLocation());
        p.sendMessage("You have successful set the "+property.toFriendlyString()+" to your current location.");
    }
}
