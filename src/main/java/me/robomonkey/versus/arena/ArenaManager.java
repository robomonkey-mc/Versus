package me.robomonkey.versus.arena;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import me.robomonkey.versus.Versus;
import me.robomonkey.versus.arena.data.ArenaData;
import me.robomonkey.versus.duel.Duel;
import me.robomonkey.versus.kit.Kit;
import me.robomonkey.versus.util.JsonUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class ArenaManager {
    private ArrayList<Arena> arenaList = new ArrayList<>();
    public File dataFile;
    public Versus plugin = Versus.getInstance();
    private static ArenaManager instance;

    public static ArenaManager getInstance() {
        if (instance == null) {
            instance = new ArenaManager();
        }
        return instance;
    }

    public Arena getArena(String name) {
        Optional<Arena> matching = arenaList.stream()
                .filter((arena) -> arena.getName().equalsIgnoreCase(name))
                .findFirst();
        if (matching.isPresent()) return matching.get();
        else return null;
    }

    public void addArena(Arena arena) {
        arenaList.add(arena);
    }

    public ArrayList<Arena> getAllArenas() {
        return arenaList;
    }

    /**
     * Returns first available arena in ArenaManager.arenaList.
     * May return Arena.empty or null if no arenas are available.
     *
     * @return
     */
    public Arena getAvailableArena() {
        List<Arena> availableArenas = arenaList.stream().filter(arena -> arena.isAvailable()).collect(Collectors.toList());
        if (availableArenas.size() > 0) {
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
            Type arenaListType = new TypeToken<List<ArenaData>>() {
            }.getType();
            Reader reader = new FileReader(dataFile);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            loaded = gson.fromJson(reader, arenaListType);
            if (loaded == null) {
                Versus.log("There are no arenas in arena.json.");
                return;
            }
            loaded.stream().forEach(arena -> {
                Arena newArena = Arena.fromArenaData(arena);
                arenaList.add(newArena);
                Versus.log("Loaded " + newArena.getName() + " arena.");
            });
            Versus.log("Successfully loaded arenas.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveAllArenas() {
        List<ArenaData> data = arenaList.stream()
                .filter(Arena::isEnabled)
                .map(Arena::toArenaData)
                .collect(Collectors.toList());
        try {
            Writer writer = new FileWriter(dataFile);
            Type arenaListType = new TypeToken<List<ArenaData>>() {
            }.getType();
            Gson gson = new Gson();
            gson.toJson(data, arenaListType, writer);
            writer.close();
            Versus.log("Successfully saved arenas.");
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

    public void notifyKitSelection(Kit kit, Player whoClicked) {
        if (ArenaBuilderCoordinator.getInstance().hasArenaBuilder(whoClicked)) {
            ArenaBuilder builder = ArenaBuilderCoordinator.getInstance().getArenaBuilder(whoClicked);
            builder.getTargetArena().setKit(kit);
        }
    }
}
