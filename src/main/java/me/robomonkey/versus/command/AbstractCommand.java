package me.robomonkey.versus.command;

import me.robomonkey.versus.Versus;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractCommand {
    public static String permissionErrorMessage = Versus.color("&c&lError: &4You do not have permission to use this command!");
    public static String improperSenderErrorMessage = Versus.color("&c&lError: &4Only players can use this command!");
    String command;
    String permission;
    private String usage;
    private Set<AbstractCommand> branches = new HashSet<>();
    boolean playersOnly = false;
    boolean staticTabComplete = false;
    private List<String> additionalCompletions = new ArrayList<>();
    private boolean permissionRequired;

    public AbstractCommand(String name, String permission) {
        this.permission = permission;
        this.command = name;
    }

    public AbstractCommand(String name, String permission, AbstractCommand... branches) {
        this.permission = permission;
        this.command = name;
        Arrays.stream(branches).forEach(branch -> this.branches.add(branch));
    }

    public void error(CommandSender sender, String message) {
        sender.sendMessage(Versus.color("&c&lError: &4"+message));
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

    public boolean isPlayersOnly() {
        return this.playersOnly;
    }

    public void setPermissionRequired(boolean required) {
        this.permissionRequired = required;
    }

    public boolean isPermissionRequired() {
        return permissionRequired;
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

    public void setPlayersOnly(Boolean playersOnly) {
        this.playersOnly = playersOnly;
    }

    public boolean isStaticTabComplete() {
        return staticTabComplete;
    }

    public void setStaticTabComplete(boolean staticTabComplete) {
        this.staticTabComplete = staticTabComplete;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    List<String> getCompletionOptions(CommandSender sender) {
        List<String> allowedCompletions = getBranches().stream()
                .filter(branch -> sender.hasPermission(branch.getPermission()))
                .map(branch -> branch.getCommand())
                .collect(Collectors.toList());
        if (sender.hasPermission(this.getPermission())) allowedCompletions.addAll(additionalCompletions);
        return allowedCompletions;
    }

    List<String> dispatchTabCompleter(CommandSender sender, String[] args) {
        callCompletionsUpdate(sender, Arrays.copyOfRange(args, 1, args.length));
        List<String> completions = getCompletionOptions(sender);
        if (args.length == 1) {
            if(staticTabComplete) return completions;
            for (String s : args) {
                if (s.toLowerCase().startsWith(args[0].toLowerCase())) completions.add(s);
            }
            return completions;
        }
        AbstractCommand potentialBranch = getBranchFromName(args[0]);
        if (args.length > 1 && potentialBranch != null) {
            String[] rest = Arrays.copyOfRange(args, 1, args.length);
            List<String> childCompletions = potentialBranch.dispatchTabCompleter(sender, rest);
            return childCompletions;
        }
        List<String> emptyList = new ArrayList<>();
        return emptyList;
    }

    public void dispatchCommand(CommandSender sender, String[] args){
        String firstArg = (args.length > 0)? args[0]: "";
        AbstractCommand branchFromName = getBranchFromName(firstArg);
        if (isLeaf() || branchFromName == null) {
            if(isPlayersOnly() && !(sender instanceof Player)){
                sender.sendMessage(improperSenderErrorMessage);
                return;
            }
            if(permissionRequired && !sender.hasPermission(permission)){
                sender.sendMessage(permissionErrorMessage);
                return;
            }
            callCommand(sender, args);
        } else {
            branchFromName.dispatchCommand(sender, Arrays.copyOfRange(args, 1, args.length));
        }
    }

    /**
     <h1>Calls this specific subcommand</h1>
     <p>This only includes relevant sub arguments. Don't worry about handling permissions
     or dispatching to subcommands. That is already handled by the AbstractCommand superclass</p>
     */
    public abstract void callCommand(CommandSender sender, String[] args);

    /**
     <h1>Notifies all commands that a tab completions update has occured.</h1>
     <p>Upon recieving the update, child classes can run setTabCompletions() or addTabCompletion() to update
     if necessary.</p>
     */
    public abstract void callCompletionsUpdate(CommandSender sender, String[] args);
}
