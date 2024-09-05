package org.sproject.sprojectapi.paper.npc.event;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.sproject.sprojectapi.paper.npc.NPC;
import org.sproject.sprojectapi.paper.type.HandType;
import org.sproject.sprojectapi.paper.type.InteractionType;

public class NPCClickEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;

    @Getter private final Player player;
    @Getter private final NPC<?> npc;
    @Getter private final InteractionType interactionType;
    @Getter private final HandType hand;

    public NPCClickEvent(Player player, NPC npc, InteractionType interactionType, HandType hand) {
        this.player = player;
        this.npc = npc;
        this.interactionType = interactionType;
        this.hand = hand;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
