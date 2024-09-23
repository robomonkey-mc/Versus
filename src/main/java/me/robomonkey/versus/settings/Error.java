package me.robomonkey.versus.settings;

public enum Error {
    SPECIFY_A_PLAYER("commands.general"),
    IS_NOT_ONLINE("commands.general"),
    DUEL_SELF("commands.duel"),
    NO_INCOMING_REQUESTS("commands.duel"),
    NOT_RECEIVED_REQUEST("commands.duel"),
    CANNOT_DUEL("commands.duel"),
    PLAYER_NO_LONGER_ONLINE("commands.duel"),
    DUEL_WHILE_QUEUEING("commands.duel"),
    WAIT_FOR_RESPONSE("commands.duel"),
    NOT_QUEUEING("commands.duel.cancel"),
    SPECTATING_WHILE_DUELING("commands.spectate"),
    SPECTATE_YOURSELF("commands.spectate"),
    IS_NOT_DUELING("commands.spectate");

    private String path;

    Error(String path) {
        this.path = path;
    }

    public String getPath() {
        return path + "." + this.toString().toLowerCase() + "_error";
    }
}
