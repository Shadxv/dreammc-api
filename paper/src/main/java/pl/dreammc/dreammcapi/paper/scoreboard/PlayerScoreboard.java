package pl.dreammc.dreammcapi.paper.scoreboard;

import net.kyori.adventure.text.Component;

import java.util.Collection;
import java.util.List;

public class PlayerScoreboard {

    private final ScoreboardModel parrent;
    public Component header;
    List<ScoreboardLine> scoreboardLines;

    public PlayerScoreboard(final ScoreboardModel parrent, Component header, List<ScoreboardLine> scoreboardLines) {
        this.parrent = parrent;
        this.header = header;
        this.scoreboardLines = scoreboardLines;
    }

}
