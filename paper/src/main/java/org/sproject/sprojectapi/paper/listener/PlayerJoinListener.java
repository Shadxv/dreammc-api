package org.sproject.sprojectapi.paper.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.sproject.sprojectapi.paper.npc.ClientSideHumanNPC;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        new ClientSideHumanNPC(
                player,
                "testnpc",
                new Location(Bukkit.getWorld("world"), 0.5, 64, -73.5, 180, 0)
        ).setGameProfile(player.getPlayerProfile())
                .spawn();
    }

}
