package me.robomonkey.versus.arena.data;

import me.robomonkey.versus.Versus;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;

public class LocationData {
    public double x;
    public double y;
    public double z;
    public float pitch;
    public float yaw;
    public String world;

    public LocationData(Location location) {
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.pitch = location.getPitch();
        this.yaw = location.getYaw();
        this.world = location.getWorld().getName();
    }

    public Location toLocation() {
        ensureExists();
        World world = Bukkit.getWorld(this.world);
        Location newLocation = new Location(world, x, y, z, yaw, pitch);
        return newLocation;
    }

    public void ensureExists() {
        if (Bukkit.getWorld(this.world) == null) {
            Versus.log("Loading world '" + world + "'.");
            new WorldCreator(world).createWorld();
        }
    }
}
