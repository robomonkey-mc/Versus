package me.robomonkey.versus.kit.command;

import me.robomonkey.versus.command.AbstractCommand;
import me.robomonkey.versus.kit.KitManager;
import me.robomonkey.versus.util.MessageUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SaveKitCommand extends AbstractCommand {
    public SaveKitCommand() {
        super("savekit", "versus.kit.save");
        setPlayersOnly(true);
        setStaticTabComplete(true);
        this.setMaxArguments(1);
        this.setArgumentRequired(true);
        this.addTabCompletion("<kit-name>");
        setUsage("/arena savekit <name>");
        setDescription("Saves a kit with a given name.");
    }

    @Override
    public void callCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (args.length < 1) {
            error(sender, "Please provide a name for the kit.");
            return;
        }

        String kitName = args[0];
        if (KitManager.getInstance().isKit(kitName)) {
            error(sender, "'" + kitName + "' already exists.");
            return;
        }
        KitManager.getInstance().add(kitName, player.getInventory().getContents());
        sender.sendMessage(MessageUtil.get("&pYou successfully saved &h" + kitName + "&p. Use &h/arena set&p to bind to an arena."));
    }

    @Override
    public List<String> callCompletionsUpdate(CommandSender sender, String[] args) {
        return List.of();
    }
}
