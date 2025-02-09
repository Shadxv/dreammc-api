package pl.dreammc.dreammcapi.api.communication.packet.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import pl.dreammc.dreammcapi.api.communication.packet.Packet;
import pl.dreammc.dreammcapi.api.communication.packet.PacketType;

@PacketType("UNREGISTER_SERVER")
public class UnregisterServerRequestPacket extends Packet {

    @Getter private final String address;
    @Getter private final int port;

    @JsonCreator
    public UnregisterServerRequestPacket(
            @JsonProperty("senderServiceGroup") String senderServiceGroup,
            @JsonProperty("senderServiceName") String senderServiceName,
            @JsonProperty("senderServiceId") String senderServiceId,
            @JsonProperty("address") String address,
            @JsonProperty("port") int port
    ) {
        super(senderServiceGroup, senderServiceName, senderServiceId);
        this.address = address;
        this.port = port;
    }

    public UnregisterServerRequestPacket(String address, int port) {
        super();
        this.address = address;
        this.port = port;
    }
}
