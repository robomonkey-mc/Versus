package me.robomonkey.versus.arena;

import me.robomonkey.versus.Versus;
import me.robomonkey.versus.util.MessageUtil;
import net.md_5.bungee.api.chat.TextComponent;
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
        editor.alterArenaProperty(currentProperty, builder);
        if(currentProperty == property) handleNextStep();
    }

    private void finalizeArena(){
        builder.sendMessage("You have completed the construction of the "+targetArena.getName()+" arena!");
        ArenaManager.getInstance().addArena(targetArena);
        coordinator.removeArenaBuilder(builder);
    }

    private boolean isAllPropertiesCompleted(){
        return currentProperty == null;
    }



}
