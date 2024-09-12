package me.robomonkey.versus.command;

import me.robomonkey.versus.settings.Setting;
import me.robomonkey.versus.settings.Settings;
import me.robomonkey.versus.util.MessageUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractCommand {
    public static String errorPrefix = Settings.getMessage(Setting.ERROR_PREFIX);
    public static String permissionErrorMessage = errorPrefix + Settings.getMessage(Setting.NO_PERMISSION_MESSAGE);
    public static String improperSenderErrorMessage = errorPrefix + Settings.getMessage(Setting.ONLY_PLAYERS_MESSAGE);
    String command;
    String permission;
    private String usage = "";
    private String description = "";
    private Set<AbstractCommand> branches = new HashSet<>();
    boolean playersOnly = false;
    boolean staticTabComplete = false;
    private List<String> additionalCompletions = new ArrayList<>();
    private boolean permissionRequired = true;
    private boolean argumentRequired = true;
    private int maxArguments = -1;
    private int minArguments = 0;
    private boolean autonomous = false;

    public AbstractCommand(String name, String permission) {
        this.permission = permission;
        this.command = name;
    }

    public AbstractCommand(String name, String permission, AbstractCommand... branches) {
        this.permission = permission;
        this.command = name;
        Arrays.stream(branches).forEach(branch -> this.branches.add(branch));
    }

    public static void error(CommandSender sender, String message) {
        String errorPrefix = Settings.getMessage(Setting.ERROR_PREFIX);
        sender.sendMessage(errorPrefix + message);
    }

    public String getCommand() {
        return command;
    }

    public boolean isAutonomous() {
        return autonomous;
    }

    public String getDescription() {
        return this.description;
    }

    public void setMinArguments(int minimum) {
        minArguments = minimum;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAutonomous(Boolean autonomous) {
        this.autonomous = autonomous;
    }

    public String getPermission() {
        return permission;
    }

    public int getMaxArguments() {
        return maxArguments;
    }

    public String getUsage() {
        return this.usage;
    }

    public boolean isPlayersOnly() {
        return this.playersOnly;
    }

    public boolean isArgumentRequired() {
        return argumentRequired;
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

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public void enforcePermissionRulesForChildren() {
        branches.forEach(branch -> {
            branch.setPermissionRequired(this.permissionRequired);
            branch.enforcePermissionRulesForChildren();
        });
    }

    public void enforcePermissionForChildren() {
        branches.forEach(branch -> {
            branch.setPermission(this.permission);
            branch.enforcePermissionForChildren();
        });
    }

    public void addTabCompletion(String completion) {
        this.additionalCompletions.add(completion);
    }

    public void addTabCompletions(List<String> completions) {
        additionalCompletions.addAll(completions);
    }

    public void setTabCompletions(List<String> completions) {
        additionalCompletions = completions;
    }

    public void setPlayersOnly(Boolean playersOnly) {
        this.playersOnly = playersOnly;
    }

    public void setArgumentRequired(boolean argumentRequired) {
        this.argumentRequired = argumentRequired;
    }

    /**
     * Sets the maximum number of arguments for tab completion. -1 is the default value and will
     * allow infinite arguments.
     */
    public void setMaxArguments(int maxArguments) {
        this.maxArguments = maxArguments;
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

    private boolean shouldTabComplete(String[] args) {
        return maxArguments == -1 || args.length <= maxArguments;
    }

    private String getHelpFromChildren() {
        String help = "";
        Iterator<AbstractCommand> branchIter = getBranches().iterator();
        while (branchIter.hasNext()) {
            AbstractCommand branch = branchIter.next();
            help = help + MessageUtil.color("&p" + branch.getUsage() + ":&s " + branch.getDescription()) + "\n";
        }
        return MessageUtil.LINE + "\n" + help + MessageUtil.LINE;
    }

    String getHelp() {
        if (isLeaf() || isAutonomous()) {
            return MessageUtil.color("&p" + getUsage() + ":&s " + getDescription());
        } else {
            return getHelpFromChildren();
        }
    }

    List<String> getBuiltinCompletionOptions(CommandSender sender) {
        List<String> allowedCompletions = getBranches().stream()
                .filter(branch -> !branch.isPermissionRequired() || sender.hasPermission(branch.getPermission()))
                .map(branch -> branch.getCommand())
                .collect(Collectors.toList());
        if (sender.hasPermission(this.getPermission())) allowedCompletions.addAll(additionalCompletions);
        return allowedCompletions;
    }

    List<String> dispatchTabCompleter(CommandSender sender, String[] args) {
        List<String> emptyList = new ArrayList<>();
        List<String> completions = getBuiltinCompletionOptions(sender);
        List<String> additionalCompletions = callCompletionsUpdate(sender, args);
        if (additionalCompletions != null) {
            completions.addAll(additionalCompletions);
        }
        if (args.length == 1 || isLeaf()) {
            if (!shouldTabComplete(args)) return emptyList;
            String lastArg = args[args.length - 1];
            if (staticTabComplete) return completions;
            completions = completions.stream()
                    .filter(completion -> completion.toLowerCase().contains(lastArg.toLowerCase()))
                    .collect(Collectors.toList());
            return completions;
        }
        AbstractCommand potentialBranch = getBranchFromName(args[0]);
        if (args.length > 1 && potentialBranch != null) {
            String[] rest = Arrays.copyOfRange(args, 1, args.length);
            List<String> childCompletions = potentialBranch.dispatchTabCompleter(sender, rest);
            return childCompletions;
        }
        return emptyList;
    }

    public String buildArgs(String[] args, int startInclusive, int endExclusive) {
        StringBuilder argsBuilder = new StringBuilder();
        for (int index = startInclusive; index < endExclusive; index++) {
            String nextPart = args[index];
            argsBuilder.append(nextPart);
            if (index != endExclusive - 1) {
                argsBuilder.append(" ");
            }
        }
        return argsBuilder.toString();
    }

    void dispatchCommand(CommandSender sender, String[] args) {
        if (permissionRequired && !sender.hasPermission(permission)) {
            sender.sendMessage(permissionErrorMessage);
            return;
        }
        if (isPlayersOnly() && !(sender instanceof Player)) {
            sender.sendMessage(improperSenderErrorMessage);
            return;
        }
        if (args.length == 0 && !argumentRequired) {
            callCommand(sender, args);
            return;
        } else if (args.length == 0) {
            sender.sendMessage(getHelp());
            return;
        } else if (args.length < minArguments) {
            sender.sendMessage(getHelp());
            return;
        }
        String firstArg = args[0];
        if (isLeaf()) {
            callCommand(sender, args);
            return;
        }
        AbstractCommand branchFromName = getBranchFromName(firstArg);
        if (branchFromName == null) {
            if (autonomous) callCommand(sender, args);
            else error(sender, firstArg + " is not a command. Type /" + command + " for help.");
        } else {
            branchFromName.dispatchCommand(sender, Arrays.copyOfRange(args, 1, args.length));
        }
    }

    /**
     * <h1>Calls this specific subcommand</h1>
     * <p>This only includes relevant sub arguments. Don't worry about handling permissions
     * or dispatching to subcommands. That is already handled by the AbstractCommand superclass</p>
     */
    public abstract void callCommand(CommandSender sender, String[] args);

    /**
     * <h1>Notifies all commands that a tab completions update has occured.</h1>
     * <p>Upon recieving the update, child classes can run setTabCompletions() or addTabCompletion() to update
     * if necessary.</p>
     */
    public abstract List<String> callCompletionsUpdate(CommandSender sender, String[] args);


}