package pl.dreammc.dreammcapi.velocity.manager;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.velocity.VelocityDreamMCAPI;
import pl.dreammc.dreammcapi.velocity.server.IServer;
import pl.dreammc.dreammcapi.velocity.server.group.IServerGroup;
import pl.dreammc.dreammcapi.velocity.server.group.ServerGroupNode;
import pl.dreammc.dreammcapi.velocity.server.group.UnsetServerGroup;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerGroupManager {

    private final Map<String, IServerGroup> knownServerGroups;
    private final Multimap<String, UnsetServerGroup> unsetServerGroups;
    @Nullable private ServerGroupNode rootNode;


    public ServerGroupManager() {
        this.knownServerGroups = new HashMap<>();
        this.unsetServerGroups = HashMultimap.create();
    }

    public void handleNewSerwer(IServer server) {
        this.knownServerGroups.computeIfAbsent(server.getServerGroupName(), groupName -> {
            if(server.getParentGroupName() == null) {
                if(this.rootNode != null)
                    throw new RuntimeException("Root server has already been set. Change 'parentGroupName' in " + server.getServerGroupName() + " config file");

                this.rootNode = new ServerGroupNode(server.getServerGroupName(), server.isDefaultServer(), server.getMinReplicas(), server.getMaxPlayers());
                this.handleNewGroupNodeAdded(this.rootNode);
                return this.rootNode;
            }

            if(this.knownServerGroups.containsKey(server.getParentGroupName())) {
                IServerGroup parentGroup = this.knownServerGroups.get(server.getParentGroupName());
                if(parentGroup instanceof ServerGroupNode parentGroupNode) {
                    ServerGroupNode node = parentGroupNode.addChildNode(
                            new ServerGroupNode(server.getServerGroupName(), server.isDefaultServer(), server.getMinReplicas(), server.getMaxPlayers())
                    );
                    this.handleNewGroupNodeAdded(node);
                    return node;
                }
            }

            UnsetServerGroup serverGroup = new UnsetServerGroup(server.getParentGroupName(), server.getServerGroupName(), server.isDefaultServer(), server.getMinReplicas(), server.getMaxPlayers());
            this.unsetServerGroups.put(server.getParentGroupName(), serverGroup);

            return serverGroup;
        }).registerNewServer(server);
    }

    public void handleNewGroupNodeAdded(ServerGroupNode serverGroupNode) {
        if(!this.unsetServerGroups.containsKey(serverGroupNode.getGroupName())) return;
        Collection<UnsetServerGroup> groupsToRegister = this.unsetServerGroups.get(serverGroupNode.getGroupName());
        if(groupsToRegister.isEmpty()) return;
        for(UnsetServerGroup group : groupsToRegister) {
            ServerGroupNode newNode = serverGroupNode.addChildNode(new ServerGroupNode(group.getGroupName(), group.isDefaultServer(), group.getMinimumReplicas(), group.getMaxPlayers()));
            for(IServer server : group.getAvailableServers().values()) {
                newNode.registerNewServer(server);
            }
            this.unsetServerGroups.removeAll(serverGroupNode.getGroupName());
            this.handleNewGroupNodeAdded(newNode);
        }
    }

    public static ServerGroupManager getInstance() {
        return VelocityDreamMCAPI.getInstance().getServerGroupManager();
    }
}
