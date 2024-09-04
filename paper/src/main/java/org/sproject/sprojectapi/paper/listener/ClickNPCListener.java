package org.sproject.sprojectapi.paper.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.sproject.sprojectapi.paper.npc.NPC;
import org.sproject.sprojectapi.paper.npc.event.NPCClickEvent;

public class ClickNPCListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClick(NPCClickEvent event) {
        NPC<?> npc = event.getNpc();
        if(!npc.isClickable()) return;
        npc.getClickAction().accept(event);
    }

}
