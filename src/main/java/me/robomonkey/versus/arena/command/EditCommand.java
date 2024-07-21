package me.robomonkey.versus.arena.command;

import me.robomonkey.versus.arena.Arena;
import me.robomonkey.versus.arena.ArenaManager;
import me.robomonkey.versus.command.AbstractCommand;
import org.bukkit.command.CommandSender;

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
        ArenaManager arenaManager = ArenaManager.getInstance();
        Arena fromString = arenaManager.getArena(arenaName);
        if(fromString == null){
            error(sender, "No arena exists with the name "+arenaName);
        }else{
            arenaManager.editArena(fromString);
        }
    }

    @Override
    public void callCompletionsUpdate(CommandSender sender) {

    }
}
