package me.robomonkey.versus.arena.command;

import me.robomonkey.versus.arena.Arena;
import me.robomonkey.versus.arena.ArenaManager;
import me.robomonkey.versus.command.AbstractCommand;
import me.robomonkey.versus.util.MessageUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class VisitCommand extends AbstractCommand {


    public VisitCommand() {
        super("visit", "versus.arena.visit");
        setUsage("/arena visit <arenaname>");
        setDescription("Visit an existing arena.");
        setPlayersOnly(true);
    }

    @Override
    public void callCommand(CommandSender sender, String[] args) {
        if (args.length == 0) {
            error(sender, "Please provide an arena");
            return;
        }
        String arenaName = args[0];
        Arena target = ArenaManager.getInstance().getArena(arenaName);
        Player player = (Player) sender;
        if (target == null) {
            error(sender, "No arena exists with the name '" + arenaName + "'.");
            return;
        }
        player.teleport(target.getCenterLocation());
        player.sendMessage(MessageUtil.get("&sNow visiting &h" + arenaName + "&s."));
    }

    @Override
    public List<String> callCompletionsUpdate(CommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> arenaNames = ArenaManager.getInstance().getAllArenas()
                    .stream().map(arena -> arena.getName())
                    .collect(Collectors.toList());
            return arenaNames;
        }
        return null;
    }
}
