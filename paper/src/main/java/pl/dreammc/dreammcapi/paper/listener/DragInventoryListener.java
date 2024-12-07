package pl.dreammc.dreammcapi.paper.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.paper.inventory.InventoryMenu;
import pl.dreammc.dreammcapi.paper.inventory.UnlockedMenu;
import pl.dreammc.dreammcapi.paper.inventory.action.ChangeAction;
import pl.dreammc.dreammcapi.paper.inventory.type.ChangeType;
import pl.dreammc.dreammcapi.paper.manager.InventoryManager;

import java.util.*;

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
            for (int i : event.getRawSlots()) {
                if(i < menu.getSize()) {
                    event.setCancelled(true);
                    return;
                }
            }
        } else {
            switch (event.getType()) {
                case SINGLE -> {
                    Map<Integer, ItemStack> toAdd = new HashMap<>();
                    for (Map.Entry<Integer, ItemStack> pair : event.getNewItems().entrySet()) {
                        if(pair.getKey() >= unlockedMenu.getSize()) continue;
                        if(!unlockedMenu.getUnlockedSlots().contains(pair.getKey())) {
                            event.setCancelled(true);
                            return;
                        }
                        toAdd.put(pair.getKey(), pair.getValue());
                    }
                    for (Map.Entry<Integer, ItemStack> pair : toAdd.entrySet()) {
                        @Nullable ItemStack inSlot = unlockedMenu.getHandledItems().get(pair.getKey());
                        ItemStack current = pair.getValue();
                        ItemStack toReturn = current.asOne();
                        if(inSlot != null) toReturn.add(inSlot.getAmount());
                        unlockedMenu.handleChange(new ChangeAction(pair.getKey(), toReturn, ChangeType.SET_ITEM));
                        unlockedMenu.addItemToContnet(pair.getKey(), toReturn);
                    }
                }
                case EVEN -> {
                    Map<Integer, ItemStack> toAdd = new HashMap<>();
                    for (Map.Entry<Integer, ItemStack> pair : event.getNewItems().entrySet()) {
                        if(pair.getKey() >= unlockedMenu.getSize()) continue;
                        if(!unlockedMenu.getUnlockedSlots().contains(pair.getKey())) {
                            event.setCancelled(true);
                            return;
                        }
                        toAdd.put(pair.getKey(), pair.getValue());
                    }
                    for (Map.Entry<Integer, ItemStack> pair : toAdd.entrySet()) {
                        @Nullable ItemStack inSlot = unlockedMenu.getHandledItems().get(pair.getKey());
                        ItemStack current = pair.getValue();
                        ItemStack toReturn = current.clone();
                        if(inSlot != null) toReturn.add(inSlot.getAmount());
                        unlockedMenu.handleChange(new ChangeAction(pair.getKey(), toReturn, ChangeType.SET_ITEM));
                        unlockedMenu.addItemToContnet(pair.getKey(), toReturn);
                    }
                }
            }
        }


    }

}
