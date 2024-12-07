package pl.dreammc.dreammcapi.paper.scoreboard;

import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;

import java.util.UUID;

@AllArgsConstructor
public class ScoreboardLine {

    private final UUID lineUUID;
    private final Component lineComponent;


}
