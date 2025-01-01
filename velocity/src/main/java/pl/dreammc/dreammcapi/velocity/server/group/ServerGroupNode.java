package pl.dreammc.dreammcapi.velocity.server.group;

import com.velocitypowered.api.proxy.server.ServerInfo;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.velocity.VelocityDreamMCAPI;
import pl.dreammc.dreammcapi.velocity.manager.ServerGroupManager;
import pl.dreammc.dreammcapi.velocity.server.IServer;
import pl.dreammc.dreammcapi.velocity.server.ServerModel;

import java.util.HashMap;
import java.util.Map;

public class ServerGroupNode implements IServerGroup {

    @Setter @Nullable private ServerGroupNode parent;
    private final String groupName;
    private final Map<String, ServerModel> registeredServers;
    private final Map<String, ServerGroupNode> children;
    private final boolean isDefaultServer;
    private final int minimumReplicas;
    private final int maxPlayers;

    public ServerGroupNode(String groupName, boolean isDefaultServer, int minimumReplicas, int maxPlayers) {
        this.groupName = groupName;
        this.registeredServers = new HashMap<>();
        this.children = new HashMap<>();
        this.isDefaultServer = isDefaultServer;
        this.minimumReplicas = minimumReplicas;
        this.maxPlayers = maxPlayers;
    }

    public ServerGroupNode addChildNode(ServerGroupNode node) {
        this.children.put(node.groupName, node);
        if(node.getParentName() == null) node.setParent(this);
        return node;
    }

    public void removeChild(String groupName) {
        ServerGroupNode node = this.children.remove(groupName);
        if(node == null) return;
        node.setParent(null);
    }

    @Override
    public void registerNewServer(IServer server) {
        String name = this.groupName + "-" + server.getServerId();
        ServerModel model = new ServerModel(
                this,
                server.getServerId(),
                VelocityDreamMCAPI.getInstance().getServer().registerServer(new ServerInfo(name, server.getAddress())),
                server.getCreatedTimestamp(),
                server.getExpireTimestamp()
        );
        this.registeredServers.put(server.getServerId(), model);
    }

    @Override
    public void removeServer(String serverId) {
        ServerModel server = this.registeredServers.remove(serverId);
        if(server == null) return;
        VelocityDreamMCAPI.getInstance().getServer().unregisterServer(server.getRegisteredServer().getServerInfo());
    }

    @Override
    public String getGroupName() {
        return this.groupName;
    }

    @Override @Nullable
    public String getParentName() {
        if(this.parent == null) return null;
        return this.parent.groupName;
    }

    @Override
    public boolean isDefaultServer() {
        return this.isDefaultServer;
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
