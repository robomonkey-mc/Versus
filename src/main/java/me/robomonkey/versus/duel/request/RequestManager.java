package me.robomonkey.versus.duel.request;

import me.robomonkey.versus.Versus;
import me.robomonkey.versus.arena.ArenaManager;
import me.robomonkey.versus.duel.DuelManager;
import me.robomonkey.versus.settings.Placeholder;
import me.robomonkey.versus.settings.Setting;
import me.robomonkey.versus.settings.Settings;
import me.robomonkey.versus.util.EffectUtil;
import me.robomonkey.versus.util.MessageUtil;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.*;

public class RequestManager {
    private ArrayList<Request> requestList = new ArrayList<>();
    private LinkedList<Request> queue = new LinkedList<>();
    private static RequestManager instance;

    public static RequestManager getInstance() {
        if(instance == null) {
            instance = new RequestManager();
        }
        return instance;
    }

    private Request getRequest(Player player) {
        Optional<Request> optionalRequest = requestList.stream()
                                    .filter(request -> request.contains(player))
                                    .findFirst();
        return (optionalRequest.isPresent())? optionalRequest.get() : null;
    }

    private void updateQueue() {
        while(queue.peek()!=null && !queue.peek().arePlayersOnline()) {
            queue.pop();
        }
        if(queue.size() > 0) {
            Request latest = queue.pop();
            DuelManager.getInstance().setupDuel(latest.getRequestedPlayer(), latest.getRequestingPlayer());
        }
    }

    private void removeRequest(Player player) {
        requestList.remove(getRequest(player));
    }

    public void placeInQueue(Request request) {
        requestList.remove(request);
        queue.add(request);
    }

    public Request getQueuedRequest(Player player){
        Optional<Request> optionalRequest = queue.stream()
                .filter(request -> request.contains(player))
                .findFirst();
        return (optionalRequest.isPresent())? optionalRequest.get() : null;
    }

    public void cancelQueue(Player player) {
        queue.removeIf(request -> request.contains(player));
    }

    public boolean isQueued(Player player) {
        return queue.stream().anyMatch(request -> request.contains(player));
    }

    public Request getFirstInQueue() {
        if(queue.size() > 0) return queue.pop();
        else return null;
    }

    public void notifyDuelCompletion() {
        updateQueue();
    }

    public boolean hasIncomingRequest(Player requested) {
        return requestList.stream()
                .filter(request -> request.getRequested().equals(requested.getUniqueId()))
                .findFirst().isPresent();
    }

    public boolean hasOutgoingRequest(Player requesting) {
        return requestList.stream()
                .filter(request -> request.getRequesting().equals(requesting.getUniqueId()))
                .findFirst().isPresent();
    }

    public boolean anyPlayersQueued() {
        return queue.size() > 0;
    }

    public boolean contains(Player player) {
        return isQueued(player) || getRequest(player) != null;
    }

    public void cancelRequest(Request request) {
        requestList.remove(request);
        queue.remove(request);
    }

    public UUID getRequested(Player requesting) {
        if(hasOutgoingRequest(requesting)) return getRequest(requesting).getRequested();
        else return null;
    }

    public UUID getRequester(Player requested) {
        if(hasIncomingRequest(requested)) return getRequest(requested).getRequesting();
        else return null;
    }

    public void sendRequest(Player requesting, Player requested) {
        requestList.add(new Request(requested, requesting));
        String sentRequestMessage = Settings.getMessage(Setting.SENT_REQUEST, new Placeholder("%player%", requested.getName()));
        String requestNotification = Settings.getMessage(Setting.REQUEST_NOTIFICATION, new Placeholder("%player%", requesting.getName()));

        requesting.sendMessage(sentRequestMessage);
        TextComponent requestMessage = getRequestMessage(requested, requesting);
        requested.sendMessage(requestNotification);
        EffectUtil.playSound(requested, Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
        requested.spigot().sendMessage(requestMessage);
    }

    public void acceptRequest(Player requested) throws PlayerOfflineException {
        Request currentRequest = getRequest(requested);
        Player requester = currentRequest.getRequestingPlayer();
        if(requester == null) {
            throw new PlayerOfflineException();
        }
        removeRequest(requested);
        if(ArenaManager.getInstance().getAvailableArena() == null) {
            requester.sendMessage(Settings.getMessage(Setting.NO_ARENAS_AVAILABLE));
            requested.sendMessage(Settings.getMessage(Setting.NO_ARENAS_AVAILABLE));
            placeInQueue(currentRequest);
        } else {
            DuelManager.getInstance().setupDuel(requester, requested);
        }
    }

    public void denyRequest(Player requested) {
        Player requester = getRequest(requested).getRequestingPlayer();
        removeRequest(requested);
        if(requester != null) {
            requester.sendMessage(Settings.getMessage(Setting.DENIED_REQUEST, Placeholder.of("%player", requested.getName())));
            requested.sendMessage(Settings.getMessage(Setting.DENIED_REQUEST_CONFIRMATION, Placeholder.of("%player%", requester.getName())));
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
        String acceptButtonText = Settings.getMessage(Setting.ACCEPT_BUTTON);
        String denyButtonText = Settings.getMessage(Setting.DENY_BUTTON);
        TextComponent parentButton = MessageUtil.createButton(acceptButtonText, acceptCommand, acceptCommand);
        TextComponent denyButton = MessageUtil.createButton(denyButtonText, denyCommand, denyCommand);
        parentButton.addExtra("  ");
        parentButton.addExtra(denyButton);
        return parentButton;
    }
}
