package pl.dreammc.dreammcapi.api.communication.packet;

import lombok.Getter;
import pl.dreammc.dreammcapi.shared.Registry;

import java.io.Serializable;

@Getter
public class Packet implements Serializable {

    private final String senderServiceGroup;
    private final String senderServiceName;
    private final String senderServiceId;

    protected Packet(String senderServiceGroup, String senderServiceName, String senderServiceId) {
        this.senderServiceGroup = senderServiceGroup;
        this.senderServiceName = senderServiceName;
        this.senderServiceId = senderServiceId;
    }

    protected Packet() {
        this.senderServiceGroup = Registry.service.getServiceGroup();
        this.senderServiceName = Registry.service.getServiceName();
        this.senderServiceId = Registry.service.getServiceId();
    }

}
