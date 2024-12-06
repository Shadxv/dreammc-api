package pl.dreammc.dreammcapi.paper.inventory;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.joml.Vector2i;
import pl.dreammc.dreammcapi.paper.inventory.action.ChangeAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class UnlockedMenu extends InventoryMenu{

    @Getter private final Map<Integer, ItemStack> handledItems;
    private final Vector2i unlockedPosStart;
    private final Vector2i unlockedPosEnd;
    @Getter private final List<Integer> unlockedSlots;

    protected UnlockedMenu(Player player, Component title, int rows, Vector2i unlockedPosStart, Vector2i unlockedPosEnd) {
        super(player, title, rows);
        this.handledItems = new HashMap<>();
        this.unlockedPosStart = unlockedPosStart;
        this.unlockedPosEnd = unlockedPosEnd;
        this.unlockedSlots = new ArrayList<>();
        for(int y = this.unlockedPosStart.y(); y <= this.unlockedPosEnd.y(); y++) {
            for(int x = this.unlockedPosStart.x(); x <= this.unlockedPosEnd.x(); x++) {
                this.unlockedSlots.add(9 * y + x);
            }
        }
    }

    protected void clearContent() {
        if(this.inventory == null) return;
        for(int index : this.unlockedSlots) {
            this.inventory.setItem(index, null);
        }
    }

    public void renderContent() {
        if(this.inventory == null) return;
        this.clearContent();
        for(Map.Entry<Integer, ItemStack> entry : this.handledItems.entrySet()) {
            this.inventory.setItem(entry.getKey(), entry.getValue());
        }
    }

    public abstract void handleChange(ChangeAction action);

    public void setContent(Map<Integer, ItemStack> content) {
        this.handledItems.clear();
        this.addContnet(content);
    }

    public void addContnet(Map<Integer, ItemStack> content) {
        this.handledItems.putAll(content);
    }

    public void addItemToContnet(int slot, ItemStack itemStack) {
        this.handledItems.put(slot, itemStack);
    }

    public void removeItemFromContent(int slot) {
        this.handledItems.remove(slot);
    }

}
