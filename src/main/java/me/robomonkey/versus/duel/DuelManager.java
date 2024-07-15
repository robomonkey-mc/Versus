package me.robomonkey.versus.duel;

public class DuelManager {
    private DuelManager instance;

    public static DuelManager getInstance() {
        if (instance==null) {
            instance = new DuelManager();
        }
        return instance;
    }
}
