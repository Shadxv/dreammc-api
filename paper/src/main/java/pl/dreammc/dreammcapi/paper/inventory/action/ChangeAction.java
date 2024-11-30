package pl.dreammc.dreammcapi.paper.inventory.action;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import pl.dreammc.dreammcapi.paper.inventory.type.ChangeType;

@AllArgsConstructor
@Getter
public class ChangeAction {
    private final int slot;
    private final ItemStack itemStack;
    private final ChangeType type;
}
