package pl.dreammc.dreammcapi.paper.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import pl.dreammc.dreammcapi.paper.npc.NPC;
import pl.dreammc.dreammcapi.paper.npc.event.NPCClickEvent;

public class ClickNPCListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClick(NPCClickEvent event) {
        NPC<?> npc = event.getNpc();
        if(!npc.isClickable()) return;
        if(event.isCancelled()) return;
        npc.getClickAction().accept(event);
    }

}
