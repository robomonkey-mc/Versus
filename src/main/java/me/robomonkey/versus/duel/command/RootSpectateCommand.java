package me.robomonkey.versus.duel.command;

import me.robomonkey.versus.command.RootCommand;
import me.robomonkey.versus.duel.Duel;
import me.robomonkey.versus.duel.DuelManager;
import me.robomonkey.versus.settings.Error;
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
            error(sender, Error.SPECIFY_A_PLAYER);
            return;
        }
        String playerName = args[0];
        Player selectedPlayer = Bukkit.getPlayer(playerName);
        if (DuelManager.getInstance().isDueling(player)) {
            error(sender, Error.SPECTATE_WHILE_DUELING);
            return;
        }
        if (selectedPlayer == null) {
            error(sender, Error.IS_NOT_ONLINE, playerName);
            return;
        }
        if (selectedPlayer.equals(player)) {
            error(sender, Error.SPECTATE_YOURSELF);
            return;
        }
        if (!DuelManager.getInstance().isDueling(selectedPlayer)) {
            error(sender, Error.IS_NOT_DUELING, selectedPlayer.getName());
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
