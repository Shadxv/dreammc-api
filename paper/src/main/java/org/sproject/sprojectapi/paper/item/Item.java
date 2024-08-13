package org.sproject.sprojectapi.paper.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Item extends BaseItem<Item> {
    public Item() {
        super();
    }

    public Item(Material material) {
        super(material);
    }

    public Item(ItemStack itemStack) {
        super(itemStack);
    }
}
