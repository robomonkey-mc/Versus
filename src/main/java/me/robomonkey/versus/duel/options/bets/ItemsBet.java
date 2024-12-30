package me.robomonkey.versus.duel.options.bets;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ItemsBet implements Bet {

    private Map<UUID, List<ItemStack>> itemsMap = new HashMap<>();

    public void addItems(Player player, ItemStack... items) {
        if (!itemsMap.containsKey(player.getUniqueId())) {
            itemsMap.put(player.getUniqueId(), new ArrayList<>());
        }
        List<ItemStack> currentItems = itemsMap.get(player.getUniqueId());
        Arrays.stream(items).forEach((item) -> currentItems.add(item));
    }

    public void setItems(Player player, List<ItemStack> items) {
        itemsMap.put(player.getUniqueId(), items);
    }

    @Override
    public void collect(Player player) {

    }

    @Override
    public void reward(Player player) {
        Optional<UUID> otherUUID = itemsMap.keySet().stream().filter(id -> !id.equals(player.getUniqueId())).findFirst();
        if(!otherUUID.isPresent()) {
            return;
        }
        List<ItemStack> items = itemsMap.get(otherUUID);
        Inventory rewardInventory = Bukkit.createInventory(null, 54);
        items.forEach((item) -> rewardInventory.addItem(item));
        player.openInventory(rewardInventory);
    }

    @Override
    public void penalize(Player player) {
        //TODO: NOTE this can be exploited if players don't have their items removed initially
        List<ItemStack> betItems = itemsMap.get(player.getUniqueId());
        betItems.forEach((item) -> player.getInventory().removeItem(item));
    }

    @Override
    public boolean canBet(Player player) {
        return true;
    }

    @Override
    public boolean hasRewardedAll() {
        return false;
    }
}
