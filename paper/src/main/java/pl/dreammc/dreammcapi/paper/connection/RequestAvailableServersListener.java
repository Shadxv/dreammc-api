package pl.dreammc.dreammcapi.paper.connection;

import pl.dreammc.dreammcapi.api.communication.listener.RedisPacketListener;
import pl.dreammc.dreammcapi.api.communication.packet.proxy.RequestAvailableServersPacket;
import pl.dreammc.dreammcapi.api.communication.packet.server.RegisterServerRequestPacket;
import pl.dreammc.dreammcapi.api.logger.Logger;
import pl.dreammc.dreammcapi.paper.PaperDreamMCAPI;

public class RequestAvailableServersListener extends RedisPacketListener<RequestAvailableServersPacket> {

    public RequestAvailableServersListener(Class<RequestAvailableServersPacket> packetClass) {
        super(packetClass);
    }

    @Override
    public void handlePacket(RequestAvailableServersPacket packet) {
        PaperDreamMCAPI.getInstance().sendRegisterServerRequest(packet.getSenderServiceGroup(), packet.getSenderServiceName(), packet.getSenderServiceId());
        Logger.sendInfo("Received request for all available servers");
    }
}
