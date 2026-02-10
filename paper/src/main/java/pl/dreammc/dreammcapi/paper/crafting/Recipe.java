package pl.dreammc.dreammcapi.paper.crafting;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.api.util.StringUtil;
import pl.dreammc.dreammcapi.paper.item.BaseItem;

@Getter
public class Recipe {

    private final @Nullable BaseItem<?>[][] recipeGrid;
    private final @NotNull BaseItem<?> result;

    public Recipe(@Nullable BaseItem<?>[][] recipeGrid, @NotNull BaseItem<?> result) {
        this.recipeGrid = recipeGrid;
        this.result = result;
    }

    public String buildKey() {
        return StringUtil.mapTwoDimentionalArrayToString(this.recipeGrid, baseItem -> baseItem.getMaterial().name());
    }

    public boolean canCraft(ItemStack[][] craftingGrid) {
        try {
            if (this.recipeGrid == null) return false;
            for (int y = 0; y < this.recipeGrid.length; y++) {
                for (int x = 0; x < this.recipeGrid[y].length; x++) {
                    if (this.recipeGrid[y][x] == null) continue;

                    if (craftingGrid[y][x] == null) return false;

                    if (this.recipeGrid[y][x].getAmount() > craftingGrid[y][x].getAmount()) return false;
                }
            }
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }
}
