package pl.dreammc.dreammcapi.paper.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import pl.dreammc.dreammcapi.api.util.BaseColor;
import pl.dreammc.dreammcapi.api.util.MessageSender;
import pl.dreammc.dreammcapi.api.util.TextUtil;
import pl.dreammc.dreammcapi.paper.command.PaperCommand;

public class PlayerCommandPreprocessListener implements Listener {

    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Command command = Bukkit.getCommandMap().getCommand(event.getMessage().split(" ")[0].substring(1));
        if(command == null) {
            MessageSender.sendErrorMessage(event.getPlayer(), "Taka komenda nie istnieje");
            event.setCancelled(true);
            return;
        }
        if(command instanceof PaperCommand || event.getPlayer().hasPermission("dreammc.commands.all")) return;
        MessageSender.sendErrorMessage(event.getPlayer(), "Taka komenda nie istnieje");
        event.setCancelled(true);
    }

}
