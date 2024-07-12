package me.robomonkey.versus.arena;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.checkerframework.common.returnsreceiver.qual.This;

import java.util.ArrayList;
import java.util.List;

public class ArenaBuilder {
    enum Phase {
        SET_CENTER_POSITION,
        SET_FIRST_POSITION,
        SET_SECOND_POSITION,
        FINALIZE;

    }

    public void selectMiddlePoint(Player p) {
        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F);
        TextComponent message = new TextComponent(Main.colour(Main.prefix+ChatColor.RESET+ChatColor.GOLD+" Click"));
        TextComponent deny = new TextComponent(Main.colour(" &3&lSELECT "));
        deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/arena selectMiddle"));
        deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("SelectMiddle").create()));
        message.addExtra(deny);
        message.addExtra(Main.colour("&6to select the &4&lFIREWORK MIDDLE&r&6 point."));
        p.spigot().sendMessage(message);
    }


}
