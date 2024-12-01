package pl.dreammc.dreammcapi.paper.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import pl.dreammc.dreammcapi.paper.manager.InventoryManager;

public class PlayerMoveListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (InventoryManager.getInstance().hasOpenedInventory(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }


}
