package pl.dreammc.dreammcapi.paper.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import pl.dreammc.dreammcapi.api.util.BaseColor;
import pl.dreammc.dreammcapi.api.util.TextUtil;
import pl.dreammc.dreammcapi.paper.item.BaseItem;
import pl.dreammc.dreammcapi.paper.item.InventoryItem;

@Getter
@AllArgsConstructor
public enum StaticItemsEnum {

    CLOSE_ITEM(new InventoryItem().setMaterial(Material.BARRIER).setName(TextUtil.deserializeText("&" + BaseColor.redPrimary + "Zamknij")).click(event -> {
        event.getWhoClicked().closeInventory();
    })),
    INFO_ITEM(new InventoryItem().setMaterial(Material.FILLED_MAP).setName(TextUtil.deserializeText("&" + BaseColor.orangePrimary + "&l" + TextUtil.stylizeText("PRZYDATNE INFORMACJE:"))).addItemFlag(ItemFlag.HIDE_ATTRIBUTES).addItemFlag(ItemFlag.HIDE_ADDITIONAL_TOOLTIP)),
    GLASS_FILL(new InventoryItem().setMaterial(Material.BLACK_STAINED_GLASS_PANE).setName(Component.text(" "))),
    BACK(new InventoryItem().setMaterial(Material.SPECTRAL_ARROW).setName(TextUtil.deserializeText("&" + BaseColor.orangePrimary + "Powrót")));

    private final BaseItem<?> customItem;

}
