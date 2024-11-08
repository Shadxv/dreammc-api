package pl.dreammc.dreammcapi.paper.item;

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

    @Override
    public Item clone() {
        return (Item) super.clone();
    }
}
