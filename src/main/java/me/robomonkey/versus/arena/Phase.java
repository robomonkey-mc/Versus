package me.robomonkey.versus.arena;

enum Phase {
    SET_CENTER_POSITION("center"),
    SET_FIRST_POSITION("first"),
    SET_SECOND_POSITION("second"),
    FINALIZE("complete");

    String arenaComandArgument;

    Phase(String arenaComamandArgumnet){
        this.arenaComandArgument = arenaComamandArgumnet;
    }

    public String getCommand() { return arenaComandArgument; }
    public Phase getNextPhase() {
        switch(this) {
            case SET_CENTER_POSITION:
                return SET_FIRST_POSITION;
            case SET_FIRST_POSITION:
                return SET_SECOND_POSITION;
            case SET_SECOND_POSITION:
                return SET_CENTER_POSITION;
            default:
                return null;
        }
    }
}
