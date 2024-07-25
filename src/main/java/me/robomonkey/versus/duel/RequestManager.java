package me.robomonkey.versus.duel;

import me.robomonkey.versus.util.EffectUtil;
import me.robomonkey.versus.util.MessageUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RequestManager {
    private Map<UUID, UUID> requestMap = new HashMap<>();
    private static RequestManager instance;

    public static RequestManager getInstance() {
        if(instance == null) {
            instance = new RequestManager();
        }
        return instance;
    }

    private void removeRequest(Player player) {
        requestMap.remove(player.getUniqueId());
    }

    public boolean hasIncomingRequest(Player requested) {
        return (requestMap.containsValue(requested.getUniqueId()));
    }

    public boolean hasOutgoingRequest(Player requesting) {
        return (requestMap.containsKey(requesting.getUniqueId()));
    }

    public UUID getCurrentlyRequesting(Player requesting) {
        return requestMap.get(requesting.getUniqueId());
    }

    public UUID getRequester(Player requested) {
        if(!hasIncomingRequest(requested)) return null;
        Map.Entry<UUID, UUID> request = requestMap.entrySet().stream().filter(entry -> entry.getValue().equals(requested.getUniqueId())).findFirst().get();
        return request.getKey();
    }

    public void sendRequest(Player requesting, Player requested) {
        requestMap.put(requesting.getUniqueId(), requested.getUniqueId());
        requesting.sendMessage(MessageUtil.color("&7You just sent a &cduel request to &6"+requested.getName()+"."));
        TextComponent requestMessage = getRequestMessage(requested, requesting);
        requested.sendMessage(MessageUtil.color("&6"+requesting.getName()+" &7has invited you to a &4duel!"));
        EffectUtil.playSound(requested, Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
        requested.spigot().sendMessage(requestMessage);
    }

    public void acceptRequest(Player requested) throws PlayerOfflineException {
        Player requester = Bukkit.getPlayer(getRequester(requested));
        if(requester == null) {
            throw new PlayerOfflineException();
        }
        requestMap.remove(requester.getUniqueId());
        DuelManager.getInstance().setupDuel(requester, requested);
    }

    public void denyRequest(Player requested) {
        requestMap.remove(getRequester(requested));
        Player requester = Bukkit.getPlayer(getRequester(requested));
        if(requester != null) {
            requester.sendMessage(MessageUtil.color("&c&lYour request has been &c&ldenied."));
        }
    }

    public class PlayerOfflineException extends Exception {
        public PlayerOfflineException() {
            super("The requested player is offline.");
        }
    }

    private TextComponent getRequestMessage(Player requested, Player requesting) {
        String acceptCommand = "/duel "+requesting.getName();
        String denyCommand = "/duel deny";
        TextComponent parentButton = MessageUtil.createButton("&7&l[&a&lACCEPT&7&l]", acceptCommand, acceptCommand);
        TextComponent denyButton = MessageUtil.createButton("&7&l[&c&lDENY&7&l]", acceptCommand, denyCommand);
        parentButton.addExtra("  ");
        parentButton.addExtra(denyButton);
        return parentButton;
    }
}
