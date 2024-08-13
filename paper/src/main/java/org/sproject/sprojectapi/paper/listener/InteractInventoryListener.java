package org.sproject.sprojectapi.paper.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.sproject.sprojectapi.paper.inventory.InventoryMenu;
import org.sproject.sprojectapi.paper.item.BaseItem;
import org.sproject.sprojectapi.paper.item.InventoryItem;
import org.sproject.sprojectapi.paper.item.Item;
import org.sproject.sprojectapi.paper.manager.InventoryManager;

import java.util.Objects;

public class InteractInventoryListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryInteract(InventoryClickEvent event) {
        if(!(event.getWhoClicked() instanceof Player player)) return;

        if(!InventoryManager.getInstance().hasOpenedInventory(player.getUniqueId())) return;

        InventoryMenu menu = InventoryManager.getInstance().getOpenendInventory(player.getUniqueId());
        if(!Objects.requireNonNull(menu).getInventory().equals(event.getInventory())) return;

        event.setCancelled(true);

        BaseItem item = menu.getItem(event.getSlot());
        if(!(item instanceof InventoryItem inventoryItem)) return;

        if(inventoryItem.getClickAction() != null) inventoryItem.getClickAction().accept(event);
    }

}
