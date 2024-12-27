package pl.dreammc.dreammcapi.api.communication.packet;

import java.util.HashMap;
import java.util.Map;

public class PacketHelper {

    private static final Map<Class<?>, String> PACKET_NAME_CACHE = new HashMap<>();

    public static String getPacketType(Class<? extends Packet> packetClass) {
        return PACKET_NAME_CACHE.computeIfAbsent(packetClass, clazz -> {
            PacketType annotation = clazz.getAnnotation(PacketType.class);
            if (annotation != null) {
                return annotation.value();
            }
            throw new IllegalStateException("Packet does not have specified @PacketType");
        });
    }

}
