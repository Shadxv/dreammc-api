package org.sproject.sprojectapi.paper.npc;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.sproject.sprojectapi.paper.manager.NPCManager;

public class ClientSideHumanNPC extends HumanNPC<ClientSideHumanNPC>{

    @Getter private final Player owner;

    public ClientSideHumanNPC(Player owner, Location location) {
        super(location);
        this.owner = owner;
        this.registerNPC();
    }

    @Override
    protected boolean registerNPC() {
        if(!NPCManager.getInstance().registerNPC(this)) return false;
        if(!NPCManager.getInstance().registerClientSideNPC(this)) return false;
        return true;
    }

    @Override
    public void unregisterNPC() {
        NPCManager.getInstance().unregisterClientSideNPC(this);
        NPCManager.getInstance().unregisterNPC(this);
    }

    @Override
    public ClientSideHumanNPC spawn() {
        if(!this.spawnLocation.getWorld().getName().equals(this.owner.getWorld().getName())) return this;
        this.sendInfoPacket(this.owner);
        this.sendSpawnPacket(this.owner);
        this.sendMetadataPacket(this.owner);
        return this;
    }

    @Override
    public ClientSideHumanNPC despawn() {
        if(!this.isSpawned) return this;
        this.sendDespawnPackets(this.owner);
        return this;
    }
}
