package me.robomonkey.versus.duel.command;

import me.robomonkey.versus.command.RootCommand;
import me.robomonkey.versus.duel.DuelManager;
import me.robomonkey.versus.duel.request.RequestManager;
import me.robomonkey.versus.settings.Setting;
import me.robomonkey.versus.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class RootDuelCommand extends RootCommand {

    public RootDuelCommand() {
        super("duel", "versus.duel");
        setPermissionRequired(Settings.is(Setting.PERMISSION_REQUIRED_TO_DUEL));
        setPlayersOnly(true);
        setArgumentRequired(true);
        setUsage("/duel <player>");
        setDescription("Sends a duel request.");
        addBranches(new DenyCommand(),
                new CancelCommand(),
                new AcceptCommand());
        setAutonomous(true);
        enforcePermissionRulesForChildren();
    }

    @Override
    public void callCommand(CommandSender sender, String[] args) {
        RequestManager requestManager = RequestManager.getInstance();
        Player player = (Player) sender;
        String playerNameRequested = args[0];
        Player requested = Bukkit.getPlayer(playerNameRequested);
        if (requested == null) {
            error(sender, "'" + playerNameRequested + "' is not online.");
            return;
        }
        if (requested.equals(player)) {
            error(sender, "You cannot duel yourself.");
            return;
        }
        if (DuelManager.getInstance().isDueling(player)) {
            error(sender, "You cannot duel right now.");
            return;
        }
        if (DuelManager.getInstance().isDueling(requested) || requestManager.isQueued(requested)) {
            error(sender, requested.getName() + " cannot duel right now.");
            return;
        }
        if (requestManager.hasIncomingRequest(player)
                && requestManager.isRequestedBy(requested, player)) {
            try {
                RequestManager.getInstance().acceptSpecificRequest(player, requested);
            } catch (RequestManager.PlayerOfflineException e) {
                error(player, "The player that requested a duel is no longer online!");
            }
            return;
        }
        if (requestManager.isQueued(player)) {
            error(sender, "You cannot send duel requests while queueing for a duel. Type /duel cancel to quit the queue.");
            return;
        }
        if (requestManager.isRequestedBy(player, requested)) {
            error(sender, "Please wait for " + requested.getName() + " to respond to your first request.");
            return;
        }
        requestManager.sendRequest(player, requested);
    }

    @Override
    public List<String> callCompletionsUpdate(CommandSender sender, String[] args) {
        return Bukkit.getOnlinePlayers()
                .stream()
                .map(player -> player.getName())
                .collect(Collectors.toList());
    }
}
