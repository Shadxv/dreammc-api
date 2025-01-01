package pl.dreammc.dreammcapi.paper.connection;

import pl.dreammc.dreammcapi.api.communication.listener.RedisPacketListener;
import pl.dreammc.dreammcapi.api.communication.packet.server.TransferPlayerProfileConfirmationPacket;
import pl.dreammc.dreammcapi.paper.manager.PaperProfileManager;

public class TransferPlayerProfileConfirmationPacketListener extends RedisPacketListener<TransferPlayerProfileConfirmationPacket> {
    public TransferPlayerProfileConfirmationPacketListener() {
        super(TransferPlayerProfileConfirmationPacket.class);
    }

    @Override
    public void handlePacket(TransferPlayerProfileConfirmationPacket packet) {
        if(!packet.isAccepted())
            return;

        PaperProfileManager.getInstance().removeProfile(packet.getPlayerUUID());
    }
}
