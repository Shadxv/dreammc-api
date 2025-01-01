package pl.dreammc.dreammcapi.api.communication.listener;

import io.lettuce.core.pubsub.RedisPubSubListener;
import lombok.Getter;
import pl.dreammc.dreammcapi.api.communication.packet.Packet;
import pl.dreammc.dreammcapi.api.communication.packet.PacketHelper;
import pl.dreammc.dreammcapi.api.logger.Logger;
import pl.dreammc.dreammcapi.shared.Registry;

import java.util.Locale;
import java.util.regex.Pattern;

@SuppressWarnings("unchecked")
public abstract class RedisPacketListener<T extends Packet> implements RedisPubSubListener<String, Packet> {

    @Getter private final Class<T> packetClass;
    @Getter private final Pattern channel;

    public RedisPacketListener(Class<T> packetClass) {
        this.packetClass = packetClass;
        this.channel = Pattern.compile("^" + Registry.service.getServiceGroup() + ":(\\*|" + Registry.service.getServiceName() + "):(\\*|" + Registry.service.getServiceId() + "):" + PacketHelper.getPacketType(packetClass) + "$");
    }

    public abstract void handlePacket(T packet);

    @Override
    public void message(String channel, Packet message) {
        if(!this.channel.matcher(channel).matches() || !packetClass.isAssignableFrom(message.getClass())) return;
        Logger.sendError("Received message from: " + channel);
        this.handlePacket((T) message);
    }

    @Override
    public void message(String pattern, String channel, Packet message) {

    }

    @Override
    public void subscribed(String channel, long count) {

    }

    @Override
    public void psubscribed(String pattern, long count) {

    }

    @Override
    public void unsubscribed(String channel, long count) {

    }

    @Override
    public void punsubscribed(String pattern, long count) {

    }
}
