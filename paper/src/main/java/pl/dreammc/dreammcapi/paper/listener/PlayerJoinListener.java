package pl.dreammc.dreammcapi.paper.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.dreammc.dreammcapi.paper.manager.NPCManager;
import pl.dreammc.dreammcapi.paper.manager.ScoreboardManager;
import pl.dreammc.dreammcapi.paper.scoreboard.ScoreboardModel;

public class PlayerJoinListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        NPCManager.getInstance().spawnAllForPlayer(player);
        ScoreboardModel defaultScoreboard = ScoreboardManager.getInstance().getDefaultScoreboard();
        if(defaultScoreboard != null) ScoreboardManager.getInstance().setPlayersScoreboard(player, defaultScoreboard);
    }

}
