package pl.dreammc.dreammcapi.paper.scoreboard;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.numbers.BlankFormat;
import net.minecraft.network.protocol.game.ClientboundSetDisplayObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R3.scoreboard.CraftScoreboard;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.paper.PaperDreamMCAPI;
import pl.dreammc.dreammcapi.paper.ulit.NMSUtil;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerScoreboard {

    @Getter private final Player player;
    @Getter private final ScoreboardModel parrent;
    public Component header;
    private final Map<UUID, PlayerScoreboardLine> playerScoreboardLines;
    private final List<UUID> lineUUIDs;
    private boolean isSpawned;
    @Nullable private Objective scoreboardObjective;

    public PlayerScoreboard(final Player player, final ScoreboardModel parrent, Component header, Map<UUID, PlayerScoreboardLine> playerScoreboardLines, List<UUID> lineUUIDs) {
        this.player = player;
        this.parrent = parrent;
        this.header = header;
        this.playerScoreboardLines = playerScoreboardLines;
        this.lineUUIDs = lineUUIDs;
        this.isSpawned = false;
    }

    public void showToPlayer() {
        if(this.isSpawned) return;
        ServerGamePacketListenerImpl connection = NMSUtil.getConnection(this.player);

        this.scoreboardObjective = new Objective(
                ((CraftScoreboard) PaperDreamMCAPI.getInstance().getServerMainScoreboard()).getHandle(),
                "sidebar",
                ObjectiveCriteria.DUMMY,
                NMSUtil.toNMSComponent(this.header),
                ObjectiveCriteria.RenderType.INTEGER,
                false,
                BlankFormat.INSTANCE
        );

        var createObjectivePacket = new ClientboundSetObjectivePacket(scoreboardObjective, 0);
        var displayObjectivePacket = new ClientboundSetDisplayObjectivePacket(DisplaySlot.SIDEBAR, scoreboardObjective);

        connection.send(createObjectivePacket);
        connection.send(displayObjectivePacket);

        this.showLines();
        this.isSpawned = true;
    }

    private void showLines() {
        for(PlayerScoreboardLine line : this.playerScoreboardLines.values()) {
            line.updateLine();
        }
    }

    public void removeScoreboard() {
        if(!this.isSpawned) return;
        this.isSpawned = false;

        if(!Bukkit.getOnlinePlayers().contains(this.player)) return;

        ServerGamePacketListenerImpl connection = NMSUtil.getConnection(this.player);

        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        var removeObjectivePacket = new ClientboundSetObjectivePacket(new FriendlyByteBuf(byteBuf)
                .writeUtf("sidebar")
                .writeByte(1)
        );

        this.scoreboardObjective = null;

        connection.send(removeObjectivePacket);
        for(UUID lineUUID : this.playerScoreboardLines.keySet()) {
            if(!this.parrent.getUpdatableScoreboardLines().containsKey(lineUUID)) continue;
            UpdatableScoreboardLine line = this.parrent.getUpdatableScoreboardLines().get(lineUUID);
            line.getPlayerWithLine().remove(this.player.getUniqueId());
            if(line.getPlayerWithLine().isEmpty()) line.stopTask();
        }
    }


    public void updateHeader(Component header) {
        this.header = header;
        if(!this.isSpawned) return;
        this.scoreboardObjective.setDisplayName(NMSUtil.toNMSComponent(this.header));
        if(!Bukkit.getOnlinePlayers().contains(this.player)) return;
        ServerGamePacketListenerImpl connection = NMSUtil.getConnection(this.player);
        var updateScoreboard = new ClientboundSetObjectivePacket(this.scoreboardObjective, 2);
        connection.send(updateScoreboard);
    }

    public void addLine(int index, PlayerScoreboardLine line) {
        for(int i = index; i < this.lineUUIDs.size(); i++) {
            UUID currentLineUUID = this.lineUUIDs.get(i);
            if(!this.playerScoreboardLines.containsKey(currentLineUUID)) continue;
            this.playerScoreboardLines.get(currentLineUUID)
                    .moveLine(1);
        }
        this.lineUUIDs.add(index, line.getLineUUID());
        this.playerScoreboardLines.put(line.getLineUUID(), line);
        if(this.isSpawned && Bukkit.getOnlinePlayers().contains(this.player))
            line.updateLine();
    }

    public void addLine(int index, Component component) {
        if(this.lineUUIDs.size() <= index) return;
        this.addLine(index, new PlayerScoreboardLine(this, UUID.randomUUID(), index, component));
    }

    public void addLine(Component component) {
        this.addLine(this.lineUUIDs.size(), component);
    }

    public void removeLine(UUID uuid) {
        if(!this.playerScoreboardLines.containsKey(uuid)) return;
        PlayerScoreboardLine line = this.playerScoreboardLines.get(uuid);
        if(this.isSpawned && Bukkit.getOnlinePlayers().contains(this.player))
            line.removeLine();

        for(int i = line.getLineNumber() + 1; i < this.lineUUIDs.size(); i++) {
            UUID currentLineUUID = this.lineUUIDs.get(i);
            if(!this.playerScoreboardLines.containsKey(currentLineUUID)) continue;
            this.playerScoreboardLines.get(currentLineUUID)
                    .moveLine(-1);
        }
        this.playerScoreboardLines.remove(uuid);
        this.lineUUIDs.remove(line.getLineNumber());
    }

    public void removeLine(int index) {
        if(this.lineUUIDs.size() <= index) return;
        this.removeLine(this.lineUUIDs.get(index));
    }

    @Nullable
    public PlayerScoreboardLine getLine(UUID uuid) {
        if(!this.playerScoreboardLines.containsKey(uuid)) return null;
        return this.playerScoreboardLines.get(uuid);
    }

    @Nullable
    public PlayerScoreboardLine getLine(int index) {
        if(this.lineUUIDs.size() <= index) return null;
        return this.getLine(this.lineUUIDs.get(index));
    }
}
