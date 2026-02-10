package pl.dreammc.dreammcapi.paper.inventory;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.joml.Vector2i;
import pl.dreammc.dreammcapi.paper.crafting.Recipe;
import pl.dreammc.dreammcapi.paper.crafting.RecipeManager;
import pl.dreammc.dreammcapi.paper.inventory.action.ChangeAction;
import pl.dreammc.dreammcapi.paper.item.InventoryItem;
import pl.dreammc.dreammcapi.paper.type.StaticItemsEnum;

public abstract class CreftingMenu extends UnlockedMenu {

    private final RecipeManager recipeManager;
    private static final int[][] GRID = {{10, 11, 12}, {19, 20, 21}, {28, 29, 30}};
    private static final int RESULT_INDEX = 24;

    public CreftingMenu(Player player, Component title, RecipeManager recipeManager) {
        super(player, title, 6,
                new Vector2i(1,1), new Vector2i(3,3));
        this.recipeManager = recipeManager;
    }

    @Override
    public void handleChange(ChangeAction changeAction) {
        ItemStack[][] grid = new ItemStack[3][3];
        for (int y = 0; y < GRID.length; y++) {
            int[] row = GRID[y];
            for (int x = 0; x < GRID[y].length; x++) {
                grid[y][x] = this.getHandledItems().get(row[x]);
            }
        }

        Recipe recipe = this.recipeManager.findRecipe(grid);
        if (recipe == null || !recipe.canCraft(grid)) {
            this.addItem(RESULT_INDEX, null);
            return;
        }

        ItemStack result = recipe.getResult().build();
        InventoryItem resultItem = new InventoryItem(result);
        resultItem.click(event -> {
            if (event.getCursor().getType() == Material.AIR) {
                event.setCursor(recipe.getResult().build());
            } else if (event.getCursor().getType() == result.getType() && event.getCursor().getAmount() + recipe.getResult().getAmount() + result.getAmount() <= result.getMaxStackSize()) {
                event.setCursor(event.getCursor().asQuantity(event.getCursor().getAmount() + recipe.getResult().getAmount()));
            }
            this.removeItems(recipe);
        });
        this.addItem(RESULT_INDEX, resultItem);
    }

    private void removeItems(Recipe recipe) {
        for (int y = 0; y < GRID.length; y++) {
            int[] row = GRID[y];
            for (int x = 0; x < GRID[y].length; x++) {
                if (this.getHandledItems().get(row[x]) == null) continue;
                if (recipe.getRecipeGrid()[y][x] == null) continue;
                this.addItemToContnet(row[x], this.getHandledItems().get(row[x]).asQuantity(this.getHandledItems().get(row[x]).getAmount() - recipe.getRecipeGrid()[y][x].getAmount()));
            }
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {

        this.fillRow(0, StaticItemsEnum.GLASS_FILL.getCustomItem());
        this.fillRow(1, StaticItemsEnum.GLASS_FILL.getCustomItem());
        this.fillRow(2, StaticItemsEnum.GLASS_FILL.getCustomItem());
        this.fillRow(3, StaticItemsEnum.GLASS_FILL.getCustomItem());
        this.fillRow(4, StaticItemsEnum.GLASS_FILL.getCustomItem());
        this.fillRow(5, StaticItemsEnum.GLASS_FILL.getCustomItem());

        for (int y = 0; y < GRID.length; y++) {
            int[] row = GRID[y];
            for (int x = 0; x < GRID[y].length; x++) {
                this.addItem(row[x], null);
            }
        }

        this.addItem(RESULT_INDEX, null);

        this.onOpen0(event);
    }

    protected abstract void onOpen0(InventoryOpenEvent event);

    @Override
    protected boolean onClose0(InventoryCloseEvent event) {
        return true;
    }
}