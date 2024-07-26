package me.robomonkey.versus.arena;

import me.robomonkey.versus.Versus;
import me.robomonkey.versus.util.EffectUtil;
import me.robomonkey.versus.util.MessageUtil;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Color;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class ArenaBuilder {
    ArenaProperty currentProperty;
    Player builder;
    Arena targetArena;
    ArenaEditor editor;
    ArenaBuilderCoordinator coordinator = ArenaBuilderCoordinator.getInstance();

    public ArenaBuilder(Player builder, String name){
        targetArena = new Arena(name);
        editor = new ArenaEditor(targetArena);
        this.builder = builder;
    }
    public void handleNextStep() {
        currentProperty = (currentProperty == null)? ArenaProperty.CENTER_LOCATION : currentProperty.getNextProperty();
        Versus.log("current property is "+currentProperty);
        if(isAllPropertiesCompleted()){
            finalizeArena();
        } else {
            editor.displayInstructionalMessage(currentProperty, builder);
        }
    }

    public void handleArenaEdit(ArenaProperty property){
        editor.alterArenaProperty(property, builder);
        if(currentProperty == property) handleNextStep();
    }

    private void finalizeArena(){
        builder.sendMessage(Versus.color("&eYou have completed the construction of the &6"+targetArena.getName()+"&e arena!"));
        EffectUtil.playSound(builder, Sound.ENTITY_CAT_AMBIENT);
        EffectUtil.spawnFireWorks(builder.getLocation(), Color.YELLOW);
        ArenaManager.getInstance().addArena(targetArena);
        coordinator.removeArenaBuilder(builder);
    }

    private boolean isAllPropertiesCompleted(){
        return currentProperty == null;
    }



}
