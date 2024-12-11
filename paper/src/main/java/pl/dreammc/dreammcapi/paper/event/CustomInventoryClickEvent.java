package pl.dreammc.dreammcapi.paper.event;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CustomInventoryClickEvent {

    private final InventoryClickEvent event;
    @Nullable @Getter @Setter
    private Sound clickSound;

    public CustomInventoryClickEvent(InventoryClickEvent event) {
        this.event = event;
    }

    @NotNull
    public InventoryAction getAction() {
        return this.event.getAction();
    }

    @NotNull
    public ClickType getClick() {
        return this.event.getClick();
    }

    @Nullable
    public Inventory getClickedInventory() {
        return this.event.getClickedInventory();
    }

    @Nullable
    public ItemStack getCurrentItem() {
        return this.event.getCurrentItem();
    }

    @NotNull
    public ItemStack getCursor() {
        return this.event.getCursor();
    }

    public static @NotNull HandlerList getHandlerList() {
        return InventoryClickEvent.getHandlerList();
    }

    @NotNull
    public HandlerList getHandlers() {
        return this.event.getHandlers();
    }

    public int getHotbarButton() {
        return this.event.getHotbarButton();
    }

    public int getRawSlot() {
        return this.event.getRawSlot();
    }

    public int getSlot() {
        return this.event.getSlot();
    }

    @NotNull
    public InventoryType.SlotType getSlotType() {
        return this.event.getSlotType();
    }

    public boolean isLeftClick() {
        return this.event.isLeftClick();
    }

    public boolean isRightClick() {
        return this.event.isRightClick();
    }

    public boolean isShiftClick() {
        return this.event.isShiftClick();
    }

    public void setCurrentItem(@Nullable ItemStack stack) {
        this.event.setCurrentItem(stack);
    }

    @Deprecated
    public void setCursor(@Nullable ItemStack stack) {
        this.event.setCursor(stack);
    }

    public Event.Result getResult() {
        return this.event.getResult();
    }

    public HumanEntity getWhoClicked() {
        return this.event.getWhoClicked();
    }

    public boolean isCancelled() {
        return this.event.isCancelled();
    }

    public void setCancelled(boolean cancel) {
        this.event.setCancelled(cancel);
    }

    public void setResult(Event.Result result) {
        this.event.setResult(result);
    }

    public Inventory getInventory() {
        return this.event.getInventory();
    }

    public InventoryView getView() {
        return this.event.getView();
    }

    public List<HumanEntity> getViewers() {
        return this.event.getViewers();
    }

    public void callEvent() {
        this.event.callEvent();
    }

    public String getEventName() {
        return this.event.getEventName();
    }

    public boolean isAsynchronous() {
        return this.event.isAsynchronous();
    }


}
