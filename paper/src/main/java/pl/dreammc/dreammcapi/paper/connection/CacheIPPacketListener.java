package pl.dreammc.dreammcapi.paper.connection;

import pl.dreammc.dreammcapi.api.communication.listener.RedisPacketListener;
import pl.dreammc.dreammcapi.api.communication.packet.server.CacheIPPacket;
import pl.dreammc.dreammcapi.paper.service.PaperService;
import pl.dreammc.dreammcapi.shared.Registry;

public class CacheIPPacketListener extends RedisPacketListener<CacheIPPacket> {

    private final PaperService service;

    public CacheIPPacketListener() {
        super(CacheIPPacket.class);
        this.service = (PaperService) Registry.service;
    }

    @Override
    public void handlePacket(CacheIPPacket packet) {
        this.service.setAddressIP(packet.getAddressIP());
        this.service.setPort(packet.getPort());
    }
}
