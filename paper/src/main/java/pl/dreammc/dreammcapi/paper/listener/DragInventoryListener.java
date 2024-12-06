package pl.dreammc.dreammcapi.paper.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import pl.dreammc.dreammcapi.paper.inventory.InventoryMenu;
import pl.dreammc.dreammcapi.paper.inventory.UnlockedMenu;
import pl.dreammc.dreammcapi.paper.manager.InventoryManager;

import java.util.Objects;

public class DragInventoryListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDrag(InventoryDragEvent event) {
        if(!(event.getWhoClicked() instanceof Player player)) return;

        if(!InventoryManager.getInstance().hasOpenedInventory(player.getUniqueId())) return;
        InventoryMenu menu = InventoryManager.getInstance().getOpenendInventory(player.getUniqueId());
        if(!Objects.requireNonNull(menu).getInventory().equals(event.getInventory())) return;
        if(event.getInventory().getType() == InventoryType.PLAYER) {
            return;
        }

        if(!(menu instanceof UnlockedMenu unlockedMenu)) {
            event.setCancelled(true);
            return;
        }

        switch (event.getType()) {
            case SINGLE -> {

            }
            case EVEN -> {

            }
        }
    }

}
