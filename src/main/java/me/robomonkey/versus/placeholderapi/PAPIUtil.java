package me.robomonkey.versus.placeholderapi;

import me.clip.placeholderapi.PlaceholderAPI;
import me.robomonkey.versus.Versus;
import org.bukkit.OfflinePlayer;

public class PlaceholderUtil {

    public static String getName(OfflinePlayer player) {
        if(PlaceholderAPI.isRegistered("%essentials_nickname%")) {
            return PlaceholderAPI.setPlaceholders(player, "%essentials_nickname%");
        }
        Versus.error("Essentials is not installed. Ignoring PlaceholderAPI essentials integration.");
        return player.getName();
    }
}
