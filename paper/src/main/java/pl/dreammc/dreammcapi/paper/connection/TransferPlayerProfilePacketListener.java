package pl.dreammc.dreammcapi.paper.connection;

import pl.dreammc.dreammcapi.api.communication.listener.RedisPacketListener;
import pl.dreammc.dreammcapi.api.communication.packet.proxy.TransferPlayerProfilePacket;
import pl.dreammc.dreammcapi.api.communication.packet.server.TransferPlayerProfileConfirmationPacket;
import pl.dreammc.dreammcapi.api.model.ProfileModel;
import pl.dreammc.dreammcapi.paper.PaperDreamMCAPI;
import pl.dreammc.dreammcapi.paper.manager.PaperProfileManager;

public class TransferPlayerProfilePacketListener extends RedisPacketListener<TransferPlayerProfilePacket> {

    public TransferPlayerProfilePacketListener() {
        super(TransferPlayerProfilePacket.class);
    }

    @Override
    public void handlePacket(TransferPlayerProfilePacket packet) {
        ProfileModel profileModel = packet.getProfileFromJson();
        if(profileModel != null)
            PaperProfileManager.getInstance().loadProfile(packet.getPlayerUUID(), profileModel);

        TransferPlayerProfileConfirmationPacket confirmationPacket = new TransferPlayerProfileConfirmationPacket(packet.getPlayerUUID(), profileModel != null);
        String senderChannel = "dreammc:" + packet.getSenderServiceName() + ":" + packet.getSenderServiceId() + ":PROFILE_TRANSFER_CONFIRMATION";
        if(packet.getRequesterName() != null && packet.getRequesterId() != null) {
            String requesterChannel = "dreammc:" + packet.getRequesterName() + ":" + packet.getRequesterId() + ":PROFILE_TRANSFER_CONFIRMATION:" + packet.getPlayerUUID().toString();
            PaperDreamMCAPI.getInstance().getRedisConnector().publish(requesterChannel, confirmationPacket);
        } else {
            senderChannel += ":" + packet.getPlayerUUID().toString();
        }
        PaperDreamMCAPI.getInstance().getRedisConnector().publish(senderChannel, confirmationPacket);
    }
}
