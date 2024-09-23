package me.robomonkey.versus.duel.command;

import me.robomonkey.versus.command.AbstractCommand;
import me.robomonkey.versus.duel.DuelManager;
import me.robomonkey.versus.duel.request.RequestManager;
import me.robomonkey.versus.settings.Error;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class AcceptCommand extends AbstractCommand {

    public AcceptCommand() {
        super("accept", "versus.duel");
        setPlayersOnly(true);
        setPermissionRequired(false);
        setArgumentRequired(false);
    }

    @Override
    public void callCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        RequestManager requestManager = RequestManager.getInstance();
        if (DuelManager.getInstance().isDueling(player)) {
            error(sender, Error.CANNOT_DUEL);
            return;
        }
        if (!requestManager.hasIncomingRequest(player)) {
            error(sender, Error.NO_INCOMING_REQUESTS);
            return;
        }
        try {
            requestManager.acceptRequest(player);
        } catch (RequestManager.PlayerOfflineException e) {
            error(player, Error.PLAYER_NO_LONGER_ONLINE);
        }
    }

    @Override
    public List<String> callCompletionsUpdate(CommandSender sender, String[] args) {
        return null;
    }
}

