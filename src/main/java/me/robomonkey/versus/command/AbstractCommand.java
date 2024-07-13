package me.robomonkey.versus.command;

import org.bukkit.command.CommandSender;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractCommand {

    String command;
    Set<AbstractCommand> branches = new HashSet<>();
    String permission;

    private List<String> additionalCompletions = new ArrayList<>();

    public AbstractCommand(String permission, String name){
        this.permission = permission;
        this.command = name;
    }

    public AbstractCommand(String permission, String name, AbstractCommand... branches){
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

    public List<String> getCompletions() {
        return additionalCompletions;
    }

    Set<AbstractCommand> getBranches() {
        return branches;
    }

    public boolean isLeaf(){
        return this.branches.size() == 0;
    }

    AbstractCommand getBranchFromName(String name){
        for(AbstractCommand command: this.getBranches()){
            if(command.getCommand().equalsIgnoreCase(name)){
                return command;
            }
        }
        return null;
    }

    List<String> getStringBranches(){
        List<String> stringBranches = new ArrayList<>();
        branches.stream().forEach(branch -> stringBranches.add(branch.getCommand()));
        return stringBranches;
    }

    void setBranches(Set<AbstractCommand> commandBranches) {
        branches = commandBranches;
    }

    void dispatchToBranch(AbstractCommand branch, CommandSender sender, String[] args){
        branch.callCommand(sender, Arrays.copyOfRange(args,1, args.length));
    }

    void addTabCompletion(String completion){
        this.additionalCompletions.add(completion);
    }

    void addTabCompletions(List<String> completions){
        completions.forEach(completion -> additionalCompletions.add(completion));
    }

    List<String> getTabCompletions(CommandSender sender){
        List<String> allowedBranchCompletions = getBranches().stream()
                .filter(branch -> sender.hasPermission(branch.getPermission()))
                .map(branch -> branch.getCommand())
                .collect(Collectors.toList());
        if(sender.hasPermission(this.getPermission())) allowedBranchCompletions.addAll(additionalCompletions);
        return allowedBranchCompletions;
    }

    public abstract boolean callCommand(CommandSender commandSender, String[] args);
}
