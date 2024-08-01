package me.robomonkey.versus.duel.eventlisteners;
import me.robomonkey.versus.duel.DuelManager;
import me.robomonkey.versus.settings.Setting;
import me.robomonkey.versus.settings.Settings;
import me.robomonkey.versus.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;

public class CommandListener implements Listener {

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event){
        if(DuelManager.getInstance().isDueling(event.getPlayer())) {
            if(Settings.getInstance()
                    .getStringList(Setting.BLOCKED_COMMANDS).stream()
                    .anyMatch(command -> event.getMessage().contains(command))) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(MessageUtil.error("You cannot use this command right now."));
            }
        }
    }
}
