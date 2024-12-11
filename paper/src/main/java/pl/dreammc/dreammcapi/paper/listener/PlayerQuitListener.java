package pl.dreammc.dreammcapi.paper.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.dreammc.dreammcapi.paper.manager.NPCManager;
import pl.dreammc.dreammcapi.paper.manager.ScoreboardManager;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        NPCManager.getInstance().despawnAllForPlayer(player);
        NPCManager.getInstance().unregisterAllClientSideNPC(player);

        ScoreboardManager.getInstance().removePlayersScoreboard(player);
    }

}
