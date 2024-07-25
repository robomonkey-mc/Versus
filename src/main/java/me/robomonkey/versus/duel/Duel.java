package me.robomonkey.versus.duel;

import me.robomonkey.versus.arena.Arena;
import me.robomonkey.versus.util.EffectUtil;
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
    private Countdown countdown = null;

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

    public Countdown getCountdown() {
        return this.countdown;
    }

    public DuelState getState() {
        return state;
    }

    public boolean isActive() {
        return (state == DuelState.ACTIVE || state == DuelState.COUNTDOWN);
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

    public void end(Player winner, Player loser){
        this.loser = loser.getUniqueId();
        this.winner = winner.getUniqueId();
        this.setState(DuelState.ENDED);
    }

    public void startCountdown(Runnable startDuelFunction) {
        setState(DuelState.COUNTDOWN);
        int countdownDuration = 10;
        String countdownMessage = "Starting in %seconds% seconds";
        players.stream().forEach((player) -> EffectUtil.freezePlayer(player));
        countdown = new Countdown(countdownDuration, countdownMessage, () -> {
                players.stream()
                        .forEach(EffectUtil::unfreezePlayer);
                startDuelFunction.run();
        });
        countdown.initiateCountdown();
    }

    public void cancelCountdown() {
        countdown.cancel();
    }
}
