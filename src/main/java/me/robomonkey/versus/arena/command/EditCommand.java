package me.robomonkey.versus.arena.command;

import me.robomonkey.versus.arena.Arena;
import me.robomonkey.versus.arena.ArenaEditor;
import me.robomonkey.versus.arena.ArenaManager;
import me.robomonkey.versus.command.AbstractCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class EditCommand extends AbstractCommand {

    public EditCommand() {
        super("edit", "versus.arena.edit");
        setPlayersOnly(true);
        setUsage("/arena edit <arenaname>");
        setDescription("Opens a menu to edit an existing arena.");
        setMaxArguments(1);
    }

    @Override
    public void callCommand(CommandSender sender, String[] args) {
        String arenaName = args[0];
        Player player = (Player) sender;
        ArenaManager arenaManager = ArenaManager.getInstance();
        Arena fromString = arenaManager.getArena(arenaName);
        if (fromString == null) {
            error(sender, "No arena exists with the name '" + arenaName + "'.");
        } else {
            ArenaEditor.openEditingMenu(player, fromString);
        }
    }

    @Override
    public List<String> callCompletionsUpdate(CommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> arenaNames = ArenaManager.getInstance().getAllArenas()
                    .stream().map(arena -> arena.getName())
                    .collect(Collectors.toList());
            return arenaNames;
        }
        return null;
    }
}
