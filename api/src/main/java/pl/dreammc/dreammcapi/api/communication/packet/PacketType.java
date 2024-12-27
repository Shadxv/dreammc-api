package pl.dreammc.dreammcapi.api.communication.packet;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PacketType {
    String value();
}
