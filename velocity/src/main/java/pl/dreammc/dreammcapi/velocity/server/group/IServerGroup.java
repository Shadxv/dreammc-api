package pl.dreammc.dreammcapi.velocity.server.group;

import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.velocity.server.IServer;

public interface IServerGroup {

    String getGroupName();
    @Nullable
    String getParentName();
    boolean isDefaultServer();
    int getMinimumReplicas();
    int getMaxPlayers();
    void registerNewServer(IServer server);
    void removeServer(String serverId);

}
