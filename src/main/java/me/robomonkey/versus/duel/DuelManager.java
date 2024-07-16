package me.robomonkey.versus.duel;

public class DuelManager {
    private static DuelManager instance;

    public static DuelManager getInstance() {
        if (instance==null) {
            instance = new DuelManager();
        }
        return instance;
    }
}
