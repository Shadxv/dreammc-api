package pl.dreammc.dreammcapi.api.communication.packet.proxy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import pl.dreammc.dreammcapi.api.communication.packet.Packet;
import pl.dreammc.dreammcapi.api.communication.packet.PacketType;

import java.util.UUID;

@PacketType("TRANSFER_PLAYER_REQUEST")
public class TransferPlayerRequestPacket extends Packet {

    @Getter private final UUID playerUUID;
    @Getter private final String targetServer;

    @JsonCreator
    public TransferPlayerRequestPacket(
            @JsonProperty("senderServiceGroup") String senderServiceGroup,
            @JsonProperty("senderServiceName") String senderServiceName,
            @JsonProperty("senderServiceId") String senderServiceId,
            @JsonProperty("playerUUID") UUID playerUUID,
            @JsonProperty("targetServer") String targetServer
    ) {
        super(senderServiceGroup, senderServiceName, senderServiceId);
        this.playerUUID = playerUUID;
        this.targetServer = targetServer;
    }

    public TransferPlayerRequestPacket(UUID playerUUID, String targetServer) {
        super();
        this.playerUUID = playerUUID;
        this.targetServer = targetServer;
    }

}
