package pl.dreammc.dreammcapi.api.communication;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.api.communication.listener.RedisPacketListener;
import pl.dreammc.dreammcapi.api.communication.packet.Packet;
import pl.dreammc.dreammcapi.api.communication.packet.PacketCodec;
import pl.dreammc.dreammcapi.api.communication.packet.PacketHelper;
import pl.dreammc.dreammcapi.api.logger.Logger;
import pl.dreammc.dreammcapi.shared.Registry;
import reactor.core.publisher.Mono;

public class RedisConnector {

    private final String connectionUri;
    @Nullable private RedisClient redisClient;
    @Nullable private StatefulRedisPubSubConnection<String, Packet> pubSubConnection;
    @Nullable private RedisReactiveCommands<String, Packet> reactiveConnection;
    @Nullable private PacketCodec codec;

    public RedisConnector() {
        if(System.getenv().containsKey("REDIS_URI")) this.connectionUri = System.getenv("REDIS_URI");
        else this.connectionUri = null;
    }

    public RedisConnector(String connectionUri) {
        this.connectionUri = connectionUri;
    }

    public boolean init() {
        if(this.connectionUri == null) {
            Logger.sendError("Redis connection URI has not been set");
            return false;
        }

        if(this.redisClient != null) {
            Logger.sendError("Connection with Redis has already been opened");
            return false;
        }

        this.redisClient = RedisClient.create(this.connectionUri);
        this.codec = new PacketCodec();
        this.pubSubConnection = this.redisClient.connectPubSub(this.codec);
        this.reactiveConnection = this.redisClient.connect(this.codec).reactive();
        Logger.sendInfo("Successfully connected to Redis");
        return true;
    }

    public void close() {
        if(this.pubSubConnection != null)
            this.pubSubConnection.close();
        if(this.redisClient != null)
            this.redisClient.close();
    }

    public void publish(String channel, Packet packet) {
        if(this.redisClient == null || this.pubSubConnection == null) {
            Logger.sendError("Redis packet could not have been sent because RedisClient had not been initialized");
            return;
        }
        this.pubSubConnection.async().publish(channel, packet);
        Logger.sendWarning("Sent packet: " + channel);
    }

    public void subscribe(RedisPacketListener<?> listener) {
        if(this.redisClient == null || this.pubSubConnection == null) {
            Logger.sendError("Listeners could not have been registered because RedisClient had not been initialized");
            return;
        }
        this.pubSubConnection.sync().subscribe(
                Registry.service.getServiceGroup() + ":" + Registry.service.getServiceName() + ":" + Registry.service.getServiceId() + ":" + PacketHelper.getPacketType(listener.getPacketClass()),
                Registry.service.getServiceGroup() + ":" + Registry.service.getServiceName() + ":*:" + PacketHelper.getPacketType(listener.getPacketClass()),
                Registry.service.getServiceGroup() + ":*:*:" + PacketHelper.getPacketType(listener.getPacketClass())
        );
        this.pubSubConnection.addListener(listener);
    }

    @Nullable
    public StatefulRedisPubSubConnection<String, Packet> createPrivateConnection() {
        if(this.redisClient == null) {
            Logger.sendError("Private Pub/Sub connection could not have been created because RedisClient had not been initialized");
            return null;
        }
        return this.redisClient.connectPubSub(this.codec);
    }

    @Nullable
    public Mono<Void> sendReactiveCommand(String key, Packet value, long expireTime) {
        if(this.reactiveConnection == null) {
            Logger.sendError("Could not send command because RedisReactiveCommands had not been initialized");
            return null;
        }
        if(expireTime > 0) {
            return this.reactiveConnection.setex(key, expireTime, value).then();
        } else {
            return this.reactiveConnection.set(key, value).then();
        }
    }

    @Nullable
    public Mono<Packet> getReactiveCommand(String key) {
        if(this.reactiveConnection == null) {
            Logger.sendError("Could not get command because RedisReactiveCommands had not been initialized");
            return null;
        }
        return this.reactiveConnection.get(key);
    }

    public void deleteReactiveCommand(String key) {
        if(this.reactiveConnection == null) {
            Logger.sendError("Could not remove command because RedisReactiveCommands had not been initialized");
            return;
        }
        this.reactiveConnection.del(key);
    }
}
