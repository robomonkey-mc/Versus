package me.robomonkey.versus.duel;

import me.robomonkey.versus.Versus;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class Countdown {
    private BukkitTask countdown;
    private Versus plugin = Versus.getInstance();
    public static String countdownMessage;
    private long initialTime;
    private int duration;
    private Runnable onCountdownEnd;

    public Countdown(int duration, String countdownMessage) {
        this.duration = duration;
    }

    public Countdown(int duration, String countdownMessage, Runnable onCountdownEnd) {
        this.duration = duration;
        this.onCountdownEnd = onCountdownEnd;
    }

    public void setOnCountdownEnd(Runnable onCountdownEnd) {
        onCountdownEnd = onCountdownEnd;
    }

    public void initiateCountdown() {
        initialTime = System.currentTimeMillis();
        countdown = Bukkit.getScheduler().runTaskTimer(Versus.getInstance(),
                        getCountdownRunnable(),
                        0,
                        20);
    }

    private int getTimeElapsed(){
        if (initialTime == 0) {
            return 0;
        } else {
            long millisElapsed = (System.currentTimeMillis() - initialTime);
            int secondsElapsed = (int) Math.ceil((millisElapsed / 1000));
            return secondsElapsed;
        }
    }

    private int getTimeRemaining(){
        return duration - getTimeElapsed();
    }

    private Runnable getCountdownRunnable() {
        return () -> {
            if(getTimeRemaining() < 1) finishCountdown();
            String timeElapsed = String.valueOf(getTimeRemaining());
            String message = countdownMessage.replaceAll("%seconds%", timeElapsed);
            Bukkit.getServer().broadcastMessage(message);
        };
    }

    private void finishCountdown() {
        countdown.cancel();
        onCountdownEnd.run();
    }

    public void cancel(){
        countdown.cancel();
    }
}
