package org.sproject.sprojectapi.paper.inventory;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.sproject.sprojectapi.paper.item.InventoryItem;


import java.util.HashMap;
import java.util.Map;

public abstract class InventoryMenu implements InventoryHolder {

    private Inventory inventory;
    private Player player;
    private Component title;
    private int rows;
    private Map<Integer, InventoryItem> items;

    public InventoryMenu(Player player, Component title, int rows) {
        this.player = player;
        this.title = title;
        this.rows = rows;
        this.items = new HashMap<>();
    }

    public abstract void onOpen(InventoryOpenEvent event);

    public void open() {

    }

    public abstract void onClose(InventoryCloseEvent event);

    public void close() {

    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }
}
