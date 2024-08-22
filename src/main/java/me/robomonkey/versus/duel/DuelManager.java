package me.robomonkey.versus.duel;

import me.robomonkey.versus.Versus;
import me.robomonkey.versus.arena.Arena;
import me.robomonkey.versus.arena.ArenaManager;
import me.robomonkey.versus.duel.eventlisteners.*;
import me.robomonkey.versus.duel.playerdata.DataManager;
import me.robomonkey.versus.duel.playerdata.PlayerData;
import me.robomonkey.versus.kit.Kit;
import me.robomonkey.versus.duel.request.RequestManager;
import me.robomonkey.versus.settings.Placeholder;
import me.robomonkey.versus.settings.Setting;
import me.robomonkey.versus.settings.Settings;
import me.robomonkey.versus.util.EffectUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class DuelManager {
    private static DuelManager instance;
    private Set<Duel> activeDuels = new HashSet<>();
    private HashMap<UUID, Duel> duelistMap = new HashMap<>();
    private ArenaManager arenaManager = ArenaManager.getInstance();
    private DataManager dataManager;
    private Versus plugin = Versus.getInstance();

    private DuelManager() {
        instance = this;
        dataManager = new DataManager();
        dataManager.loadDataMap();
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

    public boolean hasStoredData(Player player) {
        return dataManager.contains(player);
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

    public void restoreData(Player player, boolean isWinner) {
        if(!player.isOnline()) return;
        Versus.log("Attempting to restore data");
        if(!dataManager.contains(player)) return;
        PlayerData data = dataManager.extractData(player);
        player.setLevel(data.xpLevel);
        player.setExp(data.xpProgress);
        player.getInventory().setContents(data.items);
        ReturnOption returnOption = isWinner? Settings.getReturnOption(Setting.RETURN_WINNERS): Settings.getReturnOption(Setting.RETURN_LOSERS);
        restoreLocation(player, returnOption, data);
    }

    private void restoreLocation(Player player, ReturnOption returnOption, PlayerData data) {
        switch(returnOption) {
            case SPAWN:
                if(player.getRespawnLocation() == null) player.teleport(player.getWorld().getSpawnLocation());
                else player.teleport(player.getRespawnLocation());
                break;
            case PREVIOUS:
                player.teleport(data.previousLocation.toLocation());
                break;
            case SPECTATE:
                Arena respawnArena = arenaManager.getArena(data.arenaName);
                if(respawnArena == null) player.teleport(player.getRespawnLocation());
                else player.teleport(respawnArena.getSpectateLocation());
                break;
        }
    }

    public void setupDuel(Player playerOne, Player playerTwo) {
        Duel newDuel = createNewDuel(playerOne, playerTwo);
        newDuel.getPlayers().stream().forEach((player) -> {
            dataManager.save(player, newDuel.getArena());
            dataManager.saveDataMap();
            groomForDuel(player);
        });
        populateKits(newDuel);
        playerOne.teleport(newDuel.getArena().getSpawnLocationOne());
        playerTwo.teleport(newDuel.getArena().getSpawnLocationTwo());
        newDuel.startCountdown(() -> commenceDuel(newDuel));
    }

    private void populateKits(Duel duel) {
        duel.getPlayers().forEach((player) -> {
            Kit kit = duel.getArena().getKit();
            player.getInventory().setContents(kit.getItems());
        });
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
        duel.getPlayers().stream().filter(Player::isOnline).forEach(Player::stopAllSounds);
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
        if(duel.isFireworksEnabled()) EffectUtil.spawnFireWorks(duel.getArena().getCenterLocation(), 1, 10, duel.getFireworkColor());
        for(Player player: duel.getPlayers()){
            EffectUtil.sendTitle(player, Settings.getMessage(Setting.DUEL_GO_MESSAGE), 20, true);
            player.setInvulnerable(false);
            player.playSound(player.getLocation(), duel.getFightMusic(), Float.POSITIVE_INFINITY, 2.0F);
        }
    }

    private void groomForDuel(Player player) {
        resetAttributes(player);
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 10));
        player.setGameMode(GameMode.SURVIVAL);
        player.setInvulnerable(true);
        player.setAllowFlight(false);
        player.setLevel(0);
        player.setExp(0);
    }

    private void registerListeners() {
        List.of(new CommandListener(),
                new JoinEventListener(),
                new RespawnEventListener(),
                new DeathEventListener(),
                new MoveEventListener(),
                new QuitEventListener(),
                new FireworkExplosionListener(),
                new BlockBreakListener(),
                new BlockPlaceListener())
                .forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, Versus.getInstance()));
    }

    private void undoCountdown(Duel duel) {
        duel.cancelCountdown();
        duel.getPlayers().forEach(EffectUtil::unfreezePlayer);
    }

    private void extricateWinner(Player player, Duel duel) {
        restoreData(player, true);
        resetAttributes(player);
        removeDuel(duel);
        RequestManager.getInstance().notifyDuelCompletion();
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
        if(duel.isFireworksEnabled()) EffectUtil.spawnFireWorks(winner.getLocation(), 1, 50, duel.getFireworkColor());
        EffectUtil.playSound(winner, duel.getVictorySong());
        winner.sendTitle(
                Settings.getMessage(Setting.VICTORY_TITLE_MESSAGE),
                Settings.getMessage(Setting.VICTORY_SUBTITLE_MESSAGE, Placeholder.of("%player%", winner.getName())), 20, 40, 20);
        if(duel.isVictoryEffectsEnabled()) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                //TODO Delete nearby items on the ground if necessary.
                //TODO Implement skull system if you want later on
                extricateWinner(winner, duel);
                if(duel.isFireworksEnabled()) EffectUtil.spawnFireWorksDelayed(winner.getLocation(), 3, 20, 20L, duel.getFireworkColor());
            }, 200L);
        } else {
            extricateWinner(winner, duel);
        }
    }

    private void renderLossEffects(Player loser) {
        loser.sendMessage(Settings.getMessage(Setting.DUEL_LOSS_MESSAGE, Placeholder.of("%player%", loser.getName())));
        loser.getWorld().strikeLightningEffect(loser.getLocation());

    }

}
