package me.robomonkey.versus.duel.command;

import me.robomonkey.versus.command.RootCommand;
import me.robomonkey.versus.duel.Duel;
import me.robomonkey.versus.duel.DuelManager;
import me.robomonkey.versus.settings.Setting;
import me.robomonkey.versus.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class RootSpectateCommand extends RootCommand {

    public RootSpectateCommand() {
        super("spectate", "versus.spectate");
        setUsage("/spectate <player>");
        setDescription("Spectates a player in their current duel.");
        setAutonomous(true);
        setPermissionRequired(Settings.is(Setting.PERMISSION_REQUIRED_TO_DUEL));
        setPlayersOnly(true);
        setArgumentRequired(true);
        setMaxArguments(1);
        enforcePermissionRulesForChildren();
    }

    @Override
    public void callCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (args.length < 1) {
            error(sender, "Specify a player.");
            return;
        }
        String playerName = args[0];
        Player selectedPlayer = Bukkit.getPlayer(playerName);
        if (DuelManager.getInstance().isDueling(player)) {
            error(sender, "You cannot spectate right now.");
            return;
        }
        if (selectedPlayer == null) {
            error(sender, "'" + playerName + "' is not online.");
            return;
        }
        if (selectedPlayer.equals(player)) {
            error(sender, "You can't spectate yourself");
            return;
        }
        if (!DuelManager.getInstance().isDueling(selectedPlayer)) {
            error(sender, "'" + playerName + "' is not dueling right now.");
            return;
        }
        Duel duel = DuelManager.getInstance().getDuel(selectedPlayer);
        duel.spectate(player);
    }

    @Override
    public List<String> callCompletionsUpdate(CommandSender sender, String[] args) {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }
}
