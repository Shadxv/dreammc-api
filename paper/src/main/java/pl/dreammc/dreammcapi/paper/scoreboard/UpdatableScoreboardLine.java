package pl.dreammc.dreammcapi.paper.scoreboard;

import com.google.common.collect.ImmutableList;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import pl.dreammc.dreammcapi.paper.PaperDreamMCAPI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class UpdatableScoreboardLine extends ScoreboardLine{

    private final Consumer<PlayerScoreboardLine> task;
    private final long delay;
    private final long period;
    private final TimeUnit timeUnit;
    private ScheduledTask runningTask;
    @Getter private final List<UUID> playerWithLine;

    public UpdatableScoreboardLine(Component text, Consumer<PlayerScoreboardLine> task, long delay, long period, TimeUnit timeUnit) {
        super(text);
        this.task = task;
        this.delay = delay;
        this.period = period;
        this.timeUnit = timeUnit;
        this.playerWithLine = new ArrayList<>();
    }

    public UpdatableScoreboardLine(Component text, Consumer<PlayerScoreboardLine> task, long delay, long period, TimeUnit timeUnit, final boolean isCentered) {
        super(text, isCentered);
        this.task = task;
        this.delay = delay;
        this.period = period;
        this.timeUnit = timeUnit;
        this.playerWithLine = new ArrayList<>();
    }

    public void runTask() {
        if(this.runningTask != null) return;
        this.runningTask = Bukkit.getAsyncScheduler().runAtFixedRate(PaperDreamMCAPI.getInstance(), scheduledTask -> {
            for(PlayerScoreboard playerScoreboard : new ArrayList<>(this.parrent.getPlayersScoreboard().values())) {
                if(!this.parrent.getPlayersScoreboard().containsKey(playerScoreboard.getPlayer().getUniqueId())) continue;
                PlayerScoreboardLine line = playerScoreboard.getLine(this.lineUUID);
                if(line == null) continue;
                this.task.accept(line);
            }
        }, this.delay, this.period, this.timeUnit);
    }

    public void stopTask() {
        if(this.runningTask == null) return;
        this.runningTask.cancel();
        this.runningTask = null;
    }
}
