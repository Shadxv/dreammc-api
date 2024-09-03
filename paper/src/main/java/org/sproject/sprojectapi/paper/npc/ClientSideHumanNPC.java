package org.sproject.sprojectapi.paper.npc;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ClientSideHumanNPC extends HumanNPC<ClientSideHumanNPC>{

    @Getter private final Player owner;

    public ClientSideHumanNPC(Player owner, String name, Location location) {
        super(name, location);
        this.owner = owner;
    }

    @Override
    protected void registerNPC() {

    }

    @Override
    public ClientSideHumanNPC spawn() {
        this.sendInfoPacket(this.owner);
        this.sendSpawnPacket(this.owner);
        this.sendMetadataPacket(this.owner);
        return this;
    }

    @Override
    public ClientSideHumanNPC despawn() {

        return this;
    }
}
