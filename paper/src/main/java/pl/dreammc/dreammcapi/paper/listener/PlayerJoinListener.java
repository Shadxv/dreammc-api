package pl.dreammc.dreammcapi.paper.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.dreammc.dreammcapi.paper.manager.NPCManager;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        NPCManager.getInstance().spawnAllForPlayer(player);
    }

}
