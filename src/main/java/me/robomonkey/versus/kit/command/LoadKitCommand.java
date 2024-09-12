package me.robomonkey.versus.kit.command;

import me.robomonkey.versus.command.AbstractCommand;
import me.robomonkey.versus.kit.Kit;
import me.robomonkey.versus.kit.KitManager;
import me.robomonkey.versus.util.MessageUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class LoadKitCommand extends AbstractCommand {
    KitManager kitManager = KitManager.getInstance();

    public LoadKitCommand() {
        super("loadkit", "versus.kit.load");
        setUsage("/arena loadkit <name>");
        setDescription("Loads a kit into player inventory.");
    }

    @Override
    public void callCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (args.length < 1) {
            error(sender, "Please specify the kit to load.");
            return;
        }
        String kitName = args[0];
        if (kitManager.contains(kitName)) {
            ItemStack[] contents = kitManager.getKit(kitName).getItems();
            player.getInventory().setContents(contents);
            sender.sendMessage(MessageUtil.get("&pYou loaded &h" + kitName + "&p."));
        } else {
            error(sender, "No kit named " + kitName + " exists.");
        }
    }

    @Override
    public List<String> callCompletionsUpdate(CommandSender sender, String[] args) {
        return kitManager.getAllKits().stream().map(Kit::getName).collect(Collectors.toList());
    }
}
