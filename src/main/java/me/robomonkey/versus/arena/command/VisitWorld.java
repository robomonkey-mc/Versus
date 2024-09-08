package me.robomonkey.versus.arena.command;

import me.robomonkey.versus.Versus;
import me.robomonkey.versus.command.RootCommand;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class VisitWorld extends RootCommand {
    public VisitWorld() {
        super("visitworld", "");
        setPermissionRequired(false);
        setArgumentRequired(false);
        setAutonomous(true);
    }

    @Override
    public void callCommand(CommandSender sender, String[] args) {
            World world = Bukkit.getWorld(args[0]);
            ((Player) sender).teleport(world.getSpawnLocation());
    }

    @Override
    public List<String> callCompletionsUpdate(CommandSender sender, String[] args) {
        return List.of();
    }
}
