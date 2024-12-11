package pl.dreammc.dreammcapi.paper.item;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import pl.dreammc.dreammcapi.paper.event.CustomInventoryClickEvent;

import java.util.function.Consumer;

public class InventoryItem extends BaseItem<InventoryItem> {

    @Getter Consumer<CustomInventoryClickEvent> clickAction;

    public InventoryItem() {
        super();
    }

    public InventoryItem(Material material) {
        super(material);
    }

    public InventoryItem(ItemStack itemStack) {
        super(itemStack);
    }

    public InventoryItem click(Consumer<CustomInventoryClickEvent> action) {
        this.clickAction = action;
        return this;
    }

    @Override
    public InventoryItem clone() {
        InventoryItem clone = (InventoryItem) super.clone();
        clone.clickAction = this.clickAction;
        return clone;
    }
}
