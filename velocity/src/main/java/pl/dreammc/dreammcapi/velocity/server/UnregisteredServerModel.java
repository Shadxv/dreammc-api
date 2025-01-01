package pl.dreammc.dreammcapi.velocity.server;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;

@Getter
public class UnregisteredServerModel implements IServer{

    @Nullable private final String parentGroupName;
    private final String serverGroupName;
    private final String serverId;
    private final InetSocketAddress address;
    private final int maxPlayers;
    private final boolean isDefaultServer;
    private final long createdTimestamp;
    private final long expireTimestamp;
    private final int minReplicas;

    public UnregisteredServerModel(@Nullable String parentGroupName, String serverGroupName, String serverId, InetSocketAddress address, int maxPlayers, boolean isDefaultServer, long createdTimestamp, long expireTimestamp, int minReplicas) {
        this.parentGroupName = parentGroupName;
        this.serverGroupName = serverGroupName;
        this.serverId = serverId;
        this.address = address;
        this.maxPlayers = maxPlayers;
        this.isDefaultServer = isDefaultServer;
        this.createdTimestamp = createdTimestamp;
        this.expireTimestamp = expireTimestamp;
        this.minReplicas = minReplicas;
    }
}
