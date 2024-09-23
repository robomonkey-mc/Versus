package me.robomonkey.versus.settings;

import me.robomonkey.versus.Versus;
import me.robomonkey.versus.duel.ReturnOption;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Setting {

    ERROR_PREFIX("messages-general.errors", Type.STRING),
    NO_PERMISSION_MESSAGE("messages-general.errors", Type.STRING),
    ONLY_PLAYERS_MESSAGE("messages-general.errors", Type.STRING),

    PREFIX("messages-general.admin", Type.STRING),
    PREFIX_ENABLED("messages-general.admin", Type.BOOLEAN),
    PRIMARY_COLOR("messages-general.admin", Type.MCCOLOR),
    HIGHLIGHTED_COLOR("messages-general.admin", Type.MCCOLOR),
    BOLD_COLOR("messages-general.admin", Type.MCCOLOR),
    SUBTLE_COLOR("messages-general.admin", Type.MCCOLOR),
    LINE("messages-general.admin", Type.STRING),

    DUEL_LOSS_MESSAGE("dueling.messages", Type.STRING),
    VICTORY_SUBTITLE_MESSAGE("dueling.messages", Type.STRING),
    VICTORY_TITLE_MESSAGE("dueling.messages", Type.STRING),
    DUEL_GO_MESSAGE("dueling.messages", Type.STRING),
    COUNTDOWN_TITLE("dueling.messages", Type.STRING),
    COUNTDOWN_MESSAGE("dueling.messages", Type.STRING),
    DUEL_SPECTATE_MESSAGE("dueling.messages", Type.STRING),

    RETURN_WINNERS("dueling.mechanics", Type.RETURNOPTIONS),
    WINNER_RETURN_LOCATION("dueling.mechanics", Type.LOCATION),
    INSTANT_RESPAWN("dueling.mechanics", Type.BOOLEAN),
    PERMISSION_REQUIRED_TO_DUEL("dueling.mechanics", Type.BOOLEAN),
    RETURN_LOSERS("dueling.mechanics", Type.RETURNOPTIONS),
    LOSER_RETURN_LOCATION("dueling.mechanics", Type.LOCATION),
    BLOCKED_COMMANDS("dueling.mechanics", Type.INVALID),
    ALLOW_BLOCK_PLACEMENTS("dueling.mechanics", Type.BOOLEAN),
    COUNTDOWN_DURATION("dueling.mechanics", Type.NUMBER),
    ALLOW_BLOCK_DESTRUCTION("dueling.mechanics", Type.BOOLEAN),

    ANNOUNCE_DUELS("dueling.announcements", Type.BOOLEAN),
    DUEL_START_ANNOUNCEMENT("dueling.announcements", Type.STRING),
    DUEL_END_ANNOUNCEMENT("dueling.announcements", Type.STRING),

    VICTORY_EFFECTS_ENABLED("dueling.effects", Type.BOOLEAN),
    VICTORY_EFFECTS_DURATION("dueling.effects", Type.BOOLEAN),
    FIREWORKS_ENABLED("dueling.effects", Type.BOOLEAN),
    FIREWORKS_COLOR("dueling.effects", Type.COLOR),
    BLINDNESS_EFFECTS_ENABLED("dueling.effects", Type.BOOLEAN),

    FIGHT_MUSIC_ENABLED("dueling.music", Type.BOOLEAN),
    FIGHT_MUSIC("dueling.music", Type.BOOLEAN),
    VICTORY_MUSIC_ENABLED("dueling.music", Type.BOOLEAN),
    VICTORY_MUSIC("dueling.music", Type.MUSIC),

    SHIFT_CLICK_REQUESTING_ENABLED("requesting.mechanics", Type.BOOLEAN),

    DENIED_REQUEST("requesting.messages", Type.STRING),
    DENIED_REQUEST_CONFIRMATION("requesting.messages", Type.STRING),
    CANCEL_REQUEST("requesting.messages", Type.STRING),
    SENT_REQUEST("requesting.messages", Type.STRING),
    REQUEST_NOTIFICATION("requesting.messages", Type.STRING),
    ACCEPT_BUTTON("requesting.messages", Type.STRING),
    NO_ARENAS_AVAILABLE("requesting.messages", Type.STRING),
    DENY_BUTTON("requesting.messages", Type.STRING),

    ESSENTIALS_NICKNAMES_ENABLED("dependencies.placeholderAPI", Type.BOOLEAN),
    ITEMS_ADDER_FOR_KITS("dependencies.itemsadder", Type.BOOLEAN);

    public Object value;
    public String path;
    public Type type;

    Setting(String path, Type type) {
        this.path = path + "." + this.toString().toLowerCase();
        this.type = type;
    }

    public Object getValue() {
        if (value == null) {
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
     *
     * @param value
     * @return
     */
    public boolean setValue(Object value) {
        if (value.getClass() == myClass()) {
            this.value = value;
            return true;
        } else {
            return false;
        }
    }

    public Class myClass() {
        Object savedDefault = Versus.getInstance().getConfig().getDefaults().get(getPath());
        return savedDefault.getClass();
    }

    public Type getType() {
        return this.type;
    }

    public enum Type {
        BOOLEAN(List.of("true", "false")),
        STRING(List.of("<message>")),
        LOCATION(List.of("<x> <y> <z> <world name>")),
        MUSIC(List.of("creative", "credits", "disc_5", "disc_11", "disc_13", "disc_blocks",
                "disc_cat", "disc_chirp", "disc_creator", "disc_creator_music_box", "disc_far",
                "disc_mall", "disc_mellohi", "disc_otherside", "disc_pigstep", "disc_precipice",
                "disc_relic", "disc_stal", "disc_strad", "disc_wait", "disc_ward")),
        RETURNOPTIONS(Arrays.stream(ReturnOption.values()).map(value -> value.toString()).collect(Collectors.toList())),
        COLOR(List.copyOf(Settings.colorMap.keySet())),
        MCCOLOR(List.of("&0", "&1", "&2", "&3", "&4", "&5", "&6", "&7", "&8", "&9", "&a", "&b", "&c", "&d", "&e", "&f")),
        NUMBER(List.of("<number>")),
        INVALID(null);
        List<String> options;

        Type(List<String> autoCompletions) {
            options = autoCompletions;
        }

        public List<String> getOptions() {
            return options;
        }
    }

}

