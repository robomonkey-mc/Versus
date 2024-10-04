package me.robomonkey.versus.duel.options;

import org.bukkit.entity.Player;

public interface Bet {

    public static Bet EMPTY = null;
    /**
     * Collects a bet before a duel starts according to its own internal logic
     * @param player Player that bet should collect from.
     */
    public void collect(Player player);

    /**
     * Rewards a bet after a duel has completed.
     * @param player Player that bet should collect from.
     */
    public void reward(Player player);

    /**
     * Punishes a bet after a duel has completed.
     * @param player Player that bet should collect from.
     */
    public void punish(Player player);

    /**
     * Determines whether a player can make this bet.
     * @param player Player that bet should collect from.
     */
    public boolean canBet(Player player);

    /**
     * Returns whether a bet has rewarded all of its participants.
     */
    public boolean hasRewardedAll();

    enum Type {
        CURRENCY,
        INVENTORY,
        ITEMS,
        XP
    }
}
