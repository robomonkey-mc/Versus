package me.robomonkey.versus.duel.options;

import org.bukkit.entity.Player;

public class CurrencyBet implements Bet{
    //TODO Implement this class for specific economy plugins
    public double amount;

    public CurrencyBet(double amount) {
        this.amount = amount;
    }
    @Override
    public void collect(Player player) {
       //assert that player canBet

    }

    @Override
    public void reward(Player player) {
        //add amount to player's bank
    }

    @Override
    public void punish(Player player) {
        //remove amount from player's bank
    }

    @Override
    public boolean canBet(Player player) {
        //return whether player has enough money
        return false;
    }

    @Override
    public boolean hasRewardedAll() {
        return false;
    }
}
