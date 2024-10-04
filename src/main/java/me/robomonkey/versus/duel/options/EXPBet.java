package me.robomonkey.versus.duel.options;

import org.bukkit.entity.Player;

public class EXPBet implements Bet {

    int level;
    float progress;

    public EXPBet(int level, float progress) {
        this.level = level;
        this.progress = progress;
    }

    @Override
    public void collect(Player player) {
        
    }

    @Override
    public void reward(Player player) {
        int currLevel = player.getLevel();
        float currProgress = player.getExp();
        player.setExp(currProgress + progress);
        player.setLevel(currLevel + level);
    }

    @Override
    public void punish(Player player) {
        int currLevel = player.getLevel();
        float currProgress = player.getExp();
        player.setExp(currProgress - progress);
        player.setLevel(currLevel - level);
    }

    @Override
    public boolean canBet(Player player) {
        return player.getLevel() >= level && player.getExp() >= progress;
    }

    @Override
    public boolean hasRewardedAll() {
        return false;
    }
}
