package pl.dreammc.dreammcapi.paper.npc;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.paper.hologram.BaseHologram;
import pl.dreammc.dreammcapi.paper.hologram.ClientSideHologram;
import pl.dreammc.dreammcapi.paper.manager.HologramManager;
import pl.dreammc.dreammcapi.paper.manager.NPCManager;

import java.util.Optional;

public class ClientSideHumanNPC extends HumanNPC<ClientSideHumanNPC>{

    @Getter private final Player owner;
    @Getter @Nullable private ClientSideHologram hologram;

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

    public ClientSideHologram addOrGetHologram() {
        if(this.hologram != null) return this.hologram;
        this.hologram = new ClientSideHologram(this.owner, this.name, this.currentLocation.clone().add(0, 2.2, 0));
        return this.hologram;
    }

    public ClientSideHumanNPC removeHologram() {
        if(this.hologram == null) return this;
        HologramManager.getInstance().unregisterHologram(this.hologram.getId());
        if(this.hologram.isSpawned())
            this.hologram.despawn();
        this.hologram = null;
        return this;
    }

    @Override
    public ClientSideHumanNPC spawn() {
        if(!this.spawnLocation.getWorld().getName().equals(this.owner.getWorld().getName())) return this;
        this.sendInfoPacket(this.owner);
        this.sendSpawnPacket(this.owner);
        this.sendMetadataPacket(this.owner);
        this.sendHideNicknamePackets(this.owner);
        if(this.hologram != null) this.hologram.spawn();
        return this;
    }

    @Override
    public ClientSideHumanNPC despawn() {
        if(!this.isSpawned) return this;
        this.sendDespawnPackets(this.owner);
        Optional.ofNullable(this.hologram).ifPresent(BaseHologram::despawn);
        return this;
    }
}
