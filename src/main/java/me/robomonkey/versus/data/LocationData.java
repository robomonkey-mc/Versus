package me.robomonkey.versus.data;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.IOException;

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
        World world = Bukkit.getWorld(this.world);
        Location newLocation = new Location(world, x, y, z, yaw, pitch);
        return newLocation;
    }
}
