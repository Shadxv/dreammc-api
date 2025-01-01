package pl.dreammc.dreammcapi.velocity.server.group;

import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.velocity.server.IServer;

import java.util.HashMap;
import java.util.Map;

public class UnsetServerGroup implements IServerGroup {

    private final String parentGroup;
    private final String groupName;
    private final boolean isDefaultGroup;
    private final int minimumReplicas;
    private final int maxPlayers;
    private final Map<String, IServer> availableServers;

    public UnsetServerGroup(String parentGroup, String groupName, boolean isDefaultGroup, int minimumReplicas, int maxPlayers) {
        this.parentGroup = parentGroup;
        this.groupName = groupName;
        this.isDefaultGroup = isDefaultGroup;
        this.minimumReplicas = minimumReplicas;
        this.maxPlayers = maxPlayers;
        this.availableServers = new HashMap<>();
    }

    @Override
    public void registerNewServer(IServer server) {
        this.availableServers.put(server.getServerId(), server);
    }

    @Override
    public void removeServer(String id) {
        this.availableServers.remove(id);
    }

    public ImmutableMap<String, IServer> getAvailableServers() {
        return ImmutableMap.copyOf(this.availableServers);
    }

    @Override
    public String getGroupName() {
        return this.groupName;
    }

    @Override
    public @Nullable String getParentName() {
        return this.parentGroup;
    }

    @Override
    public boolean isDefaultServer() {
        return this.isDefaultGroup;
    }

    @Override
    public int getMinimumReplicas() {
        return this.minimumReplicas;
    }

    @Override
    public int getMaxPlayers() {
        return this.maxPlayers;
    }
}
