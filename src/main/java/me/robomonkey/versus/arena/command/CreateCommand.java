package me.robomonkey.versus.arena.command;

import me.robomonkey.versus.Versus;
import me.robomonkey.versus.arena.ArenaBuilderCoordinator;
import me.robomonkey.versus.arena.ArenaManager;
import me.robomonkey.versus.command.AbstractCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CreateCommand extends AbstractCommand {

    public CreateCommand(){
        super("create","arena.create");
        addTabCompletion("<arena name>");
        setUsage("/arena create <arenaname>");
        setPlayersOnly(true);
        setStaticTabComplete(true);
    }

    @Override
    public void callCommand(CommandSender sender, String[] args) {
        String arenaName = args[0];
        Player player = (Player) sender;
        ArenaBuilderCoordinator.getInstance().initiateArenaBuilder(player, arenaName);
        Versus.log("Arena command registered");
    }

    @Override
    public void callCompletionsUpdate(CommandSender sender, String[] args) {

    }
}
