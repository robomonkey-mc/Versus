package me.robomonkey.versus.arena.command;

import me.robomonkey.versus.arena.Arena;
import me.robomonkey.versus.arena.ArenaManager;
import me.robomonkey.versus.command.AbstractCommand;
import me.robomonkey.versus.util.MessageUtil;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ListCommand extends AbstractCommand {

    public ListCommand() {
        super("list", "versus.arena.list");
        setMaxArguments(1);
        setArgumentRequired(false);
        setUsage("/arena list");
        setDescription("Sends a list of all arenas and their locations.");
    }

    @Override
    public void callCommand(CommandSender sender, String[] args) {
        if (ArenaManager.getInstance().getAllArenas().size() == 0) {
            sender.sendMessage(MessageUtil.get("&sThere are no &pactive arenas&s currently."));
            return;
        }
        sender.sendMessage(MessageUtil.LINE);
        ArenaManager.getInstance().getAllArenas().forEach(arena -> {
            String description = getArenaDescription(arena);
            sender.sendMessage(description);
        });
        sender.sendMessage(MessageUtil.LINE);
    }

    public String getArenaDescription(Arena arena) {
        Location arenaLocation = arena.getCenterLocation();
        int arenaX = (int) arenaLocation.getX(), arenaY = (int) arenaLocation.getY(), arenaZ = (int) arenaLocation.getZ();
        return MessageUtil.color(
                "&h" + arena.getName() + ": " + "&sX&p " + arenaX + " &sY&p " + arenaY + " &sZ&p " + arenaZ + " &sKit: &p" + arena.getKit().getName() + "\n"
        );
    }

    @Override
    public List<String> callCompletionsUpdate(CommandSender sender, String[] args) {
        return null;
    }
}
