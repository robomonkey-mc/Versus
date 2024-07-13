package me.robomonkey.versus.arena.command;

import me.robomonkey.versus.command.AbstractCommand;
import org.bukkit.command.CommandSender;

public class DeleteCommand extends AbstractCommand {

    public DeleteCommand(){
        super("delete", "arena.delete");
    }

    @Override
    public boolean callCommand(CommandSender sender, String[] args) {
        return false;
    }
}
