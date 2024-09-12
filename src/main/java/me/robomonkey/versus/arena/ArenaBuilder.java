package me.robomonkey.versus.arena;

import me.robomonkey.versus.settings.Setting;
import me.robomonkey.versus.settings.Settings;
import me.robomonkey.versus.util.EffectUtil;
import me.robomonkey.versus.util.MessageUtil;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class ArenaBuilder {
    private ArenaProperty currentProperty;
    private Player builder;
    private Arena targetArena;
    private ArenaBuilderCoordinator coordinator = ArenaBuilderCoordinator.getInstance();

    public Arena getTargetArena() {
        return targetArena;
    }

    public Player getBuilder() {
        return builder;
    }

    public ArenaBuilder(Player builder, String name) {
        targetArena = new Arena(name);
        this.builder = builder;
    }

    public void handleNextStep() {
        currentProperty = (currentProperty == null) ? ArenaProperty.CENTER_LOCATION : currentProperty.getNextProperty();
        if (isAllPropertiesCompleted()) {
            finalizeArena();
        } else {
            ArenaEditor.displayInstructionalMessage(targetArena, currentProperty, builder);
            builder.playSound(builder.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F);
        }
    }

    public void handleArenaEdit(ArenaProperty property) {
        ArenaEditor.changeArenaProperty(targetArena, property, builder, () -> {
            if (currentProperty == property) handleNextStep();
        });
    }

    private void finalizeArena() {
        builder.sendMessage(MessageUtil.get("&pYou have completed the construction of the &h" + targetArena.getName() + "&p arena!"));
        EffectUtil.playSound(builder, Sound.ENTITY_CAT_AMBIENT);
        EffectUtil.spawnFireWorks(builder.getLocation(), Settings.getColor(Setting.FIREWORKS_COLOR));
        ArenaManager.getInstance().addArena(targetArena);
        coordinator.removeArenaBuilder(builder);
    }

    private boolean isAllPropertiesCompleted() {
        return currentProperty == null;
    }


}
