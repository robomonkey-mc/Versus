package me.robomonkey.versus.arena;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ArenaBuilderCoordinator {
    Map<Player, ArenaBuilder> arenaMap = new HashMap<>();

    private static ArenaBuilderCoordinator instance;

    public static ArenaBuilderCoordinator getInstance() {
        if (instance == null) {
            instance = new ArenaBuilderCoordinator();
        }
        return instance;
    }

    public ArenaBuilder getArenaBuilder(Player player) {
        return arenaMap.get(player);
    }

    public boolean hasArenaBuilder(Player player) {
        return arenaMap.containsKey(player);
    }

    public void initiateArenaBuilder(Player builderPlayer, String name) {
        ArenaBuilder arenaBuilder = new ArenaBuilder(builderPlayer, name);
        arenaMap.put(builderPlayer, arenaBuilder);
        arenaBuilder.handleNextStep();
    }

    public void removeArenaBuilder(Player builderPlayer) {
        arenaMap.remove(builderPlayer);
    }


}
