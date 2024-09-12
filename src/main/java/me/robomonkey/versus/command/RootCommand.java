package me.robomonkey.versus.command;

import me.robomonkey.versus.Versus;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public abstract class RootCommand extends AbstractCommand implements CommandExecutor, TabCompleter {

    public RootCommand(String command, String permission) {
        super(command, permission);
        this.registerCommand();
    }

    private void registerCommand() {
        Versus.getInstance().getCommand(command).setExecutor(this);
        Versus.getInstance().getCommand(command).setTabCompleter(this);
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



