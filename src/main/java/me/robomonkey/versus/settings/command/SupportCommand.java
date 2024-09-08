package me.robomonkey.versus.settings.command;

import me.robomonkey.versus.command.AbstractCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

public class SupportCommand extends AbstractCommand {
    public SupportCommand() {
        super("support", null);
        setUsage("/versus support");
        setDescription("Opens the link for discord support.");
        setAutonomous(true);
    }

    @Override
    public void callCommand(CommandSender sender, String[] args) {

    }

    @Override
    public List<String> callCompletionsUpdate(CommandSender sender, String[] args) {
        return List.of();
    }
}
