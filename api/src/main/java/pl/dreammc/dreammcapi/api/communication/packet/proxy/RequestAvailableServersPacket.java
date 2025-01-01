package pl.dreammc.dreammcapi.api.communication.packet.proxy;

import pl.dreammc.dreammcapi.api.communication.packet.Packet;
import pl.dreammc.dreammcapi.api.communication.packet.PacketType;

@PacketType("REQUEST_AVAILABLE_SERVERS")
public class RequestAvailableServersPacket extends Packet {

    public RequestAvailableServersPacket(String senderServiceGroup, String senderServiceName, String senderServiceId) {
        super(senderServiceGroup, senderServiceName, senderServiceId);
    }

    public RequestAvailableServersPacket() {
        super();
    }

}
