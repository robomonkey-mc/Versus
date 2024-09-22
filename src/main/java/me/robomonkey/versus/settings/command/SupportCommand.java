package me.robomonkey.versus.settings.command;

import me.robomonkey.versus.command.AbstractCommand;
import me.robomonkey.versus.util.MessageUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;

import java.util.List;

public class SupportCommand extends AbstractCommand {

    public static final String DISCORD_LINK = "https://discord.gg/M7vJCAN2kb";

    public SupportCommand() {
        super("support", null);
        setUsage("/versus support");
        setDescription("Opens the link for discord support.");
        setAutonomous(true);
        setArgumentRequired(false);
    }

    @Override
    public void callCommand(CommandSender sender, String[] args) {
        TextComponent message = new TextComponent(MessageUtil.get("&sJoin our &b&ldiscord!"));
        String hoverText = MessageUtil.get("&sClick here!");
        message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, DISCORD_LINK));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverText).create()));
        sender.spigot().sendMessage(message);
    }

    @Override
    public List<String> callCompletionsUpdate(CommandSender sender, String[] args) {
        return List.of();
    }
}
