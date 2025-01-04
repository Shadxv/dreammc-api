package pl.dreammc.dreammcapi.paper.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import pl.dreammc.dreammcapi.api.communication.packet.shared.TransferPlayerProfilePacket;
import pl.dreammc.dreammcapi.api.util.BaseColor;
import pl.dreammc.dreammcapi.paper.PaperDreamMCAPI;
import pl.dreammc.dreammcapi.paper.manager.PaperProfileManager;
import pl.dreammc.dreammcapi.shared.Registry;

public class PlayerLoginListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLogin(PlayerLoginEvent event) {
        if(Registry.service == null) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Component.text("Serwer nie akceptuje połączeń.").color(TextColor.fromHexString(BaseColor.redPrimary)));
            return;
        }
        PaperDreamMCAPI.getInstance().getRedisConnector().getReactiveCommand(Registry.service.getServiceGroup() + ":profile:" + event.getPlayer().getUniqueId()).subscribe(packet -> {
            if(packet instanceof TransferPlayerProfilePacket playerProfilePacket) {
                PaperProfileManager.getInstance().loadProfile(playerProfilePacket.getPlayerUUID(), playerProfilePacket.getProfileFromJson());
            }
        });
    }

}
