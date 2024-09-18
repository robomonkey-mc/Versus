package me.robomonkey.versus.duel.command;

import me.robomonkey.versus.command.RootCommand;
import me.robomonkey.versus.duel.DuelManager;
import me.robomonkey.versus.duel.request.RequestManager;
import me.robomonkey.versus.settings.Error;
import me.robomonkey.versus.settings.*;
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
            error(sender, Lang.get(Error.IS_NOT_ONLINE,
                Placeholder.of("%player%", playerNameRequested)));
            return;
        }
        if (requested.equals(player)) {
            error(sender, Lang.get(Error.DUEL_SELF));
            return;
        }
        if (DuelManager.getInstance().isDueling(player)) {
            error(sender, Lang.get(Error.CANNOT_DUEL));
            return;
        }
        if (DuelManager.getInstance().isDueling(requested) || requestManager.isQueued(requested)) {
            error(sender, Error.CANNOT_DUEL);
            return;
        }
        if (requestManager.hasIncomingRequest(player)
                && requestManager.isRequestedBy(requested, player)) {
            try {
                RequestManager.getInstance().acceptSpecificRequest(player, requested);
            } catch (RequestManager.PlayerOfflineException e) {
                error(player, Error.PLAYER_NO_LONGER_ONLINE);
            }
            return;
        }
        if (requestManager.isQueued(player)) {
            error(sender, Error.DUEL_WHILE_QUEUEING);
            return;
        }
        if (requestManager.isRequestedBy(player, requested)) {
            error(sender, Error.WAIT_FOR_RESPONSE);
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
