package me.robomonkey.versus.arena;

import me.robomonkey.versus.duel.Duel;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class Arena {
    private final String name;
    private final List<Duel> activeDuels;
    private Location spawnLocationOne;
    private Location spawnLocationTwo;
    private Location centerLocation;
    private Location spectateLocation;
    private boolean enabled;

    /**
     * <h1>Creates Arena.</h1>
     * <p>Note: Arenas handle their own verification logic.</p>
     * @param name
     */
    public Arena(String name) {
        this.name = name;
        this.activeDuels = new ArrayList<Duel>();
    }

    public String getName() {
        return name;
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
            case SPAWN_LOCATION_TWO:
                spawnLocationTwo = value;
            case SPECTATE_LOCATION:
                spectateLocation = value;
            case CENTER_LOCATION:
                centerLocation = value;
        }
        verifySelf();
    }

    public void addDuel(Duel activeDuel) {
        this.activeDuels.add(activeDuel);
    }

    public void removeDuel(Duel completedDuel) {
        this.activeDuels.remove(completedDuel);
    }

    private void verifySelf(){
        if(!isValid()) enabled = false;
        else enabled = true;
    }

}
