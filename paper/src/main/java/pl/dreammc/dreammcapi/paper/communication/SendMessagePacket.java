package pl.dreammc.dreammcapi.paper.communication;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import pl.dreammc.dreammcapi.api.communication.packet.Packet;
import pl.dreammc.dreammcapi.api.communication.packet.PacketType;

@PacketType("SEND_MESSAGE")
public class SendMessagePacket extends Packet {

    @Getter private final String playerName;
    @Getter private final String message;

    public SendMessagePacket(String senderServiceGroup, String senderServiceName, String senderServiceId, String playerName, String message) {
        super(senderServiceGroup, senderServiceName, senderServiceId);
        this.playerName = playerName;
        this.message = message;
    }

    public SendMessagePacket(String playerName, String message) {
        super();
        this.playerName = playerName;
        this.message = message;
    }

}
