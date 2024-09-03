package org.sproject.sprojectapi.paper.npc;

import lombok.Getter;
import org.bukkit.Location;

import java.util.UUID;

@SuppressWarnings("unchecked")
public abstract class NPC<T extends NPC<?>> {

    @Getter protected final UUID entityUUID;
    @Getter protected final String name;
    @Getter protected final Location spawnLocation;
    @Getter protected Location currentLocation;

    public NPC(String name, Location location) {
        this.entityUUID = UUID.randomUUID();
        this.name = name;
        this.spawnLocation = location;
    }

    protected void changeCurrentLocation(Location location) {
        this.currentLocation = location;
    }

    public abstract T spawn();

    public abstract T despawn();
}
