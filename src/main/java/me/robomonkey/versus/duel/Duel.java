package me.robomonkey.versus.duel;

import me.robomonkey.versus.arena.Arena;
import me.robomonkey.versus.duel.options.bets.DuelOptions;
import me.robomonkey.versus.settings.Placeholder;
import me.robomonkey.versus.settings.Setting;
import me.robomonkey.versus.settings.Settings;
import me.robomonkey.versus.util.EffectUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public class Duel {
    private final ArrayList<Player> players = new ArrayList<>();
    private DuelState state = DuelState.IDLE;
    private UUID winner;
    private UUID loser;
    private Countdown countdown = null;
    private DuelOptions options;

    public Duel(DuelOptions options, Player... duelists) {
        Collections.addAll(players, duelists);
        this.options = options;
    }

    // Getters

    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    public Arena getArena() {
        return this.options.getArena();
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

    public DuelOptions options() {
        return this.options;
    }

    // Setters

    public void setWinner(UUID winner) {
        this.winner = winner;
    }

    public void setState(DuelState state) {
        this.state = state;
    }

    // Methods

    public void end(Player winner, Player loser) {
        this.loser = loser.getUniqueId();
        this.winner = winner.getUniqueId();
        this.setState(DuelState.ENDED);
    }

    public void startCountdown(Runnable onCountdownExpiration) {
        setState(DuelState.COUNTDOWN);
        int countdownDuration = Settings.getNumber(Setting.COUNTDOWN_DURATION);
        players.stream().forEach((player) -> EffectUtil.freezePlayer(player));
        countdown = new Countdown(countdownDuration, () -> {
            players.stream()
                    .forEach(EffectUtil::unfreezePlayer);
            onCountdownExpiration.run();
        });
        countdown.setOnCount(() -> {
            String countdownMessage = Settings.getMessage(Setting.COUNTDOWN_MESSAGE, Placeholder.of("%seconds%", countdown.getSecondsRemaining()));
            String countdownTitle = Settings.getMessage(Setting.COUNTDOWN_TITLE, Placeholder.of("%seconds%", countdown.getSecondsRemaining()));
            players.forEach(player -> {
                EffectUtil.playSound(player, Sound.UI_BUTTON_CLICK);
                EffectUtil.sendTitle(player, countdownTitle, 30, false);
                player.sendMessage(countdownMessage);
            });
        });
        countdown.initiateCountdown();
    }

    public void cancelCountdown() {
        countdown.cancel();
    }

    public void spectate(Player player) {
        String spectateMessage = Settings.getMessage(
                Setting.DUEL_SPECTATE_MESSAGE,
                Placeholder.of("%player_one%", getPlayers().get(0).getName()),
                Placeholder.of("%player_two%", getPlayers().get(1).getName()));
        player.sendMessage(spectateMessage);
        player.teleport(getArena().getSpectateLocation());
    }

    public void removeFromSpectating(Player player) {
        player.teleport(getArena().getSpectateLocation());
    }
}
