package me.robomonkey.versus.command;

import me.robomonkey.versus.Versus;
import org.bukkit.command.CommandSender;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractCommand {

    String command;
    private String usage;
    private Set<AbstractCommand> branches = new HashSet<>();
    String permission;
    private List<String> additionalCompletions = new ArrayList<>();
    public static String permissionErrorMessage = Versus.color("&c&lError: &4You do not have permission to use this command!");

    public AbstractCommand(String name, String permission) {
        this.permission = permission;
        this.command = name;
    }

    public AbstractCommand(String name, String permission, AbstractCommand... branches) {
        this.permission = permission;
        this.command = name;
        Arrays.stream(branches).forEach(branch -> this.branches.add(branch));
    }

    public String getCommand() {
        return command;
    }

    public String getPermission() {
        return permission;
    }

    public String getUsage() {
        return this.usage;
    }

    public List<String> getCompletions() {
        return additionalCompletions;
    }

    public Set<AbstractCommand> getBranches() {
        return branches;
    }

    public boolean isLeaf() {
        return this.branches.size() == 0;
    }

    public AbstractCommand getBranchFromName(String name) {
        for (AbstractCommand command : this.getBranches()) {
            if (command.getCommand().equalsIgnoreCase(name)) {
                return command;
            }
        }
        return null;
    }

    public List<String> getStringBranches() {
        List<String> stringBranches = new ArrayList<>();
        branches.stream().forEach(branch -> stringBranches.add(branch.getCommand()));
        return stringBranches;
    }

    public void setBranches(Set<AbstractCommand> commandBranches) {
        branches = commandBranches;
    }

    public void addBranches(AbstractCommand... newBranch) {
        branches.addAll(Arrays.asList(newBranch));
    }

    public void addTabCompletion(String completion) {
        this.additionalCompletions.add(completion);
    }

    public void addTabCompletions(List<String> completions) {
        completions.forEach(completion -> additionalCompletions.add(completion));
    }

    public void setTabCompletions(List<String> completions) {
        additionalCompletions = completions;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    List<String> getCompletionOptions(CommandSender sender) {
        List<String> allowedBranchCompletions = getBranches().stream()
                .filter(branch -> sender.hasPermission(branch.getPermission()))
                .map(branch -> branch.getCommand())
                .collect(Collectors.toList());
        if (sender.hasPermission(this.getPermission())) allowedBranchCompletions.addAll(additionalCompletions);
        return allowedBranchCompletions;
    }

    List<String> dispatchTabCompleter(CommandSender sender, String[] args) {
        List<String> completions = getCompletionOptions(sender);
        if (args.length == 1) {
            for (String s : args) {
                if (s.toLowerCase().startsWith(args[0].toLowerCase())) completions.add(s);
            }
            return completions;
        }
        if (args.length > 1) {
            String[] rest = Arrays.copyOfRange(args, 1, args.length);
            AbstractCommand selectedChild = getBranchFromName(args[0]);
            List<String> childCompletions = selectedChild.dispatchTabCompleter(sender, rest);
            return childCompletions;
        }
        List<String> emptyList = new ArrayList<>();
        return emptyList;
    }

    public void dispatchCommand(CommandSender sender, String[] args){
        String firstArg = (args.length > 0)? args[0]: "";
        AbstractCommand branchFromName = getBranchFromName(firstArg);
        if (branchFromName == null) {
            if(sender.hasPermission(permission)) callCommand(sender, args);
            else sender.sendMessage(permissionErrorMessage);
        } else {
            branchFromName.callCommand(sender, Arrays.copyOfRange(args, 1, args.length));
        }
    }

    /**
     <h1>Calls this specific subcommand</h1>
     <p>This only includes relevant sub arguments. Don't worry about handling permissions
     or dispatching to subcommands. That is already handled by the AbstractCommand superclass</p>
     */
    public abstract boolean callCommand(CommandSender sender, String[] args);
}
