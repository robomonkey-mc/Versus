package me.robomonkey.versus.arena;

enum ArenaProperty {
    CENTER_LOCATION("center of the arena"),
    SPAWN_LOCATION_ONE("first spawn location"),
    SPAWN_LOCATION_TWO("second spawn location"),
    SPECTATE_LOCATION("location for spectators");


    private String friendlyString;

    ArenaProperty(String friendlyVersion){
        friendlyString = friendlyVersion;
    }

    public ArenaProperty getNextProperty() {
        switch(this) {
            case CENTER_LOCATION:
                return SPAWN_LOCATION_ONE;
            case SPAWN_LOCATION_ONE:
                return SPAWN_LOCATION_TWO;
            case SPAWN_LOCATION_TWO:
                return SPECTATE_LOCATION;
            default:
                return null;
        }
    }

    @Override
    public String toString(){
        return super.toString().toLowerCase();
    }

    public String toFriendlyString(){
        return friendlyString;
    }
}
