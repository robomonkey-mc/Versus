package me.robomonkey.versus.arena.command;

import me.robomonkey.versus.command.RootCommand;
import me.robomonkey.versus.kit.command.KitDeleteCommand;
import me.robomonkey.versus.kit.command.LoadKitCommand;
import me.robomonkey.versus.kit.command.SaveKitCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

public class RootArenaCommand extends RootCommand {

    public RootArenaCommand() {
        super("arena", "versus.arena");
        addBranches(new CreateCommand(),
                new DeleteCommand(),
                new EditCommand(),
                new SetCommand(),
                new ListCommand(),
                new KitDeleteCommand(),
                new SaveKitCommand(),
                new LoadKitCommand(),
                new VisitCommand()
        );
        setArgumentRequired(true);
    }

    @Override
    public void callCommand(CommandSender sender, String[] args) {
        sender.sendMessage(getUsage());
    }

    @Override
    public List<String> callCompletionsUpdate(CommandSender sender, String[] args) {
        return null;
    }
}
