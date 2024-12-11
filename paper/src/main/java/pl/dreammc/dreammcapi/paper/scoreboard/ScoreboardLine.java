package pl.dreammc.dreammcapi.paper.scoreboard;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;

import java.util.UUID;

public class ScoreboardLine {

    @Setter protected ScoreboardModel parrent;
    @Getter protected final UUID lineUUID;
    @Setter @Getter protected int lineNumber;
    @Getter @Setter protected Component text;

    public ScoreboardLine(Component text) {
        this.lineUUID = UUID.randomUUID();
        this.text = text;
    }

    public void moveLine(int delta) {
        this.lineNumber += delta;
    }

}
