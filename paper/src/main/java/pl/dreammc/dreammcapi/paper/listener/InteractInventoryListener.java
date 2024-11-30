package pl.dreammc.dreammcapi.paper.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import pl.dreammc.dreammcapi.paper.inventory.InventoryMenu;
import pl.dreammc.dreammcapi.paper.inventory.UnlockedMenu;
import pl.dreammc.dreammcapi.paper.inventory.action.ChangeAction;
import pl.dreammc.dreammcapi.paper.inventory.type.ChangeType;
import pl.dreammc.dreammcapi.paper.item.BaseItem;
import pl.dreammc.dreammcapi.paper.item.InventoryItem;
import pl.dreammc.dreammcapi.paper.manager.InventoryManager;

import java.util.Objects;

public class InteractInventoryListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryInteract(InventoryClickEvent event) {
        if(!(event.getWhoClicked() instanceof Player player)) return;

        if(!InventoryManager.getInstance().hasOpenedInventory(player.getUniqueId())) return;

        InventoryMenu menu = InventoryManager.getInstance().getOpenendInventory(player.getUniqueId());
        if(!Objects.requireNonNull(menu).getInventory().equals(event.getInventory())) return;
        if(event.getClickedInventory() == null) return;
        if(event.getClickedInventory().getType() == InventoryType.PLAYER) {
            if(menu instanceof UnlockedMenu unlockedMenu) {
                if(event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                    unlockedMenu.addItemToContnet(event.getSlot(), event.getCurrentItem());
                    unlockedMenu.handleChange(new ChangeAction(event.getSlot(), event.getCurrentItem(), ChangeType.ADDED_ITEM));
                }
            }
            return;
        }

        if(menu instanceof UnlockedMenu unlockedMenu && unlockedMenu.getUnlockedSlots().contains(event.getSlot())) {
            switch (event.getAction()) {
                case PLACE_ALL, PLACE_SOME, PLACE_ONE -> {
                    unlockedMenu.addItemToContnet(event.getSlot(), event.getCurrentItem());
                    unlockedMenu.handleChange(new ChangeAction(event.getSlot(), event.getCurrentItem(), ChangeType.ADDED_ITEM));
                }
                case PICKUP_ALL, PICKUP_HALF, PICKUP_SOME, PICKUP_ONE, MOVE_TO_OTHER_INVENTORY -> {
                    unlockedMenu.removeItemFromContent(event.getSlot());
                    unlockedMenu.handleChange(new ChangeAction(event.getSlot(), event.getCurrentItem(), ChangeType.REMOVED_ITEM));
                }
                case SWAP_WITH_CURSOR -> {
                    unlockedMenu.addItemToContnet(event.getSlot(), event.getCurrentItem());
                    unlockedMenu.handleChange(new ChangeAction(event.getSlot(), event.getCurrentItem(), ChangeType.SWAP_ITEM));
                }
            }
            return;
        }

        event.setCancelled(true);

        BaseItem<?> item = menu.getItem(event.getSlot());
        if(!(item instanceof InventoryItem inventoryItem)) return;

        if(inventoryItem.getClickAction() != null) inventoryItem.getClickAction().accept(event);
    }

}
