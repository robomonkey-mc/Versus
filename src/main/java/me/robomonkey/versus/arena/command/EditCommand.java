package me.robomonkey.versus.arena.command;

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

    }

    @Override
    public void callCompletionsUpdate(CommandSender sender) {

    }
}
