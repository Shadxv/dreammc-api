package pl.dreammc.dreammcapi.paper.inventory;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public abstract class UnlockedMenu extends InventoryMenu{
    protected UnlockedMenu(Player player, Component title, int rows) {
        super(player, title, rows);
    }
}
