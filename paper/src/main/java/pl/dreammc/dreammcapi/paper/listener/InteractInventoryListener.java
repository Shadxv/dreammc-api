package pl.dreammc.dreammcapi.paper.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.paper.inventory.InventoryMenu;
import pl.dreammc.dreammcapi.paper.inventory.UnlockedMenu;
import pl.dreammc.dreammcapi.paper.inventory.action.ChangeAction;
import pl.dreammc.dreammcapi.paper.inventory.type.ChangeType;
import pl.dreammc.dreammcapi.paper.item.BaseItem;
import pl.dreammc.dreammcapi.paper.item.InventoryItem;
import pl.dreammc.dreammcapi.paper.manager.InventoryManager;

import java.util.*;

public class InteractInventoryListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryInteract(InventoryClickEvent event) {
        if(!(event.getWhoClicked() instanceof Player player)) return;

        if(!InventoryManager.getInstance().hasOpenedInventory(player.getUniqueId())) return;

        InventoryMenu menu = InventoryManager.getInstance().getOpenendInventory(player.getUniqueId());
        if(!Objects.requireNonNull(menu).getInventory().equals(event.getInventory())) return;
        if(event.getClickedInventory() == null) return;
        if(event.getClickedInventory().getType() == InventoryType.PLAYER) {
            if(menu instanceof UnlockedMenu unlockedMenu && event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                Set<Integer> slots = this.getSlotToMoveItems(unlockedMenu, event.getCurrentItem());
                if(slots == null) return;
                int amountLeft = event.getCurrentItem().getAmount();
                Iterator<Integer> iterator = slots.iterator();
                while (iterator.hasNext() && amountLeft > 0) {
                    int slot = iterator.next();
                    @Nullable ItemStack inSlot = unlockedMenu.getHandledItems().get(slot);
                    if(inSlot == null) {
                        unlockedMenu.handleChange(new ChangeAction(slot, event.getCurrentItem().asQuantity(amountLeft), ChangeType.SET_ITEM));
                        break;
                    }
                    int amoutDifference = inSlot.getMaxStackSize() - inSlot.getAmount();
                    amountLeft -= amoutDifference;
                    unlockedMenu.handleChange(new ChangeAction(slot, event.getCurrentItem().asQuantity(event.getCurrentItem().getMaxStackSize()), ChangeType.SET_ITEM));
                }
            } else {
                if(event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                    event.setCancelled(true);
                }
            }
            return;
        }

        if(menu instanceof UnlockedMenu unlockedMenu && unlockedMenu.getUnlockedSlots().contains(event.getSlot())) {
            switch (event.getAction()) {
                case PLACE_ALL -> {
                    @Nullable ItemStack inSlot = event.getCurrentItem();
                    ItemStack cursor = event.getCursor();
                    ItemStack toReturn = cursor.clone();
                    if(inSlot != null) toReturn.add(inSlot.getAmount());
                    unlockedMenu.handleChange(new ChangeAction(event.getSlot(), toReturn, ChangeType.SET_ITEM));
                }
                case PLACE_SOME -> {
                    ItemStack toReturn = event.getCursor().clone();
                    toReturn.setAmount(toReturn.getMaxStackSize());
                    unlockedMenu.handleChange(new ChangeAction(event.getSlot(), toReturn, ChangeType.SET_ITEM));
                }
                case PLACE_ONE -> {
                    @Nullable ItemStack inSlot = event.getCurrentItem();
                    ItemStack cursor = event.getCursor();
                    ItemStack toReturn = cursor.asOne();
                    if(inSlot != null) toReturn.add(inSlot.getAmount());
                    unlockedMenu.handleChange(new ChangeAction(event.getSlot(), toReturn, ChangeType.SET_ITEM));
                }
                case PICKUP_ALL, DROP_ALL_SLOT, MOVE_TO_OTHER_INVENTORY -> {
                    unlockedMenu.handleChange(new ChangeAction(event.getSlot(), event.getCurrentItem(), ChangeType.REMOVE_ITEM));
                }
                case PICKUP_HALF -> {
                    ItemStack inSlot = event.getCurrentItem();
                    if(inSlot.getAmount() == 1) unlockedMenu.handleChange(new ChangeAction(event.getSlot(), inSlot, ChangeType.REMOVE_ITEM));
                    else unlockedMenu.handleChange(new ChangeAction(event.getSlot(), inSlot.asQuantity(inSlot.getAmount()/2), ChangeType.SET_ITEM));
                }
                case PICKUP_ONE, DROP_ONE_SLOT -> {
                    ItemStack inSlot = event.getCurrentItem();
                    if(inSlot.getAmount() == 1) if(inSlot.getAmount() == 1) unlockedMenu.handleChange(new ChangeAction(event.getSlot(), inSlot, ChangeType.REMOVE_ITEM));
                    else unlockedMenu.handleChange(new ChangeAction(event.getSlot(), inSlot.asQuantity(inSlot.getAmount()-1), ChangeType.SET_ITEM));
                }
                case SWAP_WITH_CURSOR -> {
                    unlockedMenu.handleChange(new ChangeAction(event.getSlot(), event.getCursor().clone(), ChangeType.SET_ITEM));
                }
            }
            return;
        }

        event.setCancelled(true);

        BaseItem<?> item = menu.getItem(event.getSlot());
        if(!(item instanceof InventoryItem inventoryItem)) return;

        if(inventoryItem.getClickAction() != null) inventoryItem.getClickAction().accept(event);
    }

    @Nullable
    private Set<Integer> getSlotToMoveItems(UnlockedMenu unlockedMenu, ItemStack itemStack) {
        Integer freeSlot = null;
        Set<Integer> slotsWithFillableItems = new LinkedHashSet<>();
        for(int i : unlockedMenu.getUnlockedSlots()) {
            if(unlockedMenu.getHandledItems().containsKey(i)) {
                ItemStack itemInCurrentSlot = unlockedMenu.getHandledItems().get(i);
                if(itemInCurrentSlot.getType() == itemStack.getType() && itemInCurrentSlot.getAmount() < itemInCurrentSlot.getMaxStackSize()) {
                    slotsWithFillableItems.add(i);
                }
            } else {
                if(freeSlot == null) freeSlot = i;
            }
        }
        if(freeSlot != null) slotsWithFillableItems.add(freeSlot);
        return slotsWithFillableItems.isEmpty() ? null : slotsWithFillableItems;
    }

}
