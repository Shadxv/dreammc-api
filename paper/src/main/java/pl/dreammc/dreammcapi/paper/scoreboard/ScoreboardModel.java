package pl.dreammc.dreammcapi.paper.scoreboard;

import net.kyori.adventure.text.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreboardModel {
    private final UUID scoreboardUUID;
    private Component header;
    private final Map<Integer, UUID> lineUUID;
    private final Map<UUID, ScoreboardLine> lines;
    private final Map<UUID, PlayerScoreboard> playersScoreboard;

    protected ScoreboardModel(Component header) {
        this.scoreboardUUID = UUID.randomUUID();
        this.header = header;
        this.lineUUID = new HashMap<>();
        this.lines = new HashMap<>();
        this.playersScoreboard = new HashMap<>();
    }
}
