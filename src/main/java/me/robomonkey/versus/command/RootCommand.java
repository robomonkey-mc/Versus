package me.robomonkey.versus.command;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public abstract class RootCommand extends AbstractCommand implements CommandExecutor, TabCompleter {

    public RootCommand(String command, String permission) {
        super(command, permission);
        this.command = command;
        this.permission = permission;
    }

    public RootCommand(String command, String permission, List<String> tabCompletions) {
        super(command, permission);
        this.permission = permission;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        String firstArg = args[0];
        AbstractCommand branchFromName = getBranchFromName(firstArg);
        if(branchFromName == null) {
            callCommand(sender, args);
            return true;
        } else {
            dispatchToBranch(branchFromName, sender, args);
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, AbstractCommand command, String s, String[] args) {
        List<String> completions = getTabCompletions(sender);
        if(args.length==1) {
            for(String s: arguments) {
                if(s.toLowerCase().startsWith(args[0].toLowerCase())) flist.add(s);
            }
            for(Player p: Bukkit.getOnlinePlayers()) {
                if(!Main.isVanished(p)&&p.getName().toLowerCase().startsWith(args[0].toLowerCase())) flist.add(p.getName());
            }
            return flist;
        }
        if(args.length==2){

        }
        return null;
    }

