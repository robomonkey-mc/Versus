package me.robomonkey.versus.duel;

import me.robomonkey.versus.Versus;
import me.robomonkey.versus.arena.Arena;
import me.robomonkey.versus.arena.ArenaManager;
import me.robomonkey.versus.duel.eventlisteners.*;
import me.robomonkey.versus.util.EffectUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class DuelManager {
    private static DuelManager instance;
    private Set<Duel> activeDuels = new HashSet<>();
    private HashMap<UUID, Duel> duelistMap = new HashMap<>();
    private ArenaManager arenaManager = ArenaManager.getInstance();
    private InventoryManager inventoryManager;
    private Versus plugin = Versus.getInstance();

    private DuelManager() {
        instance = this;
        inventoryManager = new InventoryManager();
        inventoryManager.loadInventoryMap();
        registerListeners();
    }

    public static DuelManager getInstance() {
        if (instance==null) {
            new DuelManager();
        }
        return instance;
    }

    public void addDuel(Duel duel){
        duel.getPlayers().forEach(player -> duelistMap.put(player.getUniqueId(), duel));
    }

    private void removeDuel(Duel duel) {
        duelistMap.values().removeIf(value -> value.equals(duel));
    }

    public Duel getDuel(Player player) {
        return duelistMap.get(player.getUniqueId());
    }

    public void registerQuitter(Player quitter) {
        quitter.setHealth(0);
    }

    public boolean hasStoredInventory(Player player) {
        return inventoryManager.contains(player);
    }

    public Boolean isDueling(Player player) {
        return duelistMap.containsKey(player.getUniqueId());
    }

    public boolean isMoving(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();
        if(to.getX()!=from.getX()||to.getY()!=from.getY()||to.getZ()!=from.getZ()) return true;
        return false;
    }

    public void restoreInventory(Player player) {
        if(inventoryManager.contains(player)) {
            ItemStack[] items = inventoryManager.retrieveInventory(player);
            player.getInventory().setContents(items);
        }
    }

    public void setupDuel(Player playerOne, Player playerTwo) {
        groomForDuel(playerOne);
        groomForDuel(playerTwo);
        saveData(playerOne);
        saveData(playerTwo);
        Duel newDuel = createNewDuel(playerOne, playerTwo);
        playerOne.teleport(newDuel.getArena().getSpawnLocationOne());
        playerTwo.teleport(newDuel.getArena().getSpawnLocationTwo());
        newDuel.startCountdown(() -> commenceDuel(newDuel));
    }

    public void registerMoveEvent(Player player, PlayerMoveEvent event) {
        Duel currentDuel = getDuel(player);
        if(currentDuel.getState() == DuelState.COUNTDOWN && isMoving(event)) {
            event.setCancelled(true);
        }
    }

    /**
     * Only call after checking that ensuring that the player is currently in a duel with duelManager.duelFromPlayer(..);
     * @param event PlayerDeathEvent
     */
    public void registerDuelistDeath(PlayerDeathEvent event) {
        Player loser = event.getEntity();
        Duel currentDuel = getDuel(loser);
        if(currentDuel.getState() == DuelState.COUNTDOWN) {
            undoCountdown(currentDuel);
        }
        if(currentDuel.isActive()){
            registerDuelCompletion(loser, currentDuel);
        }
    }

    private void registerDuelCompletion(Player loser, Duel duel) {
        Optional<Player> optionalWinner = duel.getPlayers().stream().filter(player -> !player.equals(loser)).findFirst();
        if(!optionalWinner.isPresent()) return;
        Player winner = optionalWinner.get();
        duel.end(winner, loser);
        stopDuel(duel);
    }

    private void stopDuel(Duel duel) {
        if(duel.isActive()) return;
        arenaManager.removeDuel(duel);
        activeDuels.remove(duel);
        Player loser = duel.getLoser();
        Player winner = duel.getWinner();

        if(winner != null) {
            renderWinEffects(winner, duel);
        }
        if(loser != null) {
            renderLossEffects(loser);
        }
    }

    private Duel createNewDuel(Player playerOne, Player playerTwo) {
        Arena availableArena = arenaManager.getAvailableArena();
        Duel newDuel = new Duel(availableArena, playerOne, playerTwo);
        addDuel(newDuel);
        arenaManager.registerDuel(availableArena, newDuel);
        return newDuel;
    }

    private void commenceDuel(Duel duel) {
        duel.setState(DuelState.ACTIVE);
        handleStartEffects(duel);
    }

    private void handleStartEffects(Duel duel){
        EffectUtil.spawnFireWorks(duel.getArena().getCenterLocation(), 1, 10);
        for(Player player: duel.getPlayers()){
            EffectUtil.sendTitle(player, "&6&lGO!", 20, true);
            player.setInvulnerable(false);
            player.playSound(player.getLocation(), Sound.MUSIC_DISC_FAR, Float.POSITIVE_INFINITY, 2.0F);
        }
    }

    private void groomForDuel(Player player) {
        resetAttributes(player);
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 10));
        player.setGameMode(GameMode.SURVIVAL);
        player.setInvulnerable(true);
        player.setAllowFlight(false);
        player.setLevel(0);
    }

    private void saveData(Player player) {
        saveInventory(player);
        saveExp(player);
        inventoryManager.saveInventoryMap();
    }

    private void saveExp(Player player) {
        //TODO: Implement
    }

    private void saveInventory(Player player) {
        inventoryManager.addInventory(player);
        ItemStack[] items = {new ItemStack(Material.DIAMOND_AXE, 1),
                new ItemStack(Material.CROSSBOW),
                new ItemStack(Material.ARROW, 5),
                new ItemStack(Material.GOLDEN_APPLE, 1)};
        ItemStack[] armor = {
                new ItemStack(Material.DIAMOND_BOOTS),
                new ItemStack(Material.DIAMOND_LEGGINGS),
                new ItemStack(Material.DIAMOND_CHESTPLATE),
                new ItemStack(Material.DIAMOND_HELMET),
        };
        player.getInventory().setContents(items);
        player.getInventory().setArmorContents(armor);
    }

    private void registerListeners() {
        Listener[] listeners = {new JoinEventListener(), new RespawnEventListener(), new DeathEventListener(), new MoveEventListener(), new QuitEventListener(), new FireworkExplosionListener()};
        Arrays.stream(listeners).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, Versus.getInstance()));
    }

    private void undoCountdown(Duel duel) {
        duel.cancelCountdown();
        duel.getPlayers().forEach(EffectUtil::unfreezePlayer);
    }

    private void extricateWinner(Player player, Duel duel) {
        restoreInventory(player);
        resetAttributes(player);
        arenaManager.sendToSpectatingArea(player, duel.getArena());
        removeDuel(duel);
    }

    private void resetAttributes(Player player) {
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setInvulnerable(false);
        EffectUtil.unfreezePlayer(player);
        player.getActivePotionEffects().stream().forEach(effect -> player.removePotionEffect(effect.getType()));
        player.setItemOnCursor(null);
        player.closeInventory();
        player.stopAllSounds();
    }

    private void renderWinEffects(Player winner, Duel duel) {
        // Bukkit.broadcastMessage(Main.colour(Main.prefix + " &3Congratulate&6 " + winner.getName() + "! &3They won this match against&6 " + loser.getName() + "!"));
        EffectUtil.spawnFireWorks(winner.getLocation(), 1, 50);
        EffectUtil.playSound(winner, Sound.MUSIC_DISC_PIGSTEP);
        winner.sendTitle(Versus.color("&6&lCONGRATULATIONS"), Versus.color("&6" + winner.getName().toUpperCase() + "!"), 20, 40, 20);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            //TODO Delete nearby items on the ground if necessary.
            //TODO Implement skull system if you want later on
            extricateWinner(winner, duel);
            EffectUtil.spawnFireWorksDelayed(winner.getLocation(), 3, 20, 20L);
        }, 200L);
    }

    private void renderLossEffects(Player loser) {
        loser.sendMessage("&c&lDon't worry about it &6&l"+loser.getName()+"&c&l. Type /ccspectate to return to the spectator area.");
        loser.getWorld().strikeLightningEffect(loser.getLocation());
    }

}
