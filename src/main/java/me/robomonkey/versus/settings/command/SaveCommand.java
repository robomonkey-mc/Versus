package me.robomonkey.versus.settings.command;

import me.robomonkey.versus.command.AbstractCommand;
import me.robomonkey.versus.settings.Settings;
import me.robomonkey.versus.util.MessageUtil;
import org.bukkit.command.CommandSender;

import java.util.List;

public class SaveCommand extends AbstractCommand {
    public SaveCommand() {
        super("save", null);
        setUsage("/versus config save");
        setDescription("Saves all changes made to config.");
        setArgumentRequired(false);
    }

    @Override
    public void callCommand(CommandSender sender, String[] args) {
        sender.sendMessage(MessageUtil.get("&sSaving all changes to Versus config..."));
        Settings.getInstance().saveSettingsChanges(allChanged -> {
            if (allChanged == null) {
                error(sender, "Failed to save config changes.");
                return;
            }
            sender.sendMessage(MessageUtil.get("&sSuccessfully saved all changes.\n&sChanged the following settings:"));
            allChanged.forEach(setting -> sender
                    .sendMessage(MessageUtil.get("&7&l■ &p&o" + setting.toString().toLowerCase())));
        });
    }

    @Override
    public List<String> callCompletionsUpdate(CommandSender sender, String[] args) {
        return List.of();
    }
}
