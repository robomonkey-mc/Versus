package me.robomonkey.versus.arena.command;

import me.robomonkey.versus.arena.Arena;
import me.robomonkey.versus.arena.ArenaEditor;
import me.robomonkey.versus.arena.ArenaManager;
import me.robomonkey.versus.arena.ArenaProperty;
import me.robomonkey.versus.command.AbstractCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetCommand extends AbstractCommand {

    public SetCommand() {
        super("arena", "arena.admin");
        setUsage("/arena set <arenaname> <setting_name>");
        setPlayersOnly(true);
    }

    @Override
    public void callCommand(CommandSender sender, String[] args) {
        if(args.length<2){
            sender.sendMessage(getUsage());
        }
        Player player = (Player) sender;
        String arenaName = args[0];
        String property = args[1];
        ArenaManager arenaManager = ArenaManager.getInstance();
        Arena arenaFromString = arenaManager.getArena(arenaName);
        ArenaProperty propertyFromString = ArenaProperty.fromString(property);

        if(arenaFromString == null) {
            error(sender, "No arena exists with the name. " + arenaName);
            return;
        }

        if(propertyFromString == null) {
            error(sender, "No property exists with that name.");
            return;
        }

        ArenaEditor editor = new ArenaEditor(arenaFromString);
        editor.alterArenaProperty(propertyFromString, player);
    }

    @Override
    public void callCompletionsUpdate(CommandSender sender) {

    }
}
