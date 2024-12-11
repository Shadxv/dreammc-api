package pl.dreammc.dreammcapi.paper.scoreboard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.minecraft.network.chat.numbers.BlankFormat;
import net.minecraft.network.protocol.game.ClientboundResetScorePacket;
import net.minecraft.network.protocol.game.ClientboundSetScorePacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import pl.dreammc.dreammcapi.paper.ulit.NMSUtil;

import java.util.UUID;

@AllArgsConstructor
public class PlayerScoreboardLine {

    private final PlayerScoreboard parrent;
    @Getter private final UUID lineUUID;
    @Getter private int lineNumber;
    private Component lineComponent;

    public PlayerScoreboardLine updateLine() {
        ServerGamePacketListenerImpl connection = NMSUtil.getConnection(this.parrent.getPlayer());

        var modifyScoreLine = new ClientboundSetScorePacket(
                this.lineUUID.toString(),
                "sidebar",
                15 - lineNumber,
                NMSUtil.toNMSComponent(this.lineComponent),
                BlankFormat.INSTANCE
        );

        connection.send(modifyScoreLine);
        return this;
    }

    public PlayerScoreboardLine removeLine() {
        ServerGamePacketListenerImpl connection = NMSUtil.getConnection(this.parrent.getPlayer());

        var removeScoreLine = new ClientboundResetScorePacket(this.lineUUID.toString(), "sidebar");

        connection.send(removeScoreLine);
        return this;
    }

    public PlayerScoreboardLine updateLine(Component component) {
        this.lineComponent = component;
        this.updateLine();
        return this;
    }

    public PlayerScoreboardLine moveLine(int delta) {
        this.lineNumber += delta;
        this.updateLine();
        return this;
    }

}
