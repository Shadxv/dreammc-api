package pl.dreammc.dreammcapi.api.communication.packet.proxy;

import lombok.Getter;
import pl.dreammc.dreammcapi.api.communication.packet.Packet;
import pl.dreammc.dreammcapi.api.communication.packet.PacketType;

import java.util.UUID;

@PacketType("TRANSFER_PLAYER_REQUEST")
public class TransferPlayerRequestPacket extends Packet {

    @Getter private final UUID playerUUID;
    @Getter private final String targetServer;

    public TransferPlayerRequestPacket(UUID playerUUID, String targetServer) {
        this.playerUUID = playerUUID;
        this.targetServer = targetServer;
    }

}
