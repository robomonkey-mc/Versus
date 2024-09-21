package me.robomonkey.versus.command;

import me.robomonkey.versus.Versus;
import me.robomonkey.versus.settings.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public abstract class RootCommand extends AbstractCommand implements CommandExecutor, TabCompleter {

    public RootCommand(String command, String permission) {
        super(command, permission);
        if(Lang.has(this)) {
            reflectChangeCommandName();
            loadFromYAML();
        }
        this.registerCommand();
    }

    private void reflectChangeCommandName() {
        String newName = Lang.of(this).get("name");
        Command command = Bukkit.getPluginCommand(originalCommand);
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            SimpleCommandMap commandMap = (SimpleCommandMap) commandMapField.get(Bukkit.getServer());
            commandMap.register(newName, "versus", command);
        } catch (Exception e) {
            Versus.error("Failed to add custom name to "+command.getName()+".");
        }
    }

    private void registerCommand() {
        Versus.getInstance().getCommand(originalCommand).setExecutor(this);
        Versus.getInstance().getCommand(originalCommand).setTabCompleter(this);
    }

    public RootCommand(String command, String permission, List<String> tabCompletions) {
        super(command, permission);
        this.permission = permission;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        dispatchCommand(sender, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return dispatchTabCompleter(sender, args);
    }
}



