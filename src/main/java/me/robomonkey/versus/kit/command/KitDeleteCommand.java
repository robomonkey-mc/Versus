package me.robomonkey.versus.kit.command;

import me.robomonkey.versus.command.AbstractCommand;
import me.robomonkey.versus.kit.Kit;
import me.robomonkey.versus.kit.KitManager;
import me.robomonkey.versus.util.MessageUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class KitDeleteCommand extends AbstractCommand {

    KitManager kitManager = KitManager.getInstance();

    public KitDeleteCommand() {
        super("deletekit", "versus.kit.delete");
        setUsage("/arena deletekit <name>");
        setDescription("Deletes a kit of a given name.");
    }

    @Override
    public void callCommand(CommandSender sender, String[] args) {
        if (args.length < 1) {
            error(sender, "Please specify the kit to delete.");
            return;
        }
        String kitName = args[0];
        if (kitManager.contains(kitName)) {
            kitManager.remove(kitName);
            sender.sendMessage(MessageUtil.get("&pYou deleted &h" + kitName + "&p."));
        } else {
            error(sender, "No kit named " + kitName + " exists.");
        }
    }

    @Override
    public List<String> callCompletionsUpdate(CommandSender sender, String[] args) {
        return kitManager.getAllKits().stream().map(Kit::getName).collect(Collectors.toList());
    }
}
