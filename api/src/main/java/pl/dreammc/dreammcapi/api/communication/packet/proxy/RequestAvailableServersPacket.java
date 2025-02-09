package pl.dreammc.dreammcapi.api.communication.packet.proxy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import pl.dreammc.dreammcapi.api.communication.packet.Packet;
import pl.dreammc.dreammcapi.api.communication.packet.PacketType;

@PacketType("REQUEST_AVAILABLE_SERVERS")
public class RequestAvailableServersPacket extends Packet {

    @JsonCreator
    public RequestAvailableServersPacket(
            @JsonProperty("senderServiceGroup") String senderServiceGroup,
            @JsonProperty("senderServiceName") String senderServiceName,
            @JsonProperty("senderServiceId") String senderServiceId
    ) {
        super(senderServiceGroup, senderServiceName, senderServiceId);
    }

    public RequestAvailableServersPacket() {
        super();
    }

}
