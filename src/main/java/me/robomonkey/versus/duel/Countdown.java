package me.robomonkey.versus.duel;

import me.robomonkey.versus.Versus;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class Countdown {
    private BukkitTask countdown;
    private Versus plugin = Versus.getInstance();
    private long initialTime;
    private int duration;
    private int secondsRemaining;

    private Runnable onCountdownEnd;
    private Runnable onCount;

    public Countdown(int duration, String countdownMessage) {
        this.duration = duration;
    }

    public Countdown(int duration, Runnable onCountdownEnd) {
        this.duration = duration;
        this.secondsRemaining = duration;
        this.onCountdownEnd = onCountdownEnd;
    }

    public int getSecondsRemaining() {
        return secondsRemaining;
    }

    public void setOnCountdownEnd(Runnable onCountdownEnd) {
        onCountdownEnd = onCountdownEnd;
    }

    public void setOnCount(Runnable onCount) {
        this.onCount = onCount;
    }

    public void initiateCountdown() {
        initialTime = System.currentTimeMillis();
        countdown = Bukkit.getScheduler().runTaskTimer(Versus.getInstance(),
                count(),
                0,
                20);
    }

    private Runnable count() {
        return () -> {
            if (getSecondsRemaining() == 0) {
                finishCountdown();
                return;
            }
            if (onCount != null) onCount.run();
            secondsRemaining--;
        };
    }

    private void finishCountdown() {
        countdown.cancel();
        onCountdownEnd.run();
    }

    public void cancel() {
        countdown.cancel();
    }
}
