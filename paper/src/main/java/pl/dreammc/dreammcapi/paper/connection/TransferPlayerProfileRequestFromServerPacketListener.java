package pl.dreammc.dreammcapi.paper.connection;

import pl.dreammc.dreammcapi.api.communication.listener.RedisPacketListener;
import pl.dreammc.dreammcapi.api.communication.packet.proxy.TransferPlayerProfilePacket;
import pl.dreammc.dreammcapi.api.communication.packet.proxy.TransferPlayerProfileRequestFromServerPacket;
import pl.dreammc.dreammcapi.api.communication.packet.server.TransferPlayerProfileConfirmationPacket;
import pl.dreammc.dreammcapi.api.model.ProfileModel;
import pl.dreammc.dreammcapi.paper.PaperDreamMCAPI;
import pl.dreammc.dreammcapi.paper.manager.PaperProfileManager;

public class TransferPlayerProfileRequestFromServerPacketListener extends RedisPacketListener<TransferPlayerProfileRequestFromServerPacket> {

    public TransferPlayerProfileRequestFromServerPacketListener() {
        super(TransferPlayerProfileRequestFromServerPacket.class);
    }

    @Override
    public void handlePacket(TransferPlayerProfileRequestFromServerPacket packet) {
        ProfileModel profile = PaperProfileManager.getInstance().getProfile(packet.getPlayerUUID());
        if(profile == null) {
            String senderChannel = "dreammc:" + packet.getSenderServiceName() + ":" + packet.getSenderServiceId() + ":PROFILE_TRANSFER_CONFIRMATION:" + packet.getPlayerUUID().toString();
            TransferPlayerProfileConfirmationPacket confirmationPacket = new TransferPlayerProfileConfirmationPacket(packet.getPlayerUUID(), false);
            PaperDreamMCAPI.getInstance().getRedisConnector().publish(senderChannel, confirmationPacket);
            return;
        }

        String targetChannel = "dreammc:" + packet.getTargetName() + ":" + packet.getTargetId() + ":PROFILE_TRANSFER_REQUEST:" + packet.getPlayerUUID().toString();
        TransferPlayerProfilePacket transferPacket = new TransferPlayerProfilePacket(packet.getSenderServiceName(), packet.getSenderServiceId(), packet.getPlayerUUID(), profile);
        PaperDreamMCAPI.getInstance().getRedisConnector().publish(targetChannel, transferPacket);
    }
}
