package me.robomonkey.versus.arena.command;

import me.robomonkey.versus.command.AbstractCommand;
import org.bukkit.command.CommandSender;

public class CreateCommand extends AbstractCommand {

    public CreateCommand(){
        super("create","arena.create");
        addTabCompletion("<name>");
    }

    @Override
    public boolean callCommand(CommandSender sender, String[] args) {
        return false;
    }
}
