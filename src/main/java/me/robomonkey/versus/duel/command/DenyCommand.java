package me.robomonkey.versus.duel.command;

import me.robomonkey.versus.command.AbstractCommand;
import me.robomonkey.versus.duel.request.RequestManager;
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
        setUsage("/duel deny <player>");
        setDescription("Denies a player's request to duel.");
    }

    @Override
    public void callCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        RequestManager requestManager = RequestManager.getInstance();
        if (!requestManager.hasIncomingRequest(player)) {
            error(sender, "You currently have no incoming requests.");
            return;
        }
        if (args.length < 1) {
            error(sender, "You must specify a player whose request you want to deny.");
            return;
        }
        String playername = args[0];
        Player requester = Bukkit.getPlayer(playername);
        if (requester == null) {
            error(sender, "'" + playername + "' is not online.");
            return;
        }
        if (requester.equals(player)) {
            error(sender, "You cannot duel yourself.");
            return;
        }
        if (requestManager.getRequest(player, requester) == null) {
            error(sender, "You have not received a request from " + requester.getName() + ".");
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
