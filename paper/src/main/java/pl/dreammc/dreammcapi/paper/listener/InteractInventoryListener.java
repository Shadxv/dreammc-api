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
import pl.dreammc.dreammcapi.paper.event.CustomInventoryClickEvent;
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
        if(event.getAction() == InventoryAction.COLLECT_TO_CURSOR) {
            event.setCancelled(true);
            return;
        }
        if(event.getClickedInventory().getType() == InventoryType.PLAYER) {
            if(menu instanceof UnlockedMenu unlockedMenu && event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                event.setCancelled(true);
                Set<Integer> slots = this.getSlotToMoveItems(unlockedMenu, event.getCurrentItem());
                if(slots == null) return;
                int amountLeft = event.getCurrentItem().getAmount();
                Iterator<Integer> iterator = slots.iterator();
                while (iterator.hasNext() && amountLeft > 0) {
                    int slot = iterator.next();
                    @Nullable ItemStack inSlot = unlockedMenu.getHandledItems().get(slot);
                    if(inSlot == null) {
                        ItemStack toAdd = event.getCurrentItem().asQuantity(amountLeft);
                        unlockedMenu.handleChange(new ChangeAction(slot, toAdd, ChangeType.SET_ITEM));
                        unlockedMenu.getInventory().setItem(slot, toAdd);
                        unlockedMenu.addItemToContnet(slot, toAdd);
                        amountLeft = 0;
                    } else {
                        int amoutDifference = inSlot.getMaxStackSize() - inSlot.getAmount();
                        if (amoutDifference >= amountLeft) {
                            ItemStack toAdd = event.getCurrentItem().asQuantity(event.getCurrentItem().getAmount() + amountLeft);
                            unlockedMenu.handleChange(new ChangeAction(slot, event.getCurrentItem().asQuantity(event.getCurrentItem().getAmount() + amountLeft), ChangeType.SET_ITEM));
                            unlockedMenu.getInventory().setItem(slot, toAdd);
                            unlockedMenu.addItemToContnet(slot, toAdd);
                        } else {
                            ItemStack toAdd = event.getCurrentItem().asQuantity(event.getCurrentItem().getMaxStackSize());
                            unlockedMenu.handleChange(new ChangeAction(slot, toAdd, ChangeType.SET_ITEM));
                            unlockedMenu.getInventory().setItem(slot, toAdd);
                            unlockedMenu.addItemToContnet(slot, toAdd);
                        }
                        amountLeft -= amoutDifference;
                    }
                }
                if(amountLeft > 0) event.setCurrentItem(event.getCurrentItem().asQuantity(amountLeft));
                else event.setCurrentItem(null);
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
                    unlockedMenu.addItemToContnet(event.getSlot(), toReturn);
                }
                case PLACE_SOME -> {
                    ItemStack toReturn = event.getCursor().clone();
                    toReturn.setAmount(toReturn.getMaxStackSize());
                    unlockedMenu.handleChange(new ChangeAction(event.getSlot(), toReturn, ChangeType.SET_ITEM));
                    unlockedMenu.addItemToContnet(event.getSlot(), toReturn);
                }
                case PLACE_ONE -> {
                    @Nullable ItemStack inSlot = event.getCurrentItem();
                    ItemStack cursor = event.getCursor();
                    ItemStack toReturn = cursor.asOne();
                    if(inSlot != null) toReturn.add(inSlot.getAmount());
                    unlockedMenu.handleChange(new ChangeAction(event.getSlot(), toReturn, ChangeType.SET_ITEM));
                    unlockedMenu.addItemToContnet(event.getSlot(), toReturn);
                }
                case PICKUP_ALL, DROP_ALL_SLOT, MOVE_TO_OTHER_INVENTORY -> {
                    unlockedMenu.handleChange(new ChangeAction(event.getSlot(), null, ChangeType.REMOVE_ITEM));
                    unlockedMenu.removeItemFromContent(event.getSlot());
                }
                case PICKUP_HALF -> {
                    ItemStack inSlot = event.getCurrentItem();
                    if(inSlot.getAmount() == 1) {
                        unlockedMenu.handleChange(new ChangeAction(event.getSlot(), inSlot, ChangeType.REMOVE_ITEM));
                        unlockedMenu.removeItemFromContent(event.getSlot());
                    } else {
                        ItemStack toReturn = inSlot.asQuantity(inSlot.getAmount()/2);
                        unlockedMenu.handleChange(new ChangeAction(event.getSlot(), toReturn, ChangeType.SET_ITEM));
                        unlockedMenu.addItemToContnet(event.getSlot(), toReturn);
                    }
                }
                case PICKUP_ONE, DROP_ONE_SLOT -> {
                    ItemStack inSlot = event.getCurrentItem();
                    if(inSlot.getAmount() == 1) {
                        unlockedMenu.handleChange(new ChangeAction(event.getSlot(), null, ChangeType.REMOVE_ITEM));
                        unlockedMenu.removeItemFromContent(event.getSlot());
                    }
                    else {
                        ItemStack toReturn = inSlot.asQuantity(inSlot.getAmount()-1);
                        unlockedMenu.handleChange(new ChangeAction(event.getSlot(), inSlot.asQuantity(inSlot.getAmount()-1), ChangeType.SET_ITEM));
                        unlockedMenu.addItemToContnet(event.getSlot(), toReturn);
                    }
                }
                case SWAP_WITH_CURSOR -> {
                    ItemStack toReturn = event.getCursor().clone();
                    unlockedMenu.handleChange(new ChangeAction(event.getSlot(), toReturn, ChangeType.SET_ITEM));
                    unlockedMenu.addItemToContnet(event.getSlot(), toReturn);
                }
                case HOTBAR_SWAP -> {
                    int hotbarSlot = event.getHotbarButton();
                    ItemStack hotbarItemStack;
                    if(hotbarSlot == -1)
                        hotbarItemStack = player.getInventory().getItemInOffHand();
                    else
                        hotbarItemStack = player.getOpenInventory().getBottomInventory().getItem(hotbarSlot);

                    if(hotbarItemStack != null && hotbarItemStack.getType() != Material.AIR) {
                        unlockedMenu.handleChange(new ChangeAction(event.getSlot(), hotbarItemStack, ChangeType.SET_ITEM));
                        unlockedMenu.addItemToContnet(event.getSlot(), hotbarItemStack);
                    } else {
                        unlockedMenu.handleChange(new ChangeAction(event.getSlot(), null, ChangeType.REMOVE_ITEM));
                        unlockedMenu.removeItemFromContent(event.getSlot());
                    }
                }
            }
            return;
        }

        event.setCancelled(true);

        BaseItem<?> item = menu.getItem(event.getSlot());
        if(!(item instanceof InventoryItem inventoryItem)) return;
        CustomInventoryClickEvent custonEvent = new CustomInventoryClickEvent(event);
        if(inventoryItem.getClickAction() != null) inventoryItem.getClickAction().accept(custonEvent);
        Optional.ofNullable(custonEvent.getClickSound()).ifPresent(player::playSound);
    }

    @Nullable
    private Set<Integer> getSlotToMoveItems(UnlockedMenu unlockedMenu, ItemStack itemStack) {
        Integer freeSlot = null;
        Set<Integer> slotsWithFillableItems = new LinkedHashSet<>();
        for(int i : unlockedMenu.getUnlockedSlots()) {
            if(unlockedMenu.getHandledItems().containsKey(i)) {
                ItemStack itemInCurrentSlot = unlockedMenu.getHandledItems().get(i);
                if(itemInCurrentSlot.asOne().equals(itemStack.asOne()) && itemInCurrentSlot.getAmount() < itemInCurrentSlot.getMaxStackSize()) {
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
