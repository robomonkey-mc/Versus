package me.robomonkey.versus.data;

import me.robomonkey.versus.duel.Duel;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class ArenaData {
    public String name;
    public Location spawnLocationOne;
    public Location spawnLocationTwo;
    public Location centerLocation;
    public Location spectateLocation;
    public boolean enabled;

    /**
     * <h1>Creates Arena.</h1>
     * <p>Note: Arenas handle their own verification logic.</p>
     * @param
     */
    public ArenaData(String name,
                     Location spawnLocationOne,
                     Location spawnLocationTwo,
                     Location centerLocation,
                     Location spectateLocation,
                     Boolean enabled) {
        this.name = name;
        this.spawnLocationOne = spawnLocationOne;
        this.spawnLocationTwo = spawnLocationTwo;
        this.centerLocation = centerLocation;
        this.spectateLocation = spectateLocation;
        this.enabled = enabled;
    }

}
