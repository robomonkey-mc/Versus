package me.robomonkey.versus.arena;

public enum ArenaProperty {
    CENTER_LOCATION("center of the arena"),
    SPAWN_LOCATION_ONE("first spawn location"),
    SPAWN_LOCATION_TWO("second spawn location"),
    SPECTATE_LOCATION("location for spectators"),
    KIT("kit for players");


    private String friendlyString;

    ArenaProperty(String friendlyVersion) {
        friendlyString = friendlyVersion;
    }

    public ArenaProperty getNextProperty() {
        switch (this) {
            case CENTER_LOCATION:
                return SPAWN_LOCATION_ONE;
            case SPAWN_LOCATION_ONE:
                return SPAWN_LOCATION_TWO;
            case SPAWN_LOCATION_TWO:
                return SPECTATE_LOCATION;
            case SPECTATE_LOCATION:
                return KIT;
            default:
                return null;
        }
    }

    public String getExplanation() {
        switch (this) {
            case CENTER_LOCATION:
                return "Select the center of the arena, often located in the middle of both duelists.";
            case SPAWN_LOCATION_ONE:
                return "This determines whether the first player in a duel will spawn. ";
            case SPAWN_LOCATION_TWO:
                return "This determines where the second player in a duel will spawn";
            case SPECTATE_LOCATION:
                return "This determines where spectators will be teleported to watch a duel, and where the players will be sent after" +
                        " completing a duel.";
            case KIT:
                return "This determines the inventory players will have upon entering a duel in this arena.";
            default:
                return "";
        }
    }

    public static ArenaProperty fromString(String propertyName) {
        try {
            ArenaProperty fromString = ArenaProperty.valueOf(propertyName.toUpperCase());
            return fromString;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }

    public String toFriendlyString() {
        return friendlyString;
    }
}
