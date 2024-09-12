package me.robomonkey.versus.duel.command;

import me.robomonkey.versus.command.AbstractCommand;
import me.robomonkey.versus.duel.DuelManager;
import me.robomonkey.versus.duel.request.RequestManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class AcceptCommand extends AbstractCommand {

    public AcceptCommand() {
        super("accept", "versus.duel");
        setUsage("/duel accept");
        setPlayersOnly(true);
        setPermissionRequired(false);
        setArgumentRequired(false);
        setDescription("Accepts the most recent request to duel.");
    }

    @Override
    public void callCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        RequestManager requestManager = RequestManager.getInstance();
        if (DuelManager.getInstance().isDueling(player)) {
            error(sender, "You cannot duel right now.");
            return;
        }
        if (!requestManager.hasIncomingRequest(player)) {
            error(sender, "You currently have no incoming requests.");
            return;
        }
        try {
            requestManager.acceptRequest(player);
        } catch (RequestManager.PlayerOfflineException e) {
            error(player, "The player that requested a duel is no longer online!");
        }
    }

    @Override
    public List<String> callCompletionsUpdate(CommandSender sender, String[] args) {
        return null;
    }
}

