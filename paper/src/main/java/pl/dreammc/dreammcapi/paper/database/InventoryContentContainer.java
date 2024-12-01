package pl.dreammc.dreammcapi.paper.database;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class InventoryContentContainer {

    @Getter private final Map<Integer, ItemStack> content;

    public InventoryContentContainer(Map<Integer, ItemStack> content){
        this.content = content;
    }

}
