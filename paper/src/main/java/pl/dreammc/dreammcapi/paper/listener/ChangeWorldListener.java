package pl.dreammc.dreammcapi.paper.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import pl.dreammc.dreammcapi.paper.manager.NPCManager;

public class ChangeWorldListener implements Listener {

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();

        NPCManager.getInstance().despawnAllForPlayer(player);
        NPCManager.getInstance().spawnAllForPlayer(player);
    }

}
