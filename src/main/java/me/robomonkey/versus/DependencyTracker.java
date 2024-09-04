package me.robomonkey.versus;

import org.bukkit.Server;

public class DependencyTracker {
    public static boolean PlaceholderAPI = false;

    public static void refresh(Server currServer) {
        PlaceholderAPI = currServer.getPluginManager().getPlugin("PlaceholderAPI") != null;
    }
}
