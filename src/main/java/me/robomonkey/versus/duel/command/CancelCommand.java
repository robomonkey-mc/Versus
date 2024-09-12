package me.robomonkey.versus.duel.command;

import me.robomonkey.versus.command.AbstractCommand;
import me.robomonkey.versus.duel.request.Request;
import me.robomonkey.versus.duel.request.RequestManager;
import me.robomonkey.versus.settings.Setting;
import me.robomonkey.versus.settings.Settings;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CancelCommand extends AbstractCommand {

    public CancelCommand() {
        super("cancel", "versus.duel");
        setArgumentRequired(false);
        setUsage("/duel cancel");
        setPermissionRequired(false);
        setPlayersOnly(true);
        setDescription("Cancels a request to duel.");
    }

    @Override
    public void callCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (!RequestManager.getInstance().isQueued(player)) {
            error(sender, "You're not currently queueing for a duel.");
            return;
        }
        ;
        Request request = RequestManager.getInstance().getQueuedRequest(player);
        Player requestingPlayer = request.getRequestingPlayer();
        Player requestedPlayer = request.getRequestedPlayer();
        RequestManager.getInstance().cancelRequest(request);
        if (requestingPlayer != null) requestingPlayer.sendMessage(Settings.getMessage(Setting.CANCEL_REQUEST));
        if (requestedPlayer != null) requestedPlayer.sendMessage(Settings.getMessage(Setting.CANCEL_REQUEST));
    }

    @Override
    public List<String> callCompletionsUpdate(CommandSender sender, String[] args) {
        return List.of();
    }
}
