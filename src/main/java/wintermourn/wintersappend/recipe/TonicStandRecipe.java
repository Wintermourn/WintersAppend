package wintermourn.wintersappend.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.collection.DefaultedList;
import wintermourn.wintersappend.block.entity.TonicStandBlockEntity;

public abstract class TonicStandRecipe {
    public DefaultedList<Ingredient> ingredients;
    public int brewingTime;
    public int fuelCost;
    public int purityCost;

    public TonicStandRecipe(DefaultedList<Ingredient> ingredients, int brewTime, int fuel, int purity)
    {
        this.ingredients = ingredients;
        brewingTime = brewTime;
        fuelCost = fuel;
        purityCost = purity;
    }
    public abstract ItemStack craft(TonicStandBlockEntity inventory, DynamicRegistryManager registryManager);
    public abstract boolean isOutputValid(ItemStack output);
}
