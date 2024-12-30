package me.robomonkey.versus.duel.command;

import me.robomonkey.versus.command.RootCommand;
import me.robomonkey.versus.duel.menu.AdminDuelMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

//TODO Delete this file!!!
public class TestCommand extends RootCommand {
    public TestCommand() {
        super("testCommand", "");
        setPlayersOnly(true);
        setAutonomous(true);
        setArgumentRequired(false);
    }

    @Override
    public void callCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        AdminDuelMenu gui = new AdminDuelMenu(player, (arg) -> {});
        gui.open();
    }

    @Override
    public List<String> callCompletionsUpdate(CommandSender sender, String[] args) {
        return List.of();
    }
}
