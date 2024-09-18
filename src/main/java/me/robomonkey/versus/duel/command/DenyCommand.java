package me.robomonkey.versus.duel.command;

import me.robomonkey.versus.command.AbstractCommand;
import me.robomonkey.versus.duel.request.RequestManager;
import me.robomonkey.versus.settings.Error;
import me.robomonkey.versus.settings.Lang;
import me.robomonkey.versus.settings.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class DenyCommand extends AbstractCommand {

    public DenyCommand() {
        super("deny", "versus.duel");
        setPlayersOnly(true);
        setPermissionRequired(false);
        setArgumentRequired(true);
    }

    @Override
    public void callCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        RequestManager requestManager = RequestManager.getInstance();
        if (!requestManager.hasIncomingRequest(player)) {
            error(sender, Lang.get(Error.NO_INCOMING_REQUESTS));
            return;
        }
        if (args.length < 1) {
            error(sender, Lang.get(Error.SPECIFY_A_PLAYER));
            return;
        }
        String playername = args[0];
        Player requester = Bukkit.getPlayer(playername);
        if (requester == null) {
            error(sender, Error.PLAYER_NO_LONGER_ONLINE, playername);
            return;
        }
        if (requester.equals(player)) {
            error(sender, Error.DUEL_SELF);
            return;
        }
        if (requestManager.getRequest(player, requester) == null) {
            error(sender, Error.NOT_RECIEVED_REQUEST);
            return;
        }
        requestManager.denyRequest(player, requester);
    }


    @Override
    public List<String> callCompletionsUpdate(CommandSender sender, String[] args) {
        return Bukkit.getOnlinePlayers()
                .stream()
                .map(player -> player.getName())
                .collect(Collectors.toList());
    }
}
