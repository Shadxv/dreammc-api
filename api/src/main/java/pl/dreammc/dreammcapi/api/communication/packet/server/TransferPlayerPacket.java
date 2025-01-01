package pl.dreammc.dreammcapi.api.communication.packet.server;

import lombok.Getter;
import pl.dreammc.dreammcapi.api.communication.packet.Packet;
import pl.dreammc.dreammcapi.api.communication.packet.PacketType;

import java.util.UUID;

@PacketType("PLAYER_TRANSFER")
public class TransferPlayerPacket extends Packet {

    @Getter private final UUID playerUUID;
    @Getter private final String targetServer;

    public TransferPlayerPacket(UUID playerUUID, String targetServer) {
        this.playerUUID = playerUUID;
        this.targetServer = targetServer;
    }

}
