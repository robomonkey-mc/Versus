package me.robomonkey.versus.duel;

import me.robomonkey.versus.Versus;
import me.robomonkey.versus.arena.Arena;
import me.robomonkey.versus.arena.ArenaManager;
import me.robomonkey.versus.util.EffectUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class DuelManager {
    private static DuelManager instance;
    private Set<Duel> activeDuels = new HashSet<>();
    private HashMap<UUID, Duel> duelistMap = new HashMap<>();
    private ArenaManager arenaManager = ArenaManager.getInstance();
    private Versus plugin = Versus.getInstance();

    public static DuelManager getInstance() {
        if (instance==null) {
            instance = new DuelManager();
        }
        return instance;
    }

    public void registerQuitter() {
    }

    public Duel duelFromPlayer(Player player) {
        return duelistMap.get(player.getUniqueId());
    }

    public Boolean isDueling(Player player) {
        return duelistMap.containsKey(player.getUniqueId());
    }

    public void setupDuel(Player playerOne, Player playerTwo) {
        groomForDuel(playerOne);
        groomForDuel(playerTwo);
        saveData(playerOne);
        saveData(playerTwo);
        Duel newDuel = createNewDuel(playerOne, playerTwo);
        playerOne.teleport(newDuel.getArena().getSpawnLocationOne());
        playerTwo.teleport(newDuel.getArena().getSpawnLocationTwo());
        startCountdown(newDuel);
    }

    private Duel createNewDuel(Player playerOne, Player playerTwo) {
        Arena availableArena = arenaManager.getAvailableArena();
        Duel newDuel = new Duel(availableArena, playerOne, playerTwo);
        activeDuels.add(newDuel);
        arenaManager.registerDuel(availableArena, newDuel);
        return newDuel;
    }

    private void startCountdown(Duel duel){
        duel.setState(DuelState.COUNTDOWN);
        int countdownDuration = 0;
        String countdownMessage = "Hello";
        duel.getPlayers().stream().forEach((player) -> EffectUtil.freezePlayer(player, countdownDuration));
        Countdown countdown = new Countdown(countdownDuration, countdownMessage, () -> {
            duel.getPlayers().stream()
                    .forEach(player -> EffectUtil.unfreezePlayer(player));
            commenceDuel(duel);
        });
        countdown.initiateCountdown();
    }

    private void commenceDuel(Duel duel) {
        duel.setState(DuelState.ACTIVE);
        handleStartEffects(duel);
    }

    private void handleStartEffects(Duel duel){
        EffectUtil.spawnFireWorks(duel.getArena().getCenterLocation(), 1, 10);
        for(Player player: duel.getPlayers()){
            player.sendTitle(plugin.color("&6&lGO!"),player.getName().toUpperCase()+"!", 20, 20, 20);
            player.setInvulnerable(false);
        }
    }
    
    private void groomForDuel(Player player) {
        player.getActivePotionEffects().stream().forEach((effect) -> player.removePotionEffect(effect.getType()));
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 10));
        player.setHealth(20);
        player.closeInventory();
        player.setFoodLevel(20);
        player.setAbsorptionAmount(0);
        player.setGameMode(GameMode.SURVIVAL);
        player.setInvulnerable(true);
        player.setAllowFlight(false);
        player.setLevel(0);
    }

    private void saveData(Player player) {
        saveInventory(player);
        saveExp(player);
    }

    private void saveExp(Player player) {
        //TODO: Implement
    }

    private void saveInventory(Player player) {
        //TODO: Implement
    }

    public void registerMoveEvent(Player player, PlayerMoveEvent event) {
        Duel currentDuel = duelFromPlayer(player);
        if(currentDuel.getState() == DuelState.COUNTDOWN && isMoving(event)) {
            event.setCancelled(true);
        }
    }

    public boolean isMoving(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();
        if(to.getX()!=from.getX()||to.getY()!=from.getY()||to.getZ()!=from.getZ()) return true;
        return false;
    }

    public void stopDuel(Duel duel) {
        arenaManager.removeDuel(duel);
        activeDuels.remove(duel);
        
        Player loser = duel.getLoser();
        Player winner = duel.getWinner();
        
        if(winner != null) showWinningEffects(winner);
        if(loser != null) showLosingEffects(loser);
    }

    public void showWinningEffects(Player winner) {
        Bukkit.broadcastMessage(Main.colour(Main.prefix + " &3Congratulate&6 " + winner.getName() + "! &3They won this match against&6 " + loser.getName() + "!"));

        EffectUtil.spawnFireWorks(winner.getLocation(), 1, 50);
        winner.playSound(winner.getLocation(), Sound.MUSIC_DISC_PIGSTEP, Float.MAX_VALUE, 1F);
        winner.sendTitle(
                Versus.color("&6&lCONGRATULATIONS"),
                Versus.color("&6" + winner.getName().toUpperCase() + "!"),
                20,
                40,
                20);
        winner.setInvulnerable(true);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            winner.setItemOnCursor(null);
            winner.closeInventory();
            restoreInventory(winner);
            restoreExp(winner);
            winner.stopSound(Sound.MUSIC_DISC_PIGSTEP);
            sendToSpectate(winner, a);
            winner.setHealth(20);
            winner.setFoodLevel(20);
            winner.setInvulnerable(false);
            spawnFireWorksTimed(winner.getLocation(), 3, 20, 20L);
            activePlayers.remove(winner.getUniqueId());
            currentArena.remove(winner.getUniqueId());
            deleteItems(winner.getWorld());
            OfflinePlayer loseroff = Bukkit.getOfflinePlayer(loser.getUniqueId());
            showSkull(winner, loseroff);
            a.removePlayer(winner);
        }, 200L);
    }

    public void showLosingEffects(Player loser){
        loser.sendMessage("&c&lDon't worry about it &6&l"+loser.getName()+"&c&l. Type /ccspectate to return to the spectator area.");
        saveLoss(loser);
        loser.stopSound(Sound.MUSIC_DISC_BLOCKS);
        loser.getWorld().strikeLightningEffect(loser.getLocation());
    }
}
