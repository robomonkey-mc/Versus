package me.robomonkey.versus.arena.command;

import me.robomonkey.versus.Versus;
import me.robomonkey.versus.arena.Arena;
import me.robomonkey.versus.arena.ArenaManager;
import me.robomonkey.versus.command.AbstractCommand;
import me.robomonkey.versus.util.MessageUtil;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ListCommand extends AbstractCommand {

    public ListCommand() {
        super("list","arena.list");
        setMaxArguments(1);
        setArgumentRequired(false);
        setUsage("Get the names of all arenas."+"\n"+
                "/arena list");
    }

    @Override
    public void callCommand(CommandSender sender, String[] args) {
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
                "&a"+arena.getName()+":" + "\n"+
                "&7Location: &aX&7 "+arenaX+" &aY&7 "+arenaY+" &aZ&7 " + arenaZ + "\n"
        );
    }

    @Override
    public List<String> callCompletionsUpdate(CommandSender sender, String[] args) {
        return null;
    }
}
