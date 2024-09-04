package org.sproject.sprojectapi.paper.npc;

import lombok.Getter;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;
import org.sproject.sprojectapi.paper.npc.event.NPCClickEvent;

import java.util.UUID;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public abstract class NPC<T extends NPC<?>> {

    @Getter protected int entityId;
    @Getter protected final UUID entityUUID;
    @Getter protected final String name;
    @Getter protected final Location spawnLocation;
    @Getter protected Location currentLocation;
    @Getter private boolean isClickable;
    @Getter private Consumer<NPCClickEvent> clickAction;

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

    public T click(@Nullable Consumer<NPCClickEvent> clickAction) {
        this.isClickable = clickAction != null;
        this.clickAction = clickAction;
        return (T) this;
    }
}
