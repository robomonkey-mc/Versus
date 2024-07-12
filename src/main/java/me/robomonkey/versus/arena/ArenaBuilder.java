package me.robomonkey.versus.arena;

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
        currentProperty = ArenaProperty.CENTER_LOCATION;
        targetArena = new Arena(name);
        editor = new ArenaEditor(targetArena);
    }
    public void handleNextStep() {
        currentProperty = currentProperty.getNextProperty();
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
        coordinator.removeArenaBuilder(builder);
    }

    private boolean isAllPropertiesCompleted(){
        return currentProperty == null;
    }



}
