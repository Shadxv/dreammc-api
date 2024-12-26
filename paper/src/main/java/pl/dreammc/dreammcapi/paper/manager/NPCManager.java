package pl.dreammc.dreammcapi.paper.manager;

import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.paper.PaperDreamMCAPI;
import pl.dreammc.dreammcapi.paper.listener.ClickNPCPacketListener;
import pl.dreammc.dreammcapi.paper.npc.ClientSideHumanNPC;
import pl.dreammc.dreammcapi.paper.npc.NPC;
import pl.dreammc.dreammcapi.paper.npc.ServerSideHumanNPC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NPCManager {

    private final Map<Integer, NPC<?>> registeredNPCs;
    private final List<Integer> serverSideNPCs;
    private final Map<Player, List<Integer>> clientSideNPCs;

    public NPCManager() {
        this.registeredNPCs = new HashMap<>();
        this.serverSideNPCs = new ArrayList<>();
        this.clientSideNPCs = new HashMap<>();
        PacketHandlerManager.getInstance().registerListener(ServerboundInteractPacket.class, new ClickNPCPacketListener());
    }

    public boolean registerNPC(NPC<?> npc) {
        if(this.registeredNPCs.containsKey(npc.getEntityId())) return false;
        this.registeredNPCs.put(npc.getEntityId(), npc);
        return true;
    }

    public boolean registerServerSideNPC(ServerSideHumanNPC npc) {
        if(this.serverSideNPCs.contains(npc.getEntityId())) return false;
        this.serverSideNPCs.add(npc.getEntityId());
        return true;
    }

    public boolean registerClientSideNPC(ClientSideHumanNPC npc) {
        if(!this.clientSideNPCs.containsKey(npc.getOwner())) {
            this.clientSideNPCs.put(npc.getOwner(), new ArrayList<>());
        } else {
            if(this.clientSideNPCs.get(npc.getOwner()).contains(npc.getEntityId())) return false;
        }
        this.clientSideNPCs.get(npc.getOwner()).add(npc.getEntityId());
        return true;
    }

    public void unregisterNPC(NPC<?> npc) {
        this.registeredNPCs.remove(npc.getEntityId());
    }

    public void unregisterServerSideNPC(ServerSideHumanNPC npc) {
        this.serverSideNPCs.remove(npc.getEntityId());
    }

    public void unregisterClientSideNPC(ClientSideHumanNPC npc) {
        if(!this.clientSideNPCs.containsKey(npc.getOwner())) {
            return;
        }
        this.clientSideNPCs.get(npc.getOwner()).remove(Integer.valueOf(npc.getEntityId()));
        if(this.clientSideNPCs.get(npc.getOwner()).isEmpty()) {
            this.clientSideNPCs.remove(npc.getOwner());
        }
    }

    public void unregisterAllClientSideNPC(Player player) {
        if(!this.clientSideNPCs.containsKey(player)) {
            return;
        }
        List<Integer> idsToRemove = List.copyOf(this.clientSideNPCs.get(player));
        for(Integer id : idsToRemove) {
            ClientSideHumanNPC npc = (ClientSideHumanNPC) this.registeredNPCs.get(id);
            npc.unregisterNPC();
        }
        this.clientSideNPCs.remove(player);
    }

    public void spawnAllForPlayer(Player player) {
        for(Integer id : this.serverSideNPCs) {
            ServerSideHumanNPC npc = (ServerSideHumanNPC) this.registeredNPCs.get(id);
            if(!npc.getSpawnLocation().getWorld().getName().equals(player.getWorld().getName())) continue;
            npc.addViewer(player);
        }
        if(!this.clientSideNPCs.containsKey(player)) return;
        for(Integer id : this.clientSideNPCs.get(player)) {
            ClientSideHumanNPC npc = (ClientSideHumanNPC) this.registeredNPCs.get(id);
            if(!npc.getSpawnLocation().getWorld().getName().equals(player.getWorld().getName())) continue;
            npc.spawn();
        }
    }

    public void despawnAllForPlayer(Player player) {
        for(Integer id : this.serverSideNPCs) {
            ServerSideHumanNPC npc = (ServerSideHumanNPC) this.registeredNPCs.get(id);
            if(!npc.getViewers().contains(player)) continue;
            npc.removeViewer(player);
        }
        if(!this.clientSideNPCs.containsKey(player)) return;
        for(Integer id : this.clientSideNPCs.get(player)) {
            ClientSideHumanNPC npc = (ClientSideHumanNPC) this.registeredNPCs.get(id);
            if(!npc.isSpawned()) continue;
            npc.despawn();
        }
    }

    @Nullable
    public NPC<?> getNPC(int id) {
        return this.registeredNPCs.get(id);
    }

    public static NPCManager getInstance() {
        return PaperDreamMCAPI.getInstance().getNpcManager();
    }
}
