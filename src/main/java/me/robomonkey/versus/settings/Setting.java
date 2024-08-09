package me.robomonkey.versus.settings;

import me.robomonkey.versus.Versus;

import java.util.Arrays;

public enum Setting {

    ERROR_PREFIX("messages-general.errors"),
    NO_PERMISSION_MESSAGE("messages-general.errors"),
    ONLY_PLAYERS_MESSAGE("messages-general.errors"),

    PREFIX("messages-general.admin"),
    PREFIX_ENABLED("messages-general.admin"),
    PRIMARY_COLOR("messages-general.admin"),
    HIGHLIGHTED_COLOR("messages-general.admin"),
    BOLD_COLOR("messages-general.admin"),
    SUBTLE_COLOR("messages-general.admin"),
    LINE("messages-general.admin"),

    DUEL_LOSS_MESSAGE("dueling.messages"),
    VICTORY_SUBTITLE_MESSAGE("dueling.messages"),
    VICTORY_TITLE_MESSAGE("dueling.messages"),
    DUEL_GO_MESSAGE("dueling.messages"),
    COUNTDOWN_TITLE("dueling.messages"),
    COUNTDOWN_MESSAGE("dueling.messages"),
    DUEL_SPECTATE_MESSAGE("dueling.messages"),

    BLOCKED_COMMANDS("dueling.mechanics"),
    COUNTDOWN_DURATION("dueling.mechanics"),

    VICTORY_EFFECTS_ENABLED("dueling.effects"),
    FIREWORKS_ENABLED("dueling.effects"),
    FIREWORKS_COLOR("dueling.effects"),
    BLINDNESS_EFFECTS_ENABLED("dueling.effects"),

    FIGHT_MUSIC_ENABLED("dueling.music"),
    FIGHT_MUSIC("dueling.music"),
    VICTORY_MUSIC_ENABLED("dueling.music"),
    VICTORY_MUSIC("dueling.music"),

    DENIED_REQUEST("requesting.messages"),
    DENIED_REQUEST_CONFIRMATION("requesting.messages"),
    CANCEL_REQUEST("requesting.messages"),
    SENT_REQUEST("requesting.messages"),
    REQUEST_NOTIFICATION("requesting.messages"),
    ACCEPT_BUTTON("requesting.messages"),
    NO_ARENAS_AVAILABLE("requesting.messages"),
    DENY_BUTTON("requesting.messages");

    public Object value;
    public String path;

    Setting(String path) {
        this.path = path + "."+ this.toString().toLowerCase();
    }

    public Object getValue() {
        if(value == null) {
            return getDefaultValue();
        }
        return value;
    }

    public Object getDefaultValue() {
        return Versus.getInstance().getConfig().getDefaults().get(getPath());
    }

    public String getPath() {
        return this.path;
    }

    /**
     * Returns whether a value was successfully set
     * @param value
     * @return
     */
    public boolean setValue(Object value) {
       if(value.getClass() == getType()) {
           this.value = value;
           return true;
       } else {
           return false;
       }
    }

    public Class getType(){
        Object savedDefault = Versus.getInstance().getConfig().getDefaults().get(getPath());
        return savedDefault.getClass();
    }

}

