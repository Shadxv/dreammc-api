package pl.dreammc.dreammcapi.velocity.server;

import com.velocitypowered.api.proxy.server.RegisteredServer;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.velocity.server.group.ServerGroupNode;

import java.net.InetSocketAddress;

public class ServerModel implements IServer{

    @Getter private final ServerGroupNode groupNode;
    private final String serverId;
    @Getter private final RegisteredServer registeredServer;
    private final long createdTimestamp;
    private final long expireTimestamp;

    public ServerModel(ServerGroupNode groupNode, String serverId, RegisteredServer registeredServer, long createdTimestamp, long expireTimestamp) {
        this.groupNode = groupNode;
        this.serverId = serverId;
        this.registeredServer = registeredServer;
        this.createdTimestamp = createdTimestamp;
        this.expireTimestamp = expireTimestamp;
    }

    @Override @Nullable
    public String getParentGroupName() {
        return this.groupNode.getParentName();
    }

    @Override
    public String getServerGroupName() {
        return this.groupNode.getGroupName();
    }

    @Override
    public String getServerId() {
        return this.serverId;
    }

    @Override
    public InetSocketAddress getAddress() {
        return this.registeredServer.getServerInfo().getAddress();
    }

    @Override
    public int getMaxPlayers() {
        return this.groupNode.getMaxPlayers();
    }

    @Override
    public boolean isDefaultServer() {
        return this.groupNode.isDefaultServer();
    }

    @Override
    public long getCreatedTimestamp() {
        return this.createdTimestamp;
    }

    @Override
    public long getExpireTimestamp() {
        return this.expireTimestamp;
    }

    @Override
    public int getMinReplicas() {
        return this.groupNode.getMinimumReplicas();
    }
}
