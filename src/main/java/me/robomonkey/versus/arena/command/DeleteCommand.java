package me.robomonkey.versus.arena.command;

import me.robomonkey.versus.Versus;
import me.robomonkey.versus.arena.Arena;
import me.robomonkey.versus.arena.ArenaManager;
import me.robomonkey.versus.command.AbstractCommand;
import me.robomonkey.versus.util.MessageUtil;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.stream.Collectors;

public class DeleteCommand extends AbstractCommand {

    public DeleteCommand(){
        super("delete", "arena.delete");
        setUsage("/arena delete <arenaName>");
        setDescription("Deletes an existing arena.");
        setPlayersOnly(false);
    }

    @Override
    public void callCommand(CommandSender sender, String[] args) {
        String arenaName = args[0];
        Arena deleteCandidate = ArenaManager.getInstance().getArena(arenaName);
        if(deleteCandidate != null) {
            ArenaManager.getInstance().deleteArena(deleteCandidate);
            sender.sendMessage(MessageUtil.get("&boldSuccessfully deleted &h"+deleteCandidate.getName()));
        }
    }

    @Override
    public List<String> callCompletionsUpdate(CommandSender sender, String[] args) {
        if(args.length == 1){
            List<String> arenaNames = ArenaManager.getInstance().getAllArenas()
                    .stream().map(arena -> arena.getName())
                    .collect(Collectors.toList());
            return arenaNames;
        }
        return null;
    }
}
