package pl.dreammc.dreammcapi.api.communication.packet.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import pl.dreammc.dreammcapi.api.communication.packet.Packet;
import pl.dreammc.dreammcapi.api.communication.packet.PacketType;

@PacketType("REGISTER_SERVER_REQUEST")
public class RegisterServerRequestPacket extends Packet {

    @Getter private final String proxyServiceGroup;
    @Getter private final String proxyServiceName;

    @JsonCreator
    public RegisterServerRequestPacket(
            @JsonProperty("senderServiceGroup") String senderServiceGroup,
            @JsonProperty("senderServiceName") String senderServiceName,
            @JsonProperty("senderServiceId") String senderServiceId,
            @JsonProperty("proxyServiceGroup") String proxyServiceGroup,
            @JsonProperty("proxyServiceName") String proxyServiceName
    ) {
        super(senderServiceGroup, senderServiceName, senderServiceId);
        this.proxyServiceGroup = proxyServiceGroup;
        this.proxyServiceName = proxyServiceName;
    }
    public RegisterServerRequestPacket(String proxyServiceGroup, String proxyServiceName) {
        super();
        this.proxyServiceGroup = proxyServiceGroup;
        this.proxyServiceName = proxyServiceName;
    }
}
