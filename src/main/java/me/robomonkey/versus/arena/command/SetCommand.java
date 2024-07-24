package me.robomonkey.versus.arena.command;

import me.robomonkey.versus.Versus;
import me.robomonkey.versus.arena.*;
import me.robomonkey.versus.command.AbstractCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SetCommand extends AbstractCommand {

    public SetCommand() {
        super("set", "arena.set");
        setUsage("/arena set <arenaname> <setting_name>");
        setPlayersOnly(true);
    }

    @Override
    public void callCommand(CommandSender sender, String[] args) {
        Versus.log("Set command ran!");
        if(args.length<2){
            sender.sendMessage(getUsage());
            return;
        }
        Player player = (Player) sender;
        String property = args[1];
        ArenaProperty propertyFromString = ArenaProperty.fromString(property);
        if(propertyFromString == null) {
            error(sender, "No arena property exists with that name.");
            return;
        }
        if(ArenaBuilderCoordinator.getInstance().hasArenaBuilder(player)) {
            ArenaBuilder builder = ArenaBuilderCoordinator.getInstance().getArenaBuilder(player);
            builder.handleArenaEdit(propertyFromString);
        } else {
            editWithoutBuilder(propertyFromString, args, player);
        }
    }

    private void editWithoutBuilder(ArenaProperty property, String[] args, Player player) {
        String arenaName = args[0];
        ArenaManager arenaManager = ArenaManager.getInstance();
        Arena arenaFromString = arenaManager.getArena(arenaName);

        if(arenaFromString == null) {
            error(player, "No arena exists with the name. " + arenaName);
            return;
        }

        ArenaEditor editor = new ArenaEditor(arenaFromString);
        editor.alterArenaProperty(property, player);
    }

    @Override
    public void callCompletionsUpdate(CommandSender sender, String[] args) {
        if(args.length==1){
            List<String> arenaNames = ArenaManager.getInstance().getAllArenas()
                    .stream().map(arena -> arena.getName())
                    .collect(Collectors.toList());
            setTabCompletions(arenaNames);
            setTabCompletions(arenaNames);
        }
        if(args.length==2){
            List<String> propertyNames = Arrays.stream(ArenaProperty.values()).map(property -> property.toString()).collect(Collectors.toList());
            setTabCompletions(propertyNames);
        }
    }
}
