package me.robomonkey.versus.duel.command;

import me.robomonkey.versus.command.AbstractCommand;
import me.robomonkey.versus.duel.RequestManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.optional.qual.OptionalBottom;

import javax.annotation.Nullable;
import java.util.List;

public class DenyCommand extends AbstractCommand {

    public DenyCommand() {
        super("deny","duel.deny");
        setPlayersOnly(true);
        setPermissionRequired(false);
    }

    @Override
    public void callCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        RequestManager requestManager = RequestManager.getInstance();
        if(!requestManager.hasIncomingRequest(player)){
            error(sender,"You currently have no incoming requests.");
        }
        requestManager.denyRequest(player);
    }

    @Override
    public List<String> callCompletionsUpdate(CommandSender sender, String[] args) {
        return null;
    }
}
