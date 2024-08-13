package org.sproject.sprojectapi.paper.item;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class InventoryItem extends BaseItem<InventoryItem> {

    @Getter Consumer<InventoryClickEvent> clickAction;

    public InventoryItem() {
        super();
    }

    public InventoryItem(Material material) {
        super(material);
    }

    public InventoryItem(ItemStack itemStack) {
        super(itemStack);
    }

    public InventoryItem click(Consumer<InventoryClickEvent> action) {
        this.clickAction = action;
        return this;
    }

}
