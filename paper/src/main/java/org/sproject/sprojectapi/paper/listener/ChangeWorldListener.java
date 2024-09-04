package org.sproject.sprojectapi.paper.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.sproject.sprojectapi.paper.manager.NPCManager;

public class ChangeWorldListener implements Listener {

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();

        NPCManager.getInstance().despawnAllForPlayer(player);
        NPCManager.getInstance().spawnAllForPlayer(player);
    }

}
