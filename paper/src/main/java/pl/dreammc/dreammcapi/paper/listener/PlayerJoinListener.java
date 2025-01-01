package pl.dreammc.dreammcapi.paper.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.dreammc.dreammcapi.api.model.ProfileModel;
import pl.dreammc.dreammcapi.api.util.MessageSender;
import pl.dreammc.dreammcapi.paper.manager.NPCManager;
import pl.dreammc.dreammcapi.paper.manager.PacketHandlerManager;
import pl.dreammc.dreammcapi.paper.manager.PaperProfileManager;
import pl.dreammc.dreammcapi.paper.manager.ScoreboardManager;
import pl.dreammc.dreammcapi.paper.scoreboard.ScoreboardModel;

public class PlayerJoinListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PacketHandlerManager.getInstance().getPacketHandler().injectPacketHandler(player);

        NPCManager.getInstance().spawnAllForPlayer(player);
        ScoreboardModel defaultScoreboard = ScoreboardManager.getInstance().getDefaultScoreboard();
        if(defaultScoreboard != null) ScoreboardManager.getInstance().setPlayersScoreboard(player, defaultScoreboard);

        ProfileModel profileModel = PaperProfileManager.getInstance().getProfile(player.getUniqueId());
        if(profileModel != null)
            MessageSender.sendInfoMessage(player, "Player rank: " + profileModel.getCurrentRank());
    }

}
