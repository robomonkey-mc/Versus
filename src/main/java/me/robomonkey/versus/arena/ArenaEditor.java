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

    void displayInstructionalMessage(ArenaProperty property, Player player){
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F);
        String buttonBase = "%button%&6 to select the";
        String explanationBase = Versus.color("&e&o "+property.toFriendlyString());
        String explanationOnHover = Versus.color("&7"+property.getExplanation());
        String commandOnClick = "/arena set " + targetArena.getName() + " " +property.toString();
        String commandOnHover = Versus.color("&7"+commandOnClick);
        TextComponent setPropertyMessage = MessageUtil.getClickableMessage(buttonBase, commandOnClick, commandOnHover, "&6&lClick here");
        TextComponent explanationMessage = MessageUtil.getHoverText(explanationBase, explanationOnHover);
        setPropertyMessage.addExtra(explanationMessage);
        player.spigot().sendMessage(setPropertyMessage);
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
        p.sendMessage(MessageUtil.color("&7&oYou have successfully set the "+property.toFriendlyString()+" to your current location."));
    }
}
