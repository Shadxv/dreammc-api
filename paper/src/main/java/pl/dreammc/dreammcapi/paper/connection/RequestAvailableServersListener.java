package pl.dreammc.dreammcapi.paper.connection;

import pl.dreammc.dreammcapi.api.communication.listener.RedisPacketListener;
import pl.dreammc.dreammcapi.api.communication.packet.proxy.RequestAvailableServersPacket;
import pl.dreammc.dreammcapi.api.communication.packet.server.RegisterServerDataPacket;
import pl.dreammc.dreammcapi.api.communication.packet.server.RegisterServerRequestPacket;
import pl.dreammc.dreammcapi.api.logger.Logger;
import pl.dreammc.dreammcapi.paper.PaperDreamMCAPI;
import pl.dreammc.dreammcapi.paper.service.PaperService;
import pl.dreammc.dreammcapi.shared.Registry;

public class RequestAvailableServersListener extends RedisPacketListener<RequestAvailableServersPacket> {

    private final PaperService service;

    public RequestAvailableServersListener() {
        super(RequestAvailableServersPacket.class);
        this.service = (PaperService) Registry.service;
    }

    @Override
    public void handlePacket(RequestAvailableServersPacket packet) {
        if (!packet.getSenderServiceName().equals(this.service.getProxyServiceName())){
            return;
        }

        if(service.getAddressIP().isEmpty() || service.getPort() == null) {
            PaperDreamMCAPI.getInstance().sendRegisterServerRequest();
        } else {
            String channel = Registry.service.getServiceGroup() + ":" + ((PaperService)Registry.service).getProxyServiceName() + ":" + packet.getSenderServiceId() + ":REGISTER_SERVER_DATA";
            var registerPacket = new RegisterServerDataPacket(this.service.getAddressIP(), this.service.getPort(), this.service.getServiceName(), this.service.getServiceId());

            PaperDreamMCAPI.getInstance().getRedisConnector().publish(channel, registerPacket);
        }

        Logger.sendInfo("Received request for all available servers");
    }
}
