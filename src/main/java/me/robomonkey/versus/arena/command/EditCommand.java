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

    public EditCommand(){
        super("edit", "arena.edit");
        setPlayersOnly(true);
        setUsage("/arena edit <arenaname>" + "\n" +
                "Edit an existing Arena.");
    }

    @Override
    public void callCommand(CommandSender sender, String[] args) {
        String arenaName = args[0];
        Player player = (Player) sender;
        ArenaManager arenaManager = ArenaManager.getInstance();
        Arena fromString = arenaManager.getArena(arenaName);
        if(fromString == null){
            error(sender, "No arena exists with the name "+arenaName);
        }else{
            ArenaEditor editor = new ArenaEditor(fromString);
            editor.openEditingMenu(player);
        }
    }

    @Override
    public void callCompletionsUpdate(CommandSender sender, String[] args) {
        if(args.length==1) {
            List<String> arenaNames = ArenaManager.getInstance().getAllArenas()
                    .stream().map(arena -> arena.getName())
                    .collect(Collectors.toList());
            setTabCompletions(arenaNames);
        }
    }
}
