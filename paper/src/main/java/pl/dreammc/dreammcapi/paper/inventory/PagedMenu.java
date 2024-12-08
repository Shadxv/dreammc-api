package pl.dreammc.dreammcapi.paper.inventory;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;
import pl.dreammc.dreammcapi.api.logger.Logger;
import pl.dreammc.dreammcapi.paper.item.BaseItem;
import pl.dreammc.dreammcapi.paper.item.InventoryItem;

import java.util.*;

public abstract class PagedMenu extends InventoryMenu{

    private final Vector2i pageSlotStart;
    private final Vector2i pageSlotEnd;
    private final int previousPageItemSlot;
    private final int nextPageItemSlot;
    private final List<BaseItem<?>> items;
    @Getter private final List<BaseItem<?>> currentlyShown;
    private final List<Integer> pageSlots;
    @Getter private int currentPage;


    protected PagedMenu(Player player, Component title, int rows, Vector2i pageSlotStart, Vector2i pageSlotEnd, int previousPageItemSlot, int nextPageItemSlot) {
        super(player, title, rows);
        this.pageSlotStart = pageSlotStart;
        this.pageSlotEnd = pageSlotEnd;
        this.previousPageItemSlot = previousPageItemSlot;
        this.nextPageItemSlot = nextPageItemSlot;
        this.items = new LinkedList<>();
        this.currentlyShown = new LinkedList<>();
        this.currentPage = 1;
        this.pageSlots = new ArrayList<>();
        for(int y = this.pageSlotStart.y(); y <= this.pageSlotEnd.y(); y++) {
            for(int x = this.pageSlotStart.x(); x <= this.pageSlotEnd.x(); x++) {
                this.pageSlots.add(9 * y + x);
            }
        }
    }

    public void generatePageContent() {
        this.currentlyShown.clear();
        this.clearPageContent();
        int maxPage = this.items.size() / this.pageSlots.size() + ((this.items.size() % this.pageSlots.size() == 0) ? 0 : 1);
        if(maxPage < currentPage) this.currentPage = maxPage;
        int itemStartIndex = this.pageSlots.size() * (this.currentPage - 1);
        int itemEndIndex = Math.min(itemStartIndex + this.pageSlots.size(), this.items.size());

        for(int i = 0; i < this.pageSlots.size(); i++) {
            int itemIndex = itemStartIndex + i;
            if(itemIndex >= itemEndIndex) break;
            int slot = this.pageSlots.get(i);
            BaseItem<?> item = this.items.get(itemIndex);
            this.addItem(slot, item);
            this.currentlyShown.add(item);
        }

        if(maxPage == 1) {
            Optional.ofNullable(this.getArrowFillerIfNotExists()).ifPresent(item -> this.addItem(this.previousPageItemSlot, item));
            Optional.ofNullable(this.getArrowFillerIfNotExists()).ifPresent(item -> this.addItem(this.nextPageItemSlot, item));
        }
        if(this.currentPage > 1) {
            this.addItem(this.previousPageItemSlot, this.getPreviousPageItem(this.currentPage, maxPage));
            if (this.currentPage >= maxPage)
                Optional.ofNullable(this.getArrowFillerIfNotExists()).ifPresent(item -> this.addItem(this.nextPageItemSlot, item));
        }
        if(this.currentPage < maxPage) {
            this.addItem(this.nextPageItemSlot, this.getNextPageItem(this.currentPage, maxPage));
            if (this.currentPage <= 1)
                Optional.ofNullable(this.getArrowFillerIfNotExists()).ifPresent(item -> this.addItem(this.previousPageItemSlot, item));
        }
    }

    public void clearPageContent() {
        for(int slot : this.pageSlots) {
            this.addItem(slot, null);
        }
        this.addItem(this.previousPageItemSlot, null);
        this.addItem(this.nextPageItemSlot, null);
    }

    public void handlePageChange(int delta) {
        int maxPage = this.items.size() / this.pageSlots.size() + ((this.items.size() % this.pageSlots.size() == 0) ? 0 : 1);
        if(this.currentPage + delta > maxPage || this.currentPage + delta <= 0) return;
        this.currentPage += delta;
        this.generatePageContent();
    }

    protected abstract InventoryItem getPreviousPageItem(int currentPage, int maxPage);
    protected abstract InventoryItem getNextPageItem(int currentPage, int maxPage);
    protected abstract @Nullable InventoryItem getArrowFillerIfNotExists();

    public PagedMenu addItemToCollection(BaseItem<?> item) {
        this.items.add(item);
        return this;
    }

    public PagedMenu addItemToCollection(int index, BaseItem<?> item) {
        this.items.add(index, item);
        return this;
    }

    public PagedMenu addItems(Collection<BaseItem<?>> itemCollection) {
        this.items.addAll(itemCollection);
        return this;
    }

    public PagedMenu removeItem(int index) {
        this.items.remove(index);
        return this;
    }

    public PagedMenu removeItem(BaseItem<?> item) {
        this.items.remove(item);
        return this;
    }

    public PagedMenu clearItems() {
        this.items.clear();
        return this;
    }

    public PagedMenu removeItemCollection(Collection<BaseItem<?>> itemCollection) {
        this.items.removeAll(itemCollection);
        return this;
    }
}
