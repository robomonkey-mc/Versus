package me.robomonkey.versus.duel;

import me.robomonkey.versus.Versus;
import me.robomonkey.versus.arena.Arena;
import me.robomonkey.versus.arena.ArenaManager;
import me.robomonkey.versus.util.EffectUtil;
import org.bukkit.GameMode;
import org.bukkit.Location;
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
        this.activePlayers.remove(loser.getUniqueId());
        this.currentArena.remove(loser.getUniqueId());
        arenaManager.removeDuel(duel);
        a.removePlayer(loser);
        loser.sendMessage(Main.prefix+" Don't worry about it "+ChatColor.GOLD+ChatColor.BOLD+loser.getName()+ChatColor.DARK_AQUA+ChatColor.BOLD+". Type /ccspectate to return to the spectator area.");
        saveLoss(loser);
        loser.stopSound(Sound.MUSIC_DISC_BLOCKS);
        Player p = Bukkit.getPlayer(a.players.get(0));
        saveVictory(p);
        p.setNoDamageTicks(260);
        loser.getWorld().strikeLightningEffect(loser.getLocation());
        Bukkit.broadcastMessage(Main.colour(Main.prefix+" &3Congratulate&6 "+p.getName()+"! &3They won this match against&6 "+loser.getName()+"!"));
        spawnFireWorks(p.getLocation(), 10, 50);
        p.playSound(p.getLocation(), Sound.MUSIC_DISC_PIGSTEP, Float.MAX_VALUE, 1F);
        p.sendTitle(Main.colour("&6&lCONGRATULATIONS"),ChatColor.DARK_AQUA+p.getName().toUpperCase()+"!", 20, 40, 20);
        p.setInvulnerable(true);
        a.winning=Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            public void run() {
                p.setItemOnCursor(null);
                p.closeInventory();
                restoreInventory(p);
                restoreExp(p);
                p.stopSound(Sound.MUSIC_DISC_PIGSTEP);
                sendToSpectate(p, a);
                p.setHealth(20);
                p.setFoodLevel(20);
                p.setInvulnerable(false);
                spawnFireWorksTimed(p.getLocation(), 3, 20, 20L);
                activePlayers.remove(p.getUniqueId());
                currentArena.remove(p.getUniqueId());
                deleteItems(p.getWorld());
                OfflinePlayer loseroff = Bukkit.getOfflinePlayer(loser.getUniqueId());
                showSkull(p, loseroff);
                a.removePlayer(p);
            }
        }, 200L);
    }
}
