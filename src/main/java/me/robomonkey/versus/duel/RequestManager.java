package me.robomonkey.versus.duel;

import me.robomonkey.versus.util.MessageUtil;
import org.bukkit.Bukkit;
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

    public void sendRequest(Player requesting, Player requested) {
        requestMap.put(requesting.getUniqueId(), requested.getUniqueId());
    }

    public boolean hasIncomingRequest(Player player) {
        return (requestMap.containsValue(player.getUniqueId()));
    }

    public boolean hasOutgoingRequest(Player player) {
        return (requestMap.containsKey(player.getUniqueId()));
    }

    public UUID getCurrentlyRequesting(Player player) {
        return requestMap.get(player.getUniqueId());
    }

    private UUID getRequester(Player player) {
        if(!hasIncomingRequest(player)) return null;
        Map.Entry<UUID, UUID> request = requestMap.entrySet().stream().filter(entry -> entry.getValue().equals(player.getUniqueId())).findFirst().get();
        return request.getKey();
    }

    public void acceptRequest(Player player) throws PlayerOfflineException {
        Player requester = Bukkit.getPlayer(getRequester(player));
        if(requester == null) {
            throw new PlayerOfflineException();
        }
        DuelManager.getInstance().setupDuel(requester, player);
    }

    public void denyRequest(Player player) {
        requestMap.remove(getRequester(player));
        Player requester = Bukkit.getPlayer(getRequester(player));
        if(requester != null) {
            requester.sendMessage(MessageUtil.color("&c&lYour request has been &c&ldenied."));
        }
    }

    public class PlayerOfflineException extends Exception {
        public PlayerOfflineException() {
            super("The requested player is offline.");
        }
    }
}
