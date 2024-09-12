package me.robomonkey.versus.dependency;

import me.robomonkey.versus.settings.Setting;
import me.robomonkey.versus.settings.Settings;
import org.bukkit.Server;

public class Dependencies {
    public static boolean PAPI_ENABLED = false;
    public static boolean ITEMS_ADDER_ENABLED = false;


    public static void refresh(Server currServer) {
        PAPI_ENABLED = currServer.getPluginManager().getPlugin("PlaceholderAPI") != null;
        ITEMS_ADDER_ENABLED = currServer.getPluginManager().getPlugin("ItemsAdder") != null && Settings.is(Setting.ITEMS_ADDER_FOR_KITS);
    }
}
