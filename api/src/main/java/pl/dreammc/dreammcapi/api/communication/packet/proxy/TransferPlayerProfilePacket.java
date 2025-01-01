package pl.dreammc.dreammcapi.api.communication.packet.proxy;

import lombok.Getter;
import org.bson.Document;
import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.api.communication.packet.Packet;
import pl.dreammc.dreammcapi.api.communication.packet.PacketType;
import pl.dreammc.dreammcapi.api.model.ProfileModel;

import java.util.UUID;

@PacketType("PROFILE_TRANSFER_REQUEST")
public class TransferPlayerProfilePacket extends Packet {

    @Getter @Nullable private final String requesterName;
    @Getter @Nullable private final String requesterId;
    @Getter private final UUID playerUUID;
    @Getter private final String playerProfileJson;

    public TransferPlayerProfilePacket(@Nullable String requesterName, @Nullable String requesterId, UUID playerUUID, ProfileModel profileModel) {
        this.requesterName = requesterName;
        this.requesterId = requesterId;
        this.playerUUID = playerUUID;
        this.playerProfileJson = profileModel.toMongoDocument().toJson();
    }

    @Nullable
    public ProfileModel getProfileFromJson() {
        try {
            return ProfileModel.fromMongoDocument(Document.parse(this.playerProfileJson));
        } catch (Exception e) {
            return null;
        }
    }

}
