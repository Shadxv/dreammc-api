package pl.dreammc.dreammcapi.paper.inventory;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.paper.PaperDreamMCAPI;
import pl.dreammc.dreammcapi.paper.item.BaseItem;
import pl.dreammc.dreammcapi.paper.manager.InventoryManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public abstract class InventoryMenu implements InventoryHolder {

    protected Inventory inventory;
    @Getter private final Player player;
    private final Component title;
    private final int rows;
    private final Map<Integer, BaseItem<?>> items;
    @Nullable @Getter @Setter Sound openSound;
    @Nullable @Getter @Setter Sound closeSound;
    @Getter @Setter private boolean reopening = false;

    protected InventoryMenu(Player player, Component title, int rows) {
        this.player = player;
        this.title = title;
        this.rows = rows;
        this.items = new HashMap<>();
    }

    public abstract void onOpen(InventoryOpenEvent event);

    public void open() {
        if(InventoryManager.getInstance().hasOpenedInventory(this.player.getUniqueId())) {
            InventoryMenu menu = InventoryManager.getInstance().getOpenendInventory(this.player.getUniqueId());
            if(menu != this) {
                Bukkit.getPluginManager().callEvent(new InventoryCloseEvent(this.player.getOpenInventory(), InventoryCloseEvent.Reason.OPEN_NEW));
            }
        }
        InventoryManager.getInstance().updateInventory(this.player.getUniqueId(), this);
        this.inventory = Bukkit.createInventory(this, this.rows*9, this.title);
        this.player.openInventory(this.inventory);
        Optional.ofNullable(this.openSound).ifPresent(player::playSound);
    }

    protected abstract boolean onClose0(InventoryCloseEvent event);

    public void onClose(InventoryCloseEvent event) {
        if(!onClose0(event) && event.getReason() != InventoryCloseEvent.Reason.PLUGIN) {
            this.reopening = true;
            Bukkit.getAsyncScheduler().runDelayed(PaperDreamMCAPI.getInstance(), scheduledTask -> {
                Bukkit.getScheduler().runTask(PaperDreamMCAPI.getInstance(), bukkitTask -> {
                    event.getPlayer().openInventory(this.inventory);
                });
            }, 50, TimeUnit.MILLISECONDS);
            return;
        }

        InventoryManager.getInstance().closeInventory(this.player.getUniqueId());
        if(event.getReason() != InventoryCloseEvent.Reason.DEATH
                && event.getReason() != InventoryCloseEvent.Reason.DISCONNECT
                && event.getReason() != InventoryCloseEvent.Reason.OPEN_NEW
                && event.getReason() != InventoryCloseEvent.Reason.TELEPORT)
            Optional.ofNullable(this.closeSound).ifPresent(player::playSound);
    }

    public int getSize() {
        return this.rows * 9;
    }

    @Nullable
    public BaseItem<?> getItem(int index) {
        return this.items.get(index);
    }

    public void addItem(int index, @Nullable BaseItem<?> item) {
        if(index < 0 || index >= this.getSize()) {
            throw new RuntimeException("Invalid slot index");
        }

        if(item == null) {
            this.items.remove(index);
            this.inventory.setItem(index, new ItemStack(Material.AIR));
        } else {
            this.items.put(index, item);
            this.inventory.setItem(index, item.build());
        }
    }

    public void fillRow(int rowIndex, @Nullable BaseItem<?> item) {
        if(rowIndex < 0 || rowIndex >= this.rows) {
            throw new RuntimeException("Invalid row index");
        }

        for(int i = rowIndex * 9; i < (rowIndex + 1) * 9; i++) {
            this.addItem(i, item);
        }
    }

    public void fillColumn(int columnIndex, @Nullable BaseItem<?> item) {
        if(columnIndex < 0 || columnIndex >= 9) {
            throw new RuntimeException("Invalid column index");
        }

        for(int i = 0; i < this.rows; i++) {
            this.addItem((i * 9 + columnIndex), item);
        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }
}
