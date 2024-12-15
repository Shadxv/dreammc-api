package pl.dreammc.dreammcapi.paper.scoreboard;

import lombok.Getter;
import net.kyori.adventure.text.Component;

import java.util.UUID;
import java.util.function.Consumer;

public class StateScoreboardLine extends ScoreboardLine{

    @Getter private final Consumer<PlayerScoreboardLine> stateUpdater;

    public StateScoreboardLine(Component text, Consumer<PlayerScoreboardLine> stateUpdater) {
        super(text);
        this.stateUpdater = stateUpdater;
    }

    public StateScoreboardLine(Component text, Consumer<PlayerScoreboardLine> stateUpdater, final boolean isCentered) {
        super(text, isCentered);
        this.stateUpdater = stateUpdater;
    }

}
