package me.robomonkey.versus.arena.command;

import me.robomonkey.versus.command.AbstractCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

public class DeleteCommand extends AbstractCommand {

    public DeleteCommand(){
        super("delete", "arena.delete");
        setUsage("/arena delete <arenaName>" + "\n"+
                "Delete an existing arena.");
        setPlayersOnly(false);
    }

    @Override
    public void callCommand(CommandSender sender, String[] args) {
    }

    @Override
    public void callCompletionsUpdate(CommandSender sender) {

    }
}
