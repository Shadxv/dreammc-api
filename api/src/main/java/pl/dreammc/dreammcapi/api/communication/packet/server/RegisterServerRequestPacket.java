package pl.dreammc.dreammcapi.api.communication.packet.server;

import lombok.Getter;
import pl.dreammc.dreammcapi.api.communication.packet.Packet;
import pl.dreammc.dreammcapi.api.communication.packet.PacketType;

@PacketType("REGISTER_SERVER")
public class RegisterServerRequestPacket extends Packet {

    @Getter private final String address;
    @Getter private final int port;

    public RegisterServerRequestPacket(String senderServiceGroup, String senderServiceName, String senderServiceId, String address, int port) {
        super(senderServiceGroup, senderServiceName, senderServiceId);
        this.address = address;
        this.port = port;
    }

    public RegisterServerRequestPacket(String address, int port) {
        super();
        this.address = address;
        this.port = port;
    }
}
