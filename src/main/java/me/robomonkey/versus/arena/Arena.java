package me.robomonkey.versus.arena;

import me.robomonkey.versus.arena.data.ArenaData;
import me.robomonkey.versus.duel.Duel;
import me.robomonkey.versus.kit.Kit;
import me.robomonkey.versus.kit.KitManager;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class Arena {
    public static Arena empty = null;
    private final String name;
    private final List<Duel> activeDuels;
    private Location spawnLocationOne;
    private Location spawnLocationTwo;
    private Location centerLocation;
    private Location spectateLocation;
    private boolean enabled = false;
    private Kit kit;

    /**
     * <h1>Creates Arena.</h1>
     * <p>Note: Arenas handle their own verification logic.</p>
     *
     * @param name
     */
    public Arena(String name) {
        this.name = name;
        this.activeDuels = new ArrayList<Duel>();
        this.enabled = false;
    }

    public String getName() {
        return name;
    }

    public Kit getKit() {
        return kit;
    }

    public List<Duel> getActiveDuels() {
        return activeDuels;
    }

    public Location getSpawnLocationOne() {
        return spawnLocationOne;
    }

    public Location getSpawnLocationTwo() {
        return spawnLocationTwo;
    }

    public Location getCenterLocation() {
        return centerLocation;
    }

    public Location getSpectateLocation() {
        return spectateLocation;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isAvailable() {
        return this.getActiveDuels().size() == 0;
    }

    private boolean isValid() {
        return (spawnLocationOne != null
                && spawnLocationTwo != null
                && centerLocation != null
                && spectateLocation != null);
    }

    public void setLocationProperty(ArenaProperty property, Location value) {
        switch (property) {
            case SPAWN_LOCATION_ONE:
                spawnLocationOne = value;
                break;
            case SPAWN_LOCATION_TWO:
                spawnLocationTwo = value;
                break;
            case SPECTATE_LOCATION:
                spectateLocation = value;
                break;
            case CENTER_LOCATION:
                centerLocation = value;
                break;
        }
        verifySelf();
    }

    public void setKit(Kit kit) {
        this.kit = kit;
        verifySelf();
    }

    public void addDuel(Duel activeDuel) {
        this.activeDuels.add(activeDuel);
    }

    public void removeDuel(Duel completedDuel) {
        this.activeDuels.remove(completedDuel);
    }

    private void verifySelf() {
        if (!isValid()) enabled = false;
        else enabled = true;
    }

    public ArenaData toArenaData() {
        verifySelf();
        return new ArenaData(name,
                spawnLocationOne,
                spawnLocationTwo,
                centerLocation,
                spectateLocation,
                enabled,
                kit.getName());
    }

    public static Arena fromArenaData(ArenaData jsonArena) {
        Arena newArena = new Arena(jsonArena.name);
        newArena.setLocationProperty(ArenaProperty.CENTER_LOCATION, jsonArena.centerLocation.toLocation());
        newArena.setLocationProperty(ArenaProperty.SPAWN_LOCATION_ONE, jsonArena.spawnLocationOne.toLocation());
        newArena.setLocationProperty(ArenaProperty.SPAWN_LOCATION_TWO, jsonArena.spawnLocationTwo.toLocation());
        newArena.setLocationProperty(ArenaProperty.SPECTATE_LOCATION, jsonArena.spectateLocation.toLocation());
        newArena.setKit(KitManager.getInstance().getKit(jsonArena.kit));
        return newArena;
    }

}

