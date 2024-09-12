package me.robomonkey.versus.settings.command;

import me.robomonkey.versus.command.AbstractCommand;
import me.robomonkey.versus.settings.Setting;
import me.robomonkey.versus.settings.Settings;
import me.robomonkey.versus.util.MessageUtil;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SetCommand extends AbstractCommand {
    public SetCommand() {
        super("set", null);
        setUsage("/versus config set <name> <value>");
        setDescription("Changes a config setting named 'name' to 'value'.");
        setMinArguments(2);
    }

    @Override
    public void callCommand(CommandSender sender, String[] args) {
        String settingName = args[0].toUpperCase();
        if (!Settings.isSetting(settingName)) {
            error(sender, "'" + settingName + "' is not a real config option.");
            return;
        }
        Setting setting = Setting.valueOf(settingName);
        String option = buildArgs(args, 1, args.length);
        Object converted = Settings.tryConvertFromString(option, setting);
        if (converted == null) {
            error(sender, "'" + option + "' is not a proper config value. " + setting + " requires a " + setting.type.toString() + ".");
            return;
        }
        Settings.getInstance().changeSetting(setting, converted);
        sender.sendMessage(MessageUtil.get("&sSuccessfully set &p" + setting + "&s to &b'") + option + MessageUtil.color("' &sin the config file."),
                MessageUtil.get("&sType &p/versus config save&s to save all changes."));
    }

    @Override
    public List<String> callCompletionsUpdate(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return Arrays.stream(Setting.values())
                    .filter(setting -> setting.getType() != Setting.Type.INVALID)
                    .map(setting -> setting.toString().toLowerCase()).collect(Collectors.toList());
        } else {
            String settingName = args[0].toUpperCase();
            if (!Settings.isSetting(settingName)) return List.of();
            List<String> options = Setting.valueOf(settingName).getType().getOptions();
            return options;
        }
    }
}
