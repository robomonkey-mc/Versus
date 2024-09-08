package me.robomonkey.versus.settings.command;

import me.robomonkey.versus.command.AbstractCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ConfigCommand extends AbstractCommand {

    public ConfigCommand() {
        super("config", "");
        setUsage("/versus config");
        setDescription("Shows all commands for /versus config.");
        addBranches(new SaveCommand(), new SetCommand(), new ReloadCommand());
        setPlayersOnly(true);
        setAutonomous(false);
    }

    @Override
    public void callCommand(CommandSender sender, String[] args) {

    }

    @Override
    public List<String> callCompletionsUpdate(CommandSender sender, String[] args) {
        return List.of();
    }
}
