package org.sproject.sprojectapi.paper.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.sproject.sprojectapi.paper.inventory.InventoryMenu;
import org.sproject.sprojectapi.paper.manager.InventoryManager;

import java.util.Objects;

public class OpenInventoryListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCustomMenuOpen(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();
        if(!InventoryManager.getInstance().hasOpenedInventory(player.getUniqueId())) return;

        InventoryMenu menu = InventoryManager.getInstance().getOpenendInventory(player.getUniqueId());
        if(!Objects.requireNonNull(menu).getInventory().equals(event.getInventory())) return;

        menu.onOpen(event);
    }

}
