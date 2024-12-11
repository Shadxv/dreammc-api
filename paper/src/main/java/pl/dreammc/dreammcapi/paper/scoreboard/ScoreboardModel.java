package pl.dreammc.dreammcapi.paper.scoreboard;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ScoreboardModel {
    private final UUID scoreboardUUID;
    private Component header;
    private final List<UUID> lineUUIDs;
    private final Map<UUID, ScoreboardLine> lines;
    @Getter private final Map<UUID, PlayerScoreboard> playersScoreboard;
    @Getter private final Map<UUID, UpdatableScoreboardLine> updatableScoreboardLines;

    protected ScoreboardModel(Component header) {
        this.scoreboardUUID = UUID.randomUUID();
        this.header = header;
        this.lineUUIDs = new LinkedList<>();
        this.lines = new HashMap<>();
        this.playersScoreboard = new HashMap<>();
        this.updatableScoreboardLines = new HashMap();
    }

    private void runAllTasks() {
        List<UUID> toRemove = new LinkedList<>();
        for(UpdatableScoreboardLine updatableScoreboardLine : this.updatableScoreboardLines.values()) {
            if(!this.lineUUIDs.contains(updatableScoreboardLine.lineUUID)) {
                toRemove.add(updatableScoreboardLine.getLineUUID());
                continue;
            }
            updatableScoreboardLine.runTask();
        }
        for(UUID toRemoveUUID : toRemove) {
            this.updatableScoreboardLines.remove(toRemoveUUID);
        }
    }

    public void stopAllTasks() {
        List<UUID> toRemove = new LinkedList<>();
        for(UpdatableScoreboardLine updatableScoreboardLine : this.updatableScoreboardLines.values()) {
            if(!this.lineUUIDs.contains(updatableScoreboardLine.lineUUID)) {
                toRemove.add(updatableScoreboardLine.getLineUUID());
                continue;
            }
            updatableScoreboardLine.stopTask();
        }
        for(UUID toRemoveUUID : toRemove) {
            this.updatableScoreboardLines.remove(toRemoveUUID);
        }
    }

    public ScoreboardModel addViewer(Player player) {
        if(this.playersScoreboard.containsKey(player.getUniqueId())) return this;
        PlayerScoreboard scoreboard = new PlayerScoreboard(player,
                this,
                this.header,
                new HashMap<>(),
                new LinkedList<>()
        );
        for(ScoreboardLine line : this.lines.values()) {
            PlayerScoreboardLine playerLine = new PlayerScoreboardLine(scoreboard, line.getLineUUID(), line.getLineNumber(), line.getText());
            if(line instanceof StateScoreboardLine stateScoreboardLine) {
                stateScoreboardLine.getStateUpdater().accept(playerLine);
            } else if(line instanceof UpdatableScoreboardLine updatableScoreboardLine) {
                updatableScoreboardLine.getPlayerWithLine().add(player.getUniqueId());
                updatableScoreboardLine.runTask();
            }
        }
        scoreboard.showToPlayer();
        if(this.playersScoreboard.isEmpty())
            this.runAllTasks();
        this.playersScoreboard.put(player.getUniqueId(), scoreboard);
        return this;
    }

    public ScoreboardModel removeViewer(Player player) {
        if(!this.playersScoreboard.containsKey(player.getUniqueId())) return this;
        this.playersScoreboard.get(player.getUniqueId()).removeScoreboard();
        this.playersScoreboard.remove(player.getUniqueId());
        if(this.playersScoreboard.isEmpty())
            this.stopAllTasks();
        return this;
    }

    public ScoreboardModel changeHeader(Component header) {
        this.header = header;
        return this;
    }

    public ScoreboardModel changeHeaderAndUpdate(Component header) {
        this.changeHeader(header);
        for(PlayerScoreboard playerScoreboard : this.playersScoreboard.values()) {
            playerScoreboard.updateHeader(header);
        }
        return this;
    }

    public ScoreboardModel addLine(ScoreboardLine line) {
        line.setParrent(this);
        line.setLineNumber(this.lineUUIDs.size());
        this.lineUUIDs.add(line.getLineUUID());
        this.lines.put(line.getLineUUID(), line);
        if(line instanceof UpdatableScoreboardLine updatableScoreboardLine)
            this.updatableScoreboardLines.put(updatableScoreboardLine.getLineUUID(), updatableScoreboardLine);
        return this;
    }

    public ScoreboardModel addLine(int index, ScoreboardLine line) {
        if(index > this.lineUUIDs.size()) return this;
        line.setParrent(this);
        line.setLineNumber(index);
        this.lineUUIDs.add(index, line.getLineUUID());
        for(int i = index + 1; i < this.lineUUIDs.size(); i++) {
            this.lines.get(this.lineUUIDs.get(i)).moveLine(1);
        }
        this.lines.put(line.getLineUUID(), line);
        if(line instanceof UpdatableScoreboardLine updatableScoreboardLine)
            this.updatableScoreboardLines.put(updatableScoreboardLine.getLineUUID(), updatableScoreboardLine);
        return this;
    }

    public ScoreboardModel addAndShowLine(ScoreboardLine line) {
        this.addLine(line);
        for(PlayerScoreboard playerScoreboard : this.playersScoreboard.values()) {
            playerScoreboard.addLine(line.getLineNumber(), new PlayerScoreboardLine(playerScoreboard, line.getLineUUID(), line.getLineNumber(), line.getText()));
        }
        if(line instanceof UpdatableScoreboardLine updatableScoreboardLine && !this.playersScoreboard.isEmpty()) {
            updatableScoreboardLine.runTask();
        }
        return this;
    }

    public ScoreboardModel addAndShowLine(int index, ScoreboardLine line) {
        this.addLine(index, line);
        for(PlayerScoreboard playerScoreboard : this.playersScoreboard.values()) {
            playerScoreboard.addLine(index, new PlayerScoreboardLine(playerScoreboard, line.getLineUUID(), line.getLineNumber(), line.getText()));
        }
        return this;
    }

    public ScoreboardModel removeLine(UUID uuid) {
        if(!this.lineUUIDs.contains(uuid)) return this;
        ScoreboardLine line = this.lines.get(uuid);
        if(line == null) return this;
        for(int i = line.lineNumber + 1; i < this.lineUUIDs.size(); i++) {
            this.lines.get(this.lineUUIDs.get(i)).moveLine(-1);
        }
        this.lineUUIDs.remove(line.getLineUUID());
        this.lines.remove(line.getLineUUID());
        return this;
    }

    public ScoreboardModel removeLine(int index) {
        if(index >= this.lineUUIDs.size()) return this;
        this.removeLine(this.lineUUIDs.get(index));
        return this;
    }

    public ScoreboardModel removeAndUpdateLine(UUID uuid) {
        this.removeLine(uuid);
        for(PlayerScoreboard playerScoreboard : this.playersScoreboard.values()) {
            playerScoreboard.removeLine(uuid);
        }
        if(lines instanceof UpdatableScoreboardLine updatableScoreboardLine) {
            updatableScoreboardLine.stopTask();
        }
        return this;
    }

    public ScoreboardModel removeAndUpdateLine(int index) {
        this.removeLine(index);
        for(PlayerScoreboard playerScoreboard : this.playersScoreboard.values()) {
            playerScoreboard.removeLine(index);
        }
        if(lines instanceof UpdatableScoreboardLine updatableScoreboardLine) {
            updatableScoreboardLine.stopTask();
        }
        return this;
    }

    @Nullable
    public ScoreboardLine getLine(UUID uuid) {
        return this.lines.get(uuid);
    }

    @Nullable
    public ScoreboardLine getLine(int index) {
        if(this.lineUUIDs.size() <= index || index <= 0) return null;
        return this.lines.get(this.lineUUIDs.get(index));
    }

    public ScoreboardModel modifyText(UUID uuid, Component component) {
        ScoreboardLine line = this.lines.get(uuid);
        if(line == null) return this;
        line.setText(component);
        return this;
    }

    public ScoreboardModel modifyText(int index, Component component) {
        if(this.lineUUIDs.size() <= index || index <= 0) return this;
        this.modifyText(this.lineUUIDs.get(index), component);
        return this;
    }

    public ScoreboardModel modifyTextAndUpdate(UUID uuid, Component component) {
        this.modifyText(uuid, component);
        for(PlayerScoreboard playerScoreboard : this.playersScoreboard.values()) {
            PlayerScoreboardLine line = playerScoreboard.getLine(uuid);
            if(line == null) continue;
            line.updateLine(component);
        }
        return this;
    }

    public ScoreboardModel modifyTextAndUpdate(int index, Component component) {
        this.modifyText(index, component);
        for(PlayerScoreboard playerScoreboard : this.playersScoreboard.values()) {
            PlayerScoreboardLine line = playerScoreboard.getLine(index);
            if(line == null) continue;
            line.updateLine(component);
        }
        return this;
    }

    @Nullable
    public PlayerScoreboard getPlayersScoreboard(Player player) {
        return this.playersScoreboard.get(player.getUniqueId());
    }
}
