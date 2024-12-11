package pl.dreammc.dreammcapi.paper.manager;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.paper.PaperDreamMCAPI;
import pl.dreammc.dreammcapi.paper.scoreboard.ScoreboardModel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreboardManager {

    @Getter @Setter private ScoreboardModel defaultScoreboard;
    private final Map<UUID, ScoreboardModel> playersScoreboard;

    public ScoreboardManager() {
        this.playersScoreboard = new HashMap<>();
    }

    public void setPlayersScoreboard(Player player, ScoreboardModel scoreboardModel) {
        if(this.playersScoreboard.containsKey(player.getUniqueId())) {
            this.playersScoreboard.get(player.getUniqueId()).removeViewer(player);
        }
        this.playersScoreboard.put(player.getUniqueId(), scoreboardModel);
        scoreboardModel.addViewer(player);
    }

    public void removePlayersScoreboard(Player player) {
        if(!this.playersScoreboard.containsKey(player.getUniqueId())) return;
        this.playersScoreboard.get(player.getUniqueId()).removeViewer(player);
        this.playersScoreboard.remove(player.getUniqueId());
    }

    @Nullable
    public ScoreboardModel getPlayersScoreboard(Player player) {
        return this.playersScoreboard.get(player.getUniqueId());
    }

    @Nullable
    public ScoreboardModel getPlayersScoreboard(UUID uuid) {
        return this.playersScoreboard.get(uuid);
    }

    public static ScoreboardManager getInstance() {
        return PaperDreamMCAPI.getInstance().getScoreboardManager();
    }

}
