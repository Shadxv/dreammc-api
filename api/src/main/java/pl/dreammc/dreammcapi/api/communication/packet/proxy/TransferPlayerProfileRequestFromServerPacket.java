package pl.dreammc.dreammcapi.api.communication.packet.proxy;

import lombok.Getter;
import pl.dreammc.dreammcapi.api.communication.packet.Packet;
import pl.dreammc.dreammcapi.api.communication.packet.PacketType;

import java.util.UUID;

@PacketType("PROFILE_TRANSFER_REQUEST_SERVER")
public class TransferPlayerProfileRequestFromServerPacket extends Packet {

    @Getter private final String targetName;
    @Getter private final String targetId;
    @Getter private final UUID playerUUID;

    public TransferPlayerProfileRequestFromServerPacket(String targetName, String targetId, UUID playerUUID) {
        this.targetName = targetName;
        this.targetId = targetId;
        this.playerUUID = playerUUID;
    }
}
