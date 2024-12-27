package me.robomonkey.versus.settings.command;

import me.robomonkey.versus.command.AbstractCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

public class RulesCommand extends AbstractCommand {

    public RulesCommand() {
        super("config", "");
        setUsage("/versus rules");
        setDescription("Shows all commands for /versus rules.");
        addBranches(new SetOptionsCommand());
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

