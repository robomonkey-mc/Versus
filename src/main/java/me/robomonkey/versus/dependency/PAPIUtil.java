package me.robomonkey.versus.dependency;

import me.clip.placeholderapi.PlaceholderAPI;
import me.robomonkey.versus.settings.Setting;
import me.robomonkey.versus.settings.Settings;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PAPIUtil {

    private static String nickname_key = "%essentials_nickname%";

    public static String getName(OfflinePlayer player) {

        if (Dependencies.PAPI_ENABLED &&
                Settings.is(Setting.ESSENTIALS_NICKNAMES_ENABLED) &&
                PlaceholderAPI.isRegistered("essentials")) {
            return PlaceholderAPI.setPlaceholders(player, nickname_key);
        }
        return player.getName();
    }

    public static String getName(Player player) {
        if (Dependencies.PAPI_ENABLED &&
                Settings.is(Setting.ESSENTIALS_NICKNAMES_ENABLED) &&
                PlaceholderAPI.isRegistered("essentials")) {
            return PlaceholderAPI.setPlaceholders(player, nickname_key);
        }
        return player.getName();
    }
}
