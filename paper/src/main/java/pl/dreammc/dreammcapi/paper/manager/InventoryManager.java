package pl.dreammc.dreammcapi.paper.manager;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.paper.PaperDreamMCAPI;
import pl.dreammc.dreammcapi.paper.inventory.InventoryMenu;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InventoryManager {

    private final Map<UUID, InventoryMenu> openedMenus;

    public InventoryManager() {
        this.openedMenus = new HashMap<>();
    }

    public void updateInventory(UUID playerUUID, @NotNull InventoryMenu menu) {
        this.openedMenus.put(playerUUID, menu);
    }

    public boolean hasOpenedInventory(UUID playerUUID) {
        return this.openedMenus.containsKey(playerUUID);
    }

    @Nullable
    public InventoryMenu getOpenendInventory(UUID playerUUID) {
        return this.openedMenus.get(playerUUID);
    }

    public void closeInventory(UUID playerUUID) {
        this.openedMenus.remove(playerUUID);
    }

    public static InventoryManager getInstance() {
        return PaperDreamMCAPI.getInstance().getInventoryManager();
    }
}
