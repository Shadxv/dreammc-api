package pl.dreammc.dreammcapi.velocity.server;

import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;

public interface IServer {

    @Nullable
    String getParentGroupName();
    String getServerGroupName();
    String getServerId();
    InetSocketAddress getAddress();
    int getMaxPlayers();
    boolean isDefaultServer();
    long getCreatedTimestamp();
    long getExpireTimestamp();
    int getMinReplicas();

}
