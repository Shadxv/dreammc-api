package pl.dreammc.dreammcapi.paper.npc;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import pl.dreammc.dreammcapi.paper.manager.NPCManager;

import java.util.ArrayList;
import java.util.List;

public class ServerSideHumanNPC extends HumanNPC<ServerSideHumanNPC>{

    @Getter private final List<Player> viewers;

    public ServerSideHumanNPC(Location location) {
        super(location);
        this.viewers = new ArrayList<>();
    }

    @Override
    protected boolean registerNPC() {
        if(!NPCManager.getInstance().registerNPC(this)) return false;
        if(!NPCManager.getInstance().registerServerSideNPC(this)) return false;
        return true;
    }

    @Override
    protected void unregisterNPC() {
        NPCManager.getInstance().unregisterServerSideNPC(this);
        NPCManager.getInstance().unregisterNPC(this);
    }

    public void addViewer(Player player) {
        if(!this.spawnLocation.getWorld().getName().equals(player.getWorld().getName())) return;
        if(this.viewers.contains(player)) return;
        if(!player.isOnline()) return;
        this.viewers.add(player);
        this.spawn(player);
    }

    public void removeViewer(Player player) {
        if(!this.viewers.contains(player)) return;
        this.viewers.remove(player);
        if(!player.isOnline()) return;
        this.despawn(player);
    }

    private void spawn(Player player) {
        this.sendInfoPacket(player);
        this.sendSpawnPacket(player);
        this.sendMetadataPacket(player);
    }

    private void despawn(Player player) {
        this.sendDespawnPackets(player);
    }

    @Override
    public ServerSideHumanNPC spawn() {
        if(!this.registerNPC()) return this;

        for(Player player : this.viewers) {
            spawn(player);
        }

        this.isSpawned = true;
        return this;
    }

    @Override
    public ServerSideHumanNPC despawn() {
        this.unregisterNPC();

        for (Player player : viewers) {
            this.removeViewer(player);
        }

        this.isSpawned = false;
        return this;
    }
}
