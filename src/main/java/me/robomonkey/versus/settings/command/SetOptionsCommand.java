package me.robomonkey.versus.settings.command;

import me.robomonkey.versus.command.AbstractCommand;
import me.robomonkey.versus.duel.menu.AdminDuelMenu;
import me.robomonkey.versus.settings.Setting;
import me.robomonkey.versus.settings.Settings;
import me.robomonkey.versus.util.EffectUtil;
import me.robomonkey.versus.util.MessageUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SetOptionsCommand extends AbstractCommand {

    public SetOptionsCommand() {
        super("setOptions", "");
        setDescription("Select the decisions players will make before entering a duel.");
        setArgumentRequired(false);
        setAutonomous(true);
    }

    @Override
    public void callCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        AdminDuelMenu gui = new AdminDuelMenu(player, (options) -> {
            Settings.setEnabledDuelOptions(options);
            EffectUtil.spawnFireWorks(player.getLocation(), 1, 10, Settings.getColor(Setting.FIREWORKS_COLOR));
            StringBuilder responseBuilder = new StringBuilder("&sPlayers can now set");
            options.forEach((option) -> responseBuilder.append("&p" + option.toString().toLowerCase() + "&s, "));
            responseBuilder.append("before starting a duel. Type &p/versus rules setOptions&7 to edit.");
            sender.sendMessage(MessageUtil.get(responseBuilder.toString()));
        });
        gui.open();
    }

    @Override
    public List<String> callCompletionsUpdate(CommandSender sender, String[] args) {
        return List.of();
    }
}
