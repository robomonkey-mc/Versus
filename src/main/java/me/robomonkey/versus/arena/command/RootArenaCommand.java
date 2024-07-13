package me.robomonkey.versus.arena.command;

import me.robomonkey.versus.command.RootCommand;
import org.bukkit.command.CommandSender;

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
    public boolean callCommand(CommandSender sender, String[] args) {
        sender.sendMessage(getUsage());
        return false;
    }
}
