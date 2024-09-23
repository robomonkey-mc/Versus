package me.robomonkey.versus.command;

import me.robomonkey.versus.Versus;
import me.robomonkey.versus.settings.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.*;

import java.lang.reflect.Field;
import java.util.List;

public abstract class RootCommand extends AbstractCommand implements CommandExecutor, TabCompleter {

    public RootCommand(String command, String permission) {
        super(command, permission);
        if(Lang.has(this)) {
            rename();
            loadFromYAML();
        }
        this.registerCommand();
    }

    private void rename() {
        String newName = Lang.of(this).get("name");
        if(newName.equals(this.getOriginalCommand())) return;
        Command command = Bukkit.getPluginCommand(originalCommand);
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            SimpleCommandMap commandMap = (SimpleCommandMap) commandMapField.get(Bukkit.getServer());
            commandMap.register(newName, "versus", command);
            Versus.log("Added alias to the command /"+getOriginalCommand()+": /"+newName+".");
        } catch (Exception e) {
            Versus.error("Failed to add custom alias to the command '"+command.getName()+"'.");
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



