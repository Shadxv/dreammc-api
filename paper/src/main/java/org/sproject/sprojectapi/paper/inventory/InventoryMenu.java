package org.sproject.sprojectapi.paper.inventory;

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
import org.sproject.sprojectapi.paper.item.Item;
import org.sproject.sprojectapi.paper.manager.InventoryManager;

import java.util.HashMap;
import java.util.Map;

public abstract class InventoryMenu implements InventoryHolder {

    private Inventory inventory;
    private Player player;
    private Component title;
    private int rows;
    private Map<Integer, Item> items;

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
    }

    protected abstract boolean onClose0(InventoryCloseEvent event);

    public void onClose(InventoryCloseEvent event) {
        if(!onClose0(event)) {
            this.open();
            return;
        }

        InventoryManager.getInstance().closeInventory(this.player.getUniqueId());
    }

    public int getSize() {
        return this.rows * 9;
    }

    @Nullable
    public Item getItem(int index) {
        return this.items.get(index);
    }

    public void addItem(int index, @Nullable Item item) {
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

    public void fillRow(int rowIndex, @Nullable Item item) {
        if(rowIndex < 0 || rowIndex >= this.rows) {
            throw new RuntimeException("Invalid row index");
        }

        for(int i = rowIndex * 9; i < (rowIndex + 1) * 9; i++) {
            this.addItem(i, item);
        }
    }

    public void fillColumn(int columnIndex, @Nullable Item item) {
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
