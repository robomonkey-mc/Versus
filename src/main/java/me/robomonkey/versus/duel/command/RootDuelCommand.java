package me.robomonkey.versus.duel.command;

import me.robomonkey.versus.command.AbstractCommand;
import me.robomonkey.versus.command.RootCommand;
import me.robomonkey.versus.duel.RequestManager;
import me.robomonkey.versus.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.PlayerNamePrompt;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class RootDuelCommand extends RootCommand {

    public RootDuelCommand() {
        super("duel", "duel.general");
        setPermissionRequired(false);
        setPlayersOnly(true);
        setUsage("/duel <player>");
        addBranches(new DenyCommand());
    }

    @Override
    public void callCommand(CommandSender sender, String[] args) {
        RequestManager requestManager = RequestManager.getInstance();
        Player player = (Player) sender;
        String playerNameRequested = args[0];
        Player requested = Bukkit.getPlayer(playerNameRequested);
        if(requested.equals(player)) {
            error(sender, "You cannot duel yourself.");
            return;
        }
        if(requested == null) {
            error(sender, playerNameRequested+" is not online.");
            return;
        }
        if(requestManager.hasIncomingRequest(player)
                && requestManager.getRequester(player).equals(requested.getUniqueId())) {
           tryAcceptRequest(player);
           return;
        }
        requestManager.sendRequest(player, requested);
    }

    public void tryAcceptRequest(Player player) {
        try {
            RequestManager.getInstance().acceptRequest(player);
        } catch (RequestManager.PlayerOfflineException e) {
            error(player, "The player that requested a duel is no longer online!");
        }
    }

    @Override
    public List<String> callCompletionsUpdate(CommandSender sender, String[] args) {
        return Bukkit.getOnlinePlayers()
                .stream()
                .map(player -> player.getName())
                .collect(Collectors.toList());
    }
}
