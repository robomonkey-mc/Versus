package me.robomonkey.versus.arena.command;

import me.robomonkey.versus.arena.ArenaBuilderCoordinator;
import me.robomonkey.versus.arena.ArenaManager;
import me.robomonkey.versus.command.AbstractCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CreateCommand extends AbstractCommand {

    public CreateCommand() {
        super("create", "versus.arena.create");
        addTabCompletion("<arenaname>");
        setUsage("/arena create <arenaname>");
        setDescription("Creates a dueling arena and initiates the arena building process.");
        setPlayersOnly(true);
        setStaticTabComplete(true);
        setMaxArguments(1);
    }

    @Override
    public void callCommand(CommandSender sender, String[] args) {
        String arenaName = args[0];
        Player player = (Player) sender;
        if (ArenaManager.getInstance().getArena(arenaName) != null) {
            error(sender, "Another arena already exists with the name " + arenaName + ".");
            return;
        }
        ArenaBuilderCoordinator.getInstance().initiateArenaBuilder(player, arenaName);
    }

    @Override
    public List<String> callCompletionsUpdate(CommandSender sender, String[] args) {
        return null;
    }
}
