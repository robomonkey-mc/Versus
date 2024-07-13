package me.robomonkey.versus.arena.command;

import me.robomonkey.versus.command.AbstractCommand;
import org.bukkit.command.CommandSender;

public class EditCommand extends AbstractCommand {

    public EditCommand(){
        super("edit", "arena.edit");
    }

    @Override
    public boolean callCommand(CommandSender sender, String[] args) {
        return false;
    }
}
