package me.robomonkey.versus.duel;

import me.robomonkey.versus.arena.Arena;
import me.robomonkey.versus.settings.Placeholder;
import me.robomonkey.versus.settings.Setting;
import me.robomonkey.versus.settings.Settings;
import me.robomonkey.versus.util.EffectUtil;
import me.robomonkey.versus.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.Color;
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
    private boolean victoryEffectsEnabled = Settings.getBoolean(Setting.VICTORY_EFFECTS_ENABLED);
    private boolean fireworksEnabled = Settings.getBoolean(Setting.FIREWORKS_ENABLED);
    private Color fireworkColor = Settings.getColor(Setting.FIREWORKS_COLOR);
    private Sound victorySong = Settings.getSong(Setting.VICTORY_MUSIC);
    private Sound fightMusic = Settings.getSong(Setting.FIGHT_MUSIC);
    private boolean blindnessEnabled = Settings.getBoolean(Setting.BLINDNESS_EFFECTS_ENABLED);

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

    public boolean isFireworksEnabled() {
        return fireworksEnabled;
    }

    public boolean isVictoryEffectsEnabled() {
        return victoryEffectsEnabled;
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

    public Sound getFightMusic() {
        return fightMusic;
    }

    public Sound getVictorySong() {
        return victorySong;
    }

    public Color getFireworkColor() {
        return (fireworkColor == null)? Color.ORANGE: fireworkColor;
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
        int countdownDuration = Settings.getNumber(Setting.COUNTDOWN_DURATION);
        String countdownMessage = Settings.getMessage(Setting.COUNTDOWN_MESSAGE);
        players.stream().forEach((player) -> EffectUtil.freezePlayer(player));
        countdown = new Countdown(countdownDuration, countdownMessage, () -> {
                players.stream()
                        .forEach(EffectUtil::unfreezePlayer);
                onCountdownExpiration.run();
        });
        countdown.setOnCount(() -> {
            String countdownTitle = Settings.getMessage(Setting.COUNTDOWN_TITLE, Placeholder.of("%seconds%", countdown.getSecondsRemaining()));
            players.forEach(player -> EffectUtil.playSound(player, Sound.UI_BUTTON_CLICK));
            players.forEach(player -> EffectUtil.sendTitle(player, countdownTitle, 30, false));
        });
        countdown.initiateCountdown();
    }

    public void cancelCountdown() {
        countdown.cancel();
    }

    public void spectate(Player player) {
        String spectateMessage = Settings.getMessage(
                Setting.DUEL_SPECTATE_MESSAGE,
                Placeholder.of("%player_one%", getPlayers().getFirst()),
                Placeholder.of("%player_two%", getPlayers().getLast()));
        player.sendMessage(spectateMessage);
        player.teleport(activeArena.getSpectateLocation());
    }
}
