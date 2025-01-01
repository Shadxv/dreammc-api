package pl.dreammc.dreammcapi.api.communication.packet.server;

import lombok.Getter;
import pl.dreammc.dreammcapi.api.communication.packet.Packet;
import pl.dreammc.dreammcapi.api.communication.packet.PacketType;

import java.util.UUID;

@PacketType("PROFILE_TRANSFER_CONFIRMATION")
public class TransferPlayerProfileConfirmationPacket extends Packet {

    @Getter private final UUID playerUUID;
    @Getter private final boolean isAccepted;

    public TransferPlayerProfileConfirmationPacket(UUID playerUUID, boolean isAccepted) {
        this.playerUUID = playerUUID;
        this.isAccepted = isAccepted;
    }

}
