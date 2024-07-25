package me.robomonkey.versus.data;

import com.google.gson.annotations.Expose;
import me.robomonkey.versus.duel.Duel;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class ArenaData {
    @Expose
    public String name;
    @Expose
    public LocationData spawnLocationOne;
    @Expose
    public LocationData spawnLocationTwo;
    @Expose
    public LocationData centerLocation;
    @Expose
    public LocationData spectateLocation;
    @Expose
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
        this.spawnLocationOne = new LocationData(spawnLocationOne);
        this.spawnLocationTwo = new LocationData(spawnLocationTwo);
        this.centerLocation = new LocationData(centerLocation);
        this.spectateLocation = new LocationData(spectateLocation);
        this.enabled = enabled;
    }

}
