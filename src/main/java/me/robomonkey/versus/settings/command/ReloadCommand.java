package me.robomonkey.versus.settings.command;

import me.robomonkey.versus.command.AbstractCommand;
import me.robomonkey.versus.settings.Settings;
import me.robomonkey.versus.util.MessageUtil;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ReloadCommand extends AbstractCommand {

    public ReloadCommand() {
        super("reload", null);
        setUsage("/versus config reload");
        setDescription("Reloads all changes from the config file.");
        setArgumentRequired(false);
        setAutonomous(true);
    }

    @Override
    public void callCommand(CommandSender sender, String[] args) {
        sender.sendMessage(MessageUtil.get("&sReloading all settings from config.yml"));
        Settings.getInstance().reloadConfig(() -> {
            sender.sendMessage(MessageUtil.get("&sSuccessfully reloaded all settings from config.yml"));
        });
    }

    @Override
    public List<String> callCompletionsUpdate(CommandSender sender, String[] args) {
        return List.of();
    }
}
