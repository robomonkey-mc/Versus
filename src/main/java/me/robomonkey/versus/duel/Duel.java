package me.robomonkey.versus.duel;

import me.robomonkey.versus.arena.Arena;
import me.robomonkey.versus.util.EffectUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
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

    public void setWinner(UUID winner) {
        this.winner = winner;
    }

    public void setState(DuelState state){
        this.state = state;
    }

    public void end(Player winner, Player loser){
        this.loser = loser.getUniqueId();
        this.winner = winner.getUniqueId();
        this.setState(DuelState.ENDED);
    }

    public void startCountdown(Runnable onCountdownExpiration) {
        setState(DuelState.COUNTDOWN);
        int countdownDuration = 5;
        String countdownMessage = "&6Starting in &e%seconds%&6 seconds";
        players.stream().forEach((player) -> EffectUtil.freezePlayer(player));
        countdown = new Countdown(countdownDuration, countdownMessage, () -> {
                players.stream()
                        .forEach(EffectUtil::unfreezePlayer);
                onCountdownExpiration.run();
        });
        countdown.setOnCount(() -> {
            players.forEach(player -> EffectUtil.playSound(player, Sound.UI_BUTTON_CLICK));
            players.forEach(player -> EffectUtil.sendTitle(player, "&7"+countdown.getSecondsRemaining(), 30, false));
        });
        countdown.initiateCountdown();
    }

    public void cancelCountdown() {
        countdown.cancel();
    }
}
