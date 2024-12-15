package pl.dreammc.dreammcapi.paper.listener;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;
import pl.dreammc.dreammcapi.api.util.MessageSender;
import pl.dreammc.dreammcapi.paper.command.PaperCommand;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PlayerCommandSendListener implements Listener {

    @EventHandler
    public void onCommandSend(PlayerCommandSendEvent event) {
        if(event.getPlayer().hasPermission("dreammc.commands.all")) return;

        List<String> toRemove = new ArrayList<>();
        Player player = event.getPlayer();
        for(String commandName : event.getCommands()) {
            Command command = Bukkit.getCommandMap().getCommand(commandName);
            if(command == null) {
                toRemove.add(commandName);
                continue;
            }
            if(!(command instanceof PaperCommand paperCommand)) {
                toRemove.add(commandName);
                continue;
            }
            if(paperCommand.getPermission() != null && !player.hasPermission(paperCommand.getPermission()) && paperCommand.isHidden()) {
                toRemove.add(commandName);
                continue;
            }
        }
        event.getCommands().removeAll(toRemove);
    }

}
