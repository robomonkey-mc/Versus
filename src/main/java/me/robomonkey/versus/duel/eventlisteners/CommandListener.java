package me.robomonkey.versus.duel.eventlisteners;

import me.robomonkey.versus.duel.DuelManager;
import me.robomonkey.versus.settings.Setting;
import me.robomonkey.versus.settings.Settings;
import me.robomonkey.versus.util.MessageUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandListener implements Listener {

    public static String BYPASS_BLOCKED_COMMANDS = "versus.bypass";

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (DuelManager.getInstance().isDueling(event.getPlayer())) {
            if (!event.getPlayer().hasPermission(BYPASS_BLOCKED_COMMANDS) && Settings.getInstance()
                    .getStringList(Setting.BLOCKED_COMMANDS).stream()
                    .anyMatch(command -> event.getMessage().contains(command))) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(MessageUtil.error("You cannot use this command right now."));
            }
        }
    }
}
