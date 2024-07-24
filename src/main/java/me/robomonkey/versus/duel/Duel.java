package me.robomonkey.versus.duel;

import me.robomonkey.versus.arena.Arena;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public class Duel {
    private final ArrayList<Player> players = new ArrayList<>();
    private final Arena activeArena;
    private DuelState state = DuelState.IDLE;
    private UUID winner;
    private UUID loser;

    public Duel(Arena arena, Player... duelists){
        Collections.addAll(players, duelists);
        this.activeArena = arena;
    }

    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    public Arena getArena() {
        return this.activeArena;
    }

    public DuelState getState() {
        return state;
    }

    public boolean isActive() {
        return state == DuelState.ACTIVE;
    }

    public void setState(DuelState state){
        this.state = state;
    }

    public UUID getWinnerID() {
        return winner;
    }

    public UUID getLoserID() {
       return loser;
    }

    public Player getWinner() {
        return Bukkit.getPlayer(getWinnerID());
    }

    public Player getLoser() {
        return Bukkit.getPlayer(getLoserID());
    }

    public void registerVictory(Player winner, Player loser){
        this.loser = loser.getUniqueId();
        this.winner = winner.getUniqueId();
        this.setState(DuelState.ENDED);
    }
}
