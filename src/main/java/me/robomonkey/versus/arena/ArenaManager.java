package me.robomonkey.versus.arena;

import me.robomonkey.versus.Versus;
import me.robomonkey.versus.duel.Duel;
import me.robomonkey.versus.duel.DuelManager;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ArenaManager {

    public ArrayList<Arena> arenaList = new ArrayList<>();
    public DuelManager duelManager = DuelManager.getInstance();
    public Versus plugin = Versus.getInstance();
    private static ArenaManager instance;

    public static ArenaManager getInstance() {
        if(instance == null) {
            instance = new ArenaManager();
        }
        return instance;
    }
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
        List<Arena> availableArenas = arenaList.stream().filter(arena -> arena.isAvailable()).collect(Collectors.toList());
        if(availableArenas.size() > 0) {
            Random random = new Random();
            int randomIndex = random.nextInt(availableArenas.size());
            return availableArenas.get(randomIndex);
        } else {
            return Arena.empty;
        }
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
        arena.addDuel(duel);
    }

    public void removeDuel(Arena arena, Duel duel) {
        arena.removeDuel(duel);
    }

}
