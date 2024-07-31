package me.robomonkey.versus.settings;

public class Placeholder {
    public String holder;
    public String replacement;

    public Placeholder(String placeholder, String replacement) {
        this.holder = placeholder;
        this.replacement = replacement;
    }

    public static Placeholder of(String placeholder, Object replacement) {
        return new Placeholder(placeholder, String.valueOf(replacement));
    }
}
