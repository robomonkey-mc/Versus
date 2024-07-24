package me.robomonkey.versus.arena;

import com.google.gson.Gson;
import me.robomonkey.versus.Versus;
import me.robomonkey.versus.duel.Duel;
import me.robomonkey.versus.duel.DuelManager;
import me.robomonkey.versus.util.JsonUtil;
import me.robomonkey.versus.data.ArenaData;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ArenaManager {
    private ArrayList<Arena> arenaList = new ArrayList<>();
    public File dataFile;
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

    public ArrayList<Arena> getAllArenas() {
        return arenaList;
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
        List<ArenaData> loaded = new ArrayList<>();
        dataFile = JsonUtil.getDataFile(plugin, "arena.json");
        try {
            loaded = JsonUtil.readObject(loaded.getClass(), dataFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        loaded.stream().forEach(arena -> {
            Arena newArena = Arena.fromArenaData(arena);
            arenaList.add(newArena);
        });
    }

    public void saveAllArenas() {
        List<ArenaData> data = arenaList.stream().map(arena -> arena.toArenaData()).collect(Collectors.toList());
        try {
            JsonUtil.writeObject(data, dataFile, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendToSpectatingArea(Player p, Arena a) {
        Location spectateLocation = a.getSpectateLocation();
        p.teleport(spectateLocation);
    }

    public void deleteArena(Arena arena) {
        arenaList.remove(arena);
        saveAllArenas();
    }

    public void registerDuel(Arena arena, Duel duel) {
        arena.addDuel(duel);
    }

    public void removeDuel(Duel duel) {
        Arena arena = duel.getArena();
        arena.removeDuel(duel);
    }

}
