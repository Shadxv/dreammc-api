package pl.dreammc.dreammcapi.paper.scoreboard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minecraft.network.chat.numbers.BlankFormat;
import net.minecraft.network.protocol.game.ClientboundResetScorePacket;
import net.minecraft.network.protocol.game.ClientboundSetScorePacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import pl.dreammc.dreammcapi.api.util.MessageSender;
import pl.dreammc.dreammcapi.api.util.TextUtil;
import pl.dreammc.dreammcapi.paper.ulit.NMSUtil;

import java.util.Optional;
import java.util.UUID;

public class PlayerScoreboardLine {

    private static final int SPACE_WIDTH = TextUtil.getCharWidth(' ');

    @Getter private final PlayerScoreboard parrent;
    @Getter private final UUID lineUUID;
    @Getter private int lineNumber;
    @Getter private Component lineComponent;
    @Getter private final boolean isCentered;
    @Getter private int lineWidth = 0;

    public PlayerScoreboardLine(PlayerScoreboard parrent, UUID lineUUID, int lineNumber, Component lineComponent, boolean isCentered) {
        this.parrent = parrent;
        this.lineUUID = lineUUID;
        this.lineNumber = lineNumber;
        this.lineComponent = lineComponent;
        this.isCentered = isCentered;
        if(this.isCentered) this.parrent.getCenteredLines().add(this.lineUUID);
        this.lineWidth = TextUtil.getLineWidth(this.lineComponent);
    }

    public PlayerScoreboardLine updateLine() {
        ServerGamePacketListenerImpl connection = NMSUtil.getConnection(this.parrent.getPlayer());

        if(this.isCentered) {
            this.recenterLine();
        } else {
            var modifyScoreLine = new ClientboundSetScorePacket(
                    this.lineUUID.toString(),
                    "sidebar",
                    15 - lineNumber,
                    Optional.of(NMSUtil.toNMSComponent(this.lineComponent)),
                    Optional.of(BlankFormat.INSTANCE)
            );

            connection.send(modifyScoreLine);
        }
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
        this.lineWidth = TextUtil.getLineWidth(this.lineComponent);
        if(this.parrent.getLongestLineWidth() < this.lineWidth) {
            this.parrent.setLongestLine(this.lineUUID);
            this.parrent.setLongestLineWidth(this.lineWidth);
            this.parrent.recentreLines();
        }
        this.updateLine();
        return this;
    }

    public PlayerScoreboardLine moveLine(int delta) {
        this.lineNumber += delta;
        this.updateLine();
        return this;
    }

    public void recenterLine() {
        int difference = this.parrent.getLongestLineWidth() - this.lineWidth;
        float spaces = (float) difference / SPACE_WIDTH;
        int spacesPerSide = Math.round(spaces / 2);

        Component componentToSend = Component.text(" ".repeat(spacesPerSide)).append(this.lineComponent).append(Component.text(" ".repeat(spacesPerSide)));

        ServerGamePacketListenerImpl connection = NMSUtil.getConnection(this.parrent.getPlayer());

        var modifyScoreLine = new ClientboundSetScorePacket(
                this.lineUUID.toString(),
                "sidebar",
                15 - lineNumber,
                Optional.of(NMSUtil.toNMSComponent(componentToSend)),
                Optional.of(BlankFormat.INSTANCE)
        );

        connection.send(modifyScoreLine);
    }

}
