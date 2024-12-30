package me.robomonkey.versus.duel.options.bets;

import me.robomonkey.versus.arena.Arena;
import me.robomonkey.versus.settings.Setting;
import me.robomonkey.versus.settings.Settings;
import org.bukkit.Color;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

public class DuelOptions {

    public static DuelOptions DEFAULT = new DuelOptions(null, null, false, 20, false);

    //TODO FIX: Currently, since DEFAULT is instantiated when DuelOptions.java is first loaded, changes to default dueling
    // options won't propagate when updated live.
    private boolean fightMusicEnabled = Settings.is(Setting.FIGHT_MUSIC_ENABLED);
    private boolean victoryMusicEnabled = Settings.is(Setting.VICTORY_MUSIC_ENABLED);
    private boolean victoryEffectsEnabled = Settings.is(Setting.VICTORY_EFFECTS_ENABLED);
    private boolean fireworksEnabled = Settings.is(Setting.FIREWORKS_ENABLED);
    private Color fireworkColor = Settings.getColor(Setting.FIREWORKS_COLOR);
    private Sound victorySong = Settings.getSong(Setting.VICTORY_MUSIC);
    private Sound fightMusic = Settings.getSong(Setting.FIGHT_MUSIC);
    private boolean blindnessEnabled = Settings.is(Setting.BLINDNESS_EFFECTS_ENABLED);
    private boolean isPublic = Settings.is(Setting.ANNOUNCE_DUELS);

    private Bet bet;
    private boolean destructibleArena = false;
    private double maxHealth = 0;
    private boolean local = false;
    private ItemStack[] kit;
    private boolean useInventory = false;
    private Arena arena;

    public DuelOptions(ItemStack[] kit, Bet bet, boolean destructibleArena, double maxHealth, boolean local) {
        this.kit = kit;
        this.bet = bet;
        this.destructibleArena = destructibleArena;
        this.maxHealth = maxHealth;
        this.local = local;
    }

    public void setUseInventory(boolean useInventory) {
        this.useInventory = useInventory;
    }

    public boolean isLocal() {
        return local;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }

    public void setArena(Arena arena) {
        this.arena = arena;
    }

    public Arena getArena() {
        return arena;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
    }

    public boolean isDestructibleArena() {
        return destructibleArena;
    }

    public void setDestructibleArena(boolean destructibleArena) {
        this.destructibleArena = destructibleArena;
    }

    public Bet getBet() {
        return bet;
    }

    public void setBet(Bet bet) {
        this.bet = bet;
    }

    public ItemStack[] getKit() {
        return kit;
    }

    public void setKit(ItemStack[] kit) {
        this.kit = kit;
    }

    public boolean isFireworksEnabled() {
        return fireworksEnabled;
    }

    public boolean isVictoryEffectsEnabled() {
        return victoryEffectsEnabled;
    }

    public Sound getFightMusic() {
        return fightMusic;
    }

    public Sound getVictorySong() {
        return victorySong;
    }

    public boolean isFightMusicEnabled() {
        return this.fightMusicEnabled;
    }

    public boolean isVictoryMusicEnabled() {
        return this.victoryMusicEnabled;
    }

    public Color getFireworkColor() {
        return (fireworkColor == null) ? Color.ORANGE : fireworkColor;
    }

    public boolean isPublic() { return this.isPublic; }

}
