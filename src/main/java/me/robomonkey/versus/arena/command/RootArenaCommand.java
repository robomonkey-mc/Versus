package me.robomonkey.versus.arena.command;

import me.robomonkey.versus.command.RootCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

public class RootArenaCommand extends RootCommand {

    public RootArenaCommand() {
        super("arena", "arena.admin");
        addBranches(new CreateCommand(),
                    new DeleteCommand(),
                    new EditCommand()
        );
        setUsage("/arena <create/delete/edit/list>");
    }

    @Override
    public void callCommand(CommandSender sender, String[] args) {
        sender.sendMessage(getUsage());
    }

    @Override
    public void callCompletionsUpdate(CommandSender sender) {

    }
}