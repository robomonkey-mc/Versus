package me.robomonkey.versus.duel.command;

import me.robomonkey.versus.command.AbstractCommand;
import me.robomonkey.versus.duel.request.RequestManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class DenyCommand extends AbstractCommand {

    public DenyCommand() {
        super("deny","versus.duel");
        setPlayersOnly(true);
        setPermissionRequired(false);
        setArgumentRequired(false);
    }

    @Override
    public void callCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        RequestManager requestManager = RequestManager.getInstance();
        if(!requestManager.hasIncomingRequest(player)){
            error(sender,"You currently have no incoming requests.");
            return;
        }
        requestManager.denyRequest(player);
    }

    @Override
    public List<String> callCompletionsUpdate(CommandSender sender, String[] args) {
        return null;
    }
}
