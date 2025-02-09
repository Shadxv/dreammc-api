package pl.dreammc.dreammcapi.api.communication.packet.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import pl.dreammc.dreammcapi.api.communication.packet.Packet;
import pl.dreammc.dreammcapi.api.communication.packet.PacketType;

@PacketType("REGISTER_SERVER_DATA")
public class RegisterServerDataPacket extends Packet {

    @Getter private final String addressIP;
    @Getter private final int port;
    @Getter private final String serviceName;
    @Getter private final String serviceId;

    @JsonCreator
    public RegisterServerDataPacket(
            @JsonProperty("senderServiceGroup") String senderServiceGroup,
            @JsonProperty("senderServiceName") String senderServiceName,
            @JsonProperty("senderServiceId") String senderServiceId,
            @JsonProperty("addressIP") String addressIP,
            @JsonProperty("port") int port,
            @JsonProperty("serviceName") String serviceName,
            @JsonProperty("serviceId") String serviceId
    ) {
        super(senderServiceGroup, senderServiceName, senderServiceId);
        this.addressIP = addressIP;
        this.port = port;
        this.serviceName = serviceName;
        this.serviceId = serviceId;
    }

    public RegisterServerDataPacket(String addressIP, int port, String serviceName, String serviceId) {
        super();
        this.addressIP = addressIP;
        this.port = port;
        this.serviceName = serviceName;
        this.serviceId = serviceId;
    }

}
