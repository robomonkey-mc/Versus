package me.robomonkey.versus.arena;

import me.robomonkey.versus.Versus;
import me.robomonkey.versus.duel.Duel;
import me.robomonkey.versus.duel.DuelManager;

import java.util.ArrayList;
import java.util.HashMap;

public class ArenaManager {

    public ArrayList<Arena> arenaList = new ArrayList<>();
    public DuelManager duelManager = DuelManager.getInstance();
    public Versus plugin = Versus.getInstance();

    public Arena getArena(String name) {
        Arena matching = arenaList.stream()
                .filter((arena) -> arena.getName().equalsIgnoreCase(name))
                .findFirst().get();
        return matching;
    }

    /**
     * Returns first available arena in ArenaManager.arenaList.
     * May return Arena.empty or null if no arenas are available.
     * @return
     */
    public Arena getAvailableArena() {
        for(Arena arena: arenaList) {
            if(arena.isAvailable()) return arena;
        }
        return Arena.empty;
    }

    public void loadArenas() {

    }

    public void saveAllArenas() {

    }

    public void deleteArena(Arena arena) {

    }

    public void editArena(Arena arena) {

    }

    public void registerDuel(Arena arena, Duel duel) {

    }

}
