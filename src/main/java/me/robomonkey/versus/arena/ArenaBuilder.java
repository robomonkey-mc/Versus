package me.robomonkey.versus.arena;

import me.robomonkey.versus.util.MessageUtil;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class ArenaBuilder extends ArenaEditor {

    ArenaProperty currentProperty;
    Player builder;
    Arena targetArena;

    public ArenaBuilder(Player builder, String name){
        super(null);
        currentProperty = ArenaProperty.CENTER_LOCATION;
        targetArena = new Arena(name);
    }
    public void progressArenaConstruction() {
        currentProperty = currentProperty.getNextProperty();
        if(isAllPropertiesCompleted()){
            finalizeArena();
        } else {
            displayInstructionalMessage(currentProperty, p);
        }
    }

    private boolean isAllPropertiesCompleted(){
        return currentProperty == null;
    }



}
