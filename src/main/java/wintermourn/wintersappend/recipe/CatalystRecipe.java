package wintermourn.wintersappend.recipe;

import com.google.gson.JsonArray;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import wintermourn.wintersappend.WintersAppend;
import wintermourn.wintersappend.block.entity.TonicStandBlockEntity;
import wintermourn.wintersappend.item.AppendItems;
import wintermourn.wintersappend.item.CatalystUtil;

import java.util.Collections;
import java.util.Objects;

public class CatalystRecipe extends TonicStandRecipe implements Recipe<TonicStandBlockEntity> {
    final Identifier id;

    public final String catalystID;
    public final int color;

    public CatalystRecipe(Identifier id, String output, DefaultedList<Ingredient> input, int brewTime, int fuelCost, int purityCost)
    {
        this(id, output, input, brewTime, fuelCost, purityCost, -1);
    }
    public CatalystRecipe(Identifier id, String output, DefaultedList<Ingredient> input, int brewTime, int fuelCost, int purityCost, int color)
    {
        super(input, brewTime, fuelCost, purityCost);

        this.id = id;
        if (input.size() > 3) throw new ArithmeticException("Ingredients count too long (must be <=3)");

        DefaultedList<Ingredient> ings = DefaultedList.ofSize(3, Ingredient.EMPTY);
        Collections.copy(ings, input);

        this.catalystID = output;
        this.color = color;
    }

    public boolean matches(TonicStandBlockEntity inventory, World world) {
        if (inventory.getFuel() <= 0 && this.fuelCost > 0) return false; //this.maxFuelCost / inventory.getFuelEfficiency()

        boolean[] matchedStacks = new boolean[3];
        for (Ingredient ingredient : this.ingredients) {
            boolean matched = false;
            for (int j = 0; j < 3; j++) {
                if (matchedStacks[j]) continue;
                if (ingredient.test(inventory.getStack(j + 1))) {
                    ItemStack inventoryStack = inventory.getStack(j + 1);
                    if (inventoryStack.getItem() == AppendItems.CATALYST)
                    {
                        boolean validCatalyst = false;
                        for (ItemStack item : ingredient.getMatchingStacks())
                        {
                            if (!item.hasNbt() || !inventoryStack.hasNbt()) continue;
                            assert inventoryStack.getNbt() != null;
                            assert item.getNbt() != null;
                            if (Objects.equals(item.getNbt().getString("id"), inventoryStack.getNbt().getString("id")))
                            {
                                validCatalyst = true; break;
                            }
                        }
                        if (!validCatalyst) continue;
                    }
                    matched = true;
                    matchedStacks[j] = true;
                    break;
                }
            }
            if (!matched) {
                return false;
            }
        }

        ItemStack outputSlot = inventory.getStack(TonicStandBlockEntity.OUTPUT_SLOT_ID);

        return PotionUtil.getPotion(outputSlot) == Potions.WATER;
    }

    @Override
    public boolean isOutputValid(ItemStack output) {
        return PotionUtil.getPotion(output) == Potions.WATER;
    }

    public ItemStack craft(TonicStandBlockEntity inventory, DynamicRegistryManager registryManager) {
        return color > -1 ? CatalystUtil.getStack(catalystID, color) : CatalystUtil.getStack(catalystID);
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        return ingredients;
    }

    public boolean fits(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return color > -1 ? CatalystUtil.getStack(catalystID, color) : CatalystUtil.getStack(catalystID);
    }

//    public ItemStack getOutput(DynamicRegistryManager registryManager) {
//        return output;
//    }

    public Identifier getId() {
        return this.id;
    }
    public boolean isIgnoredInRecipeBook() { return true; }

    public RecipeSerializer<?> getSerializer() {
        return CatalystRecipeSerializer.INSTANCE;
    }

    public static class Type implements RecipeType<CatalystRecipe>
    {
        private Type() {}
        public static final Type INSTANCE = new Type();

        public static final Identifier ID = new Identifier(WintersAppend.MOD_ID, "catalyst_brewing");
    }

    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    class CatalystRecipeJson
    {
        Integer purity_needed;
        Integer fuel_price;
        Integer brewing_time;
        Integer color;
        JsonArray inputs;
        String output;
    }

}
