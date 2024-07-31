package org.sproject.sprojectapi.paper.item;

import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.Consumer;

public class InventoryItem extends Item {

    Consumer<InventoryClickEvent> clickAction;

    public InventoryItem click(Consumer<InventoryClickEvent> action) {
        this.clickAction = action;
        return this;
    }

}
