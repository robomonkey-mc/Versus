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

public class CreateWorld extends RootCommand {
    public CreateWorld() {
        super("createworld", "");
        setPermissionRequired(false);
        setArgumentRequired(false);
        setAutonomous(true);
    }

    @Override
    public void callCommand(CommandSender sender, String[] args) {
            WorldCreator worldCreator = new WorldCreator("World"+(Bukkit.getWorlds().size()+1));
            worldCreator.environment(World.Environment.NORMAL)
                    .type(WorldType.NORMAL)
                    .generateStructures(true);
            World newWorld = Bukkit.createWorld(worldCreator);
            if (newWorld != null) {
                Versus.log("Custom world generated successfully!");
                Player player = (Player) sender;
                player.teleport(newWorld.getSpawnLocation());
            } else {
                Versus.log("Failed to generate custom world.");
            }
    }

    @Override
    public List<String> callCompletionsUpdate(CommandSender sender, String[] args) {
        return List.of();
    }
}
