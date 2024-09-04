package org.sproject.sprojectapi.paper.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.sproject.sprojectapi.paper.manager.NPCManager;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        NPCManager.getInstance().despawnAllForPlayer(player);
        NPCManager.getInstance().unregisterAllClientSideNPC(player);
    }

}
