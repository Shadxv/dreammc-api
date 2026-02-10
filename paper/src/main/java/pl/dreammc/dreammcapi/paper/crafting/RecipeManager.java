package pl.dreammc.dreammcapi.paper.crafting;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import pl.dreammc.dreammcapi.api.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

public class RecipeManager {

    private final Map<String, Recipe> registry;

    public RecipeManager() {
        this.registry = new HashMap<>();
    }

    protected String buildKey(ItemStack[][] craftingGrid) {
        int minX = 3;
        int minY = 3;
        int maxX = -1;
        int maxY = -1;
        boolean empty = true;

        for (int y = 0; y < craftingGrid.length; y++) {
            for (int x = 0; x < craftingGrid[y].length; x++) {
                if (craftingGrid[y][x] != null) {
                    minX = Math.min(minX, x);
                    maxX = Math.max(maxX, x);
                    minY = Math.min(minY, y);
                    maxY = Math.max(maxY, y);
                    empty = false;
                }
            }
        }

        if (empty) return "";

        ItemStack[][] finalGrid = new ItemStack[maxY - minY + 1][maxX - minX + 1];
        for (int y = minY; y <= maxY; y++) {
            if (maxX + 1 - minX >= 0)
                System.arraycopy(craftingGrid[y], minX, finalGrid[y - minY], 0, maxX + 1 - minX);
        }

        return StringUtil.mapTwoDimentionalArrayToString(finalGrid, itemStack -> itemStack.getType().name());
    }

    public @Nullable Recipe findRecipe(ItemStack[][] craftingGrid) {
        return this.registry.get(this.buildKey(craftingGrid));
    }

    public void registerRecipe(Recipe recipe) {
        this.registry.put(recipe.buildKey(), recipe);
    }

}