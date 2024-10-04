package me.robomonkey.versus.duel.playerdata;

import me.robomonkey.versus.arena.Arena;
import me.robomonkey.versus.arena.data.LocationData;
import me.robomonkey.versus.duel.Duel;
import me.robomonkey.versus.duel.options.Bet;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerData {

    public ItemStack[] items;
    public String arenaName;
    public LocationData previousLocation;
    public Bet bet;
    public int xpLevel;
    public float xpProgress;
    private boolean hasWon = false;

    public PlayerData(Player player, Duel currentDuel) {
        items = player.getInventory().getContents();
        previousLocation = new LocationData(player.getLocation());
        xpLevel = player.getLevel();
        xpProgress = player.getExp();
        bet = currentDuel.options().getBet();
        arenaName = currentDuel.getArena().getName();
    }
}
