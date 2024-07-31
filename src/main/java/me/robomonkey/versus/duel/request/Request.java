package me.robomonkey.versus.duel.request;

import com.google.common.collect.Lists;
import me.robomonkey.versus.Versus;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Request {
    private UUID requested;
    private UUID requesting;

    public Request(UUID requested, UUID requesting) {
        this.requested = requested;
        this.requesting = requesting;
    }

    public Request(Player requested, Player requesting) {
        this.requested = requested.getUniqueId();
        this.requesting = requesting.getUniqueId();
    }

    public UUID getRequested() {
        return requested;
    }

    public UUID getRequesting() {
        return requesting;
    }

    public Player getRequestedPlayer() {
        return Bukkit.getPlayer(requested);
    }

    public Player getRequestingPlayer() {
        return Bukkit.getPlayer(requesting);
    }

    public void setRequested(UUID requested) {
        this.requested = requested;
    }

    public boolean arePlayersOnline() {
        return Bukkit.getPlayer(requested) != null && Bukkit.getPlayer(requesting) != null;
    }

    public void setRequesting(UUID requesting) {
        this.requesting = requesting;
    }

    public boolean contains(Player player) {
        Versus.log("Request contains player: " + (requested.equals(player.getUniqueId()) || requesting.equals(player.getUniqueId())));
        return requested.equals(player.getUniqueId()) || requesting.equals(player.getUniqueId());
    }
}
