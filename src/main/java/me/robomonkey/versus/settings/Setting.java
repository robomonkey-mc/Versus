package me.robomonkey.versus.settings;

public enum Setting {

    CooldownEnterMessage("&6[&4PassiveMode&6]&e You will enter &6passive mode &ein &4%seconds% seconds."),
    CooldownExitMessage("&6[&4PassiveMode&6]&e You will exit &6passive mode &ein &4%seconds% seconds."),
    CooldownFailureMessage("&bYou cannot move during the &3cooldown!"),
    CooldownSpamMessage("&4You must wait until the command is finished!"),
    PassiveEnterMessage("&6[&4PassiveMode&6]&e You have entered &6passive mode!"),
    PassiveLeaveMessage("&6[&4PassiveMode&6]&e You have left &6passive mode!"),
    AttackPassiveMessage("&6[&4PassiveMode&6]&e This player is in &6passive mode!"),
    AttackWhilePassiveMessage("&6[&4PassiveMode&6]&e You cannot attack while in &6passive mode!"),
    JoinWhenPassiveMessage("&a&lYou are still in &6&lpassive mode&a&l. Type &6&l/passive &a&lto leave."),
    PVPCooldownMessage("&4You cannot use /passive during pvp! Please wait %seconds% more seconds."),
    PeriodicRemindersMessage("&a&lYou are still in &6&lpassive mode&a&l. Type &6&l/passive &a&lto leave."),
    CooldownTime(5),
    CooldownPassiveAfterAfkDelay(5),
    PVPCooldownLength(5),
    PeriodicRemindersPeriod(300),
    CooldownActive(true),
    CanLookDuringCooldown(true),
    CanMoveDuringCooldown(false),
    RequiresPermission(false),
    PassiveWhileAfk(false),
    CooldownPassiveAfterAfk(true),
    PassiveByDefault(false),
    PVPCooldown(true),
    RecievePassiveJoinMessage(true),
    PeriodicReminders(true);

    public Object value;
    public Class type;

    Setting(Object value) {
        this.value = value;
        this.type = value.getClass();
    }

    public Object getValue() {
        return this.value;
    }

    public Class getType(){
        return this.getType();
    }

    /**
     * Changes a config setting in plugin memory.
     * Returns TRUE if change was successful.
     * Returns FALSE if change request passes in object of different type than the original value.
     * For example, a setting that's supposed to return a string will return false if the author passes in a integer.
     */
    public <E> boolean setValue(E value) {
        if(value.getClass().equals(type)){
            this.value = value;
            return true;
        }else{
            return false;
        }
    }




}

