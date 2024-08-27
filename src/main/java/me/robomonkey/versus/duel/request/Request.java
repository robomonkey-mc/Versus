package me.robomonkey.versus.duel.request;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Request {
    private UUID requested;
    private UUID requesting;
    private Long timecode;

    public Request(UUID requested, UUID requesting) {
        this.requested = requested;
        this.requesting = requesting;
        this.timecode = System.currentTimeMillis();
    }

    public Request(Player requested, Player requesting) {
        this.requested = requested.getUniqueId();
        this.requesting = requesting.getUniqueId();
    }

    public Long getTimeSent() {
        return timecode;
    }

    public void setTimecode(Long timecode) {
        this.timecode = timecode;
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
        return requested.equals(player.getUniqueId()) || requesting.equals(player.getUniqueId());
    }

    public boolean matches(Player requested, Player requesting) {
        return this.requested.equals(requested.getUniqueId()) && this.requesting.equals(requesting.getUniqueId());
    }
}
