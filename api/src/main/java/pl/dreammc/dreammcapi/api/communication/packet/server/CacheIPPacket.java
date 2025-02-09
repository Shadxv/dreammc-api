package pl.dreammc.dreammcapi.api.communication.packet.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import pl.dreammc.dreammcapi.api.communication.packet.Packet;
import pl.dreammc.dreammcapi.api.communication.packet.PacketType;

@PacketType("CACHE_IP")
public class CacheIPPacket extends Packet {

    @Getter private final String addressIP;
    @Getter private final int port;

    public CacheIPPacket(String addressIP, int port) {
        super();
        this.addressIP = addressIP;
        this.port = port;
    }

    @JsonCreator
    public CacheIPPacket(
            @JsonProperty("senderServiceGroup") String senderServiceGroup,
            @JsonProperty("senderServiceName") String senderServiceName,
            @JsonProperty("senderServiceId") String senderServiceId,
            @JsonProperty("addressIP") String addressIP,
            @JsonProperty("port") int port) {
        super(senderServiceGroup, senderServiceName, senderServiceId);
        this.addressIP = addressIP;
        this.port = port;
    }

}
