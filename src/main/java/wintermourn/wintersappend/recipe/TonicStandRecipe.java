package wintermourn.wintersappend.recipe;

import com.google.gson.JsonArray;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import wintermourn.wintersappend.WintersAppend;
import wintermourn.wintersappend.block.entity.TonicStandBlockEntity;
import wintermourn.wintersappend.item.TonicItem;
import wintermourn.wintersappend.item.TonicUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TonicStandRecipe implements Recipe<TonicStandBlockEntity> {
    final Identifier id;

    public final int maxBrewingTime;
    public final int maxFuelCost;
    public final int maxPurityCost;
    public final int applicationLimit;

    public final DefaultedList<Ingredient> ingredients;
    public final Identifier output;
    public final StatusEffect outputEffect;
    public final ItemStack outputStack;

    public TonicStandRecipe(Identifier id, Identifier output, DefaultedList<Ingredient> input, int brewTime, int fuelCost, int purityCost)
    {
        this(id, output, input, brewTime, fuelCost, purityCost, 99);
    }
    public TonicStandRecipe(Identifier id, Identifier output, DefaultedList<Ingredient> input, int brewTime, int fuelCost, int purityCost, int applicationLimit)
    {
        this.id = id;
        if (input.size() > 3) throw new ArithmeticException("Ingredients count too long (must be <=3)");

        DefaultedList<Ingredient> ings = DefaultedList.ofSize(3, Ingredient.EMPTY);
        Collections.copy(ings, input);

        this.ingredients = ings;
        this.output = output;
        this.outputEffect = Registries.STATUS_EFFECT.get(output);
        this.outputStack = TonicUtil.getStack(outputEffect);
        this.maxBrewingTime = brewTime;
        this.maxFuelCost = fuelCost;
        this.maxPurityCost = purityCost;
        this.applicationLimit = applicationLimit;
    }

    public boolean matches(TonicStandBlockEntity inventory, World world) {
        if (inventory.getFuel() <= 0 && this.maxFuelCost > 0) return false; //this.maxFuelCost / inventory.getFuelEfficiency()

        boolean[] matchedStacks = new boolean[3];
        for (Ingredient ingredient : this.ingredients) {
            boolean matched = false;
            for (int j = 0; j < 3; j++) {
                if (matchedStacks[j]) continue;
                if (ingredient.test(inventory.getStack(j + 1))) {
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
        Map<StatusEffect, Integer> effects = TonicUtil.getTonicEffects(outputSlot);

        return PotionUtil.getPotion(outputSlot) == Potions.WATER ||
                (outputSlot.getItem() instanceof TonicItem item
                        && item.getMaxEffects() > TonicUtil.getEffectsCount(outputSlot)
                        && (!effects.containsKey(outputEffect) || effects.get(outputEffect) + 1 < this.applicationLimit));
    }

    public ItemStack craft(TonicStandBlockEntity inventory, DynamicRegistryManager registryManager) {
        ItemStack outputStack = inventory.getStack(TonicStandBlockEntity.OUTPUT_SLOT_ID);
        if (!outputStack.isEmpty() && outputStack.getItem() instanceof TonicItem)
        {
            List<StatusEffect> effects = TonicUtil.getTonicEffectsList(outputStack);
            WintersAppend.LOGGER.info(effects.toString());
            effects.add(outputEffect);
            return TonicUtil.getStack(effects);
        }
        return TonicUtil.getStack(outputEffect);
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
        return outputStack;
    }

//    public ItemStack getOutput(DynamicRegistryManager registryManager) {
//        return output;
//    }

    public Identifier getId() {
        return this.id;
    }

    public RecipeSerializer<?> getSerializer() {
        return TonicStandRecipeSerializer.INSTANCE;
    }

    public static class Type implements RecipeType<TonicStandRecipe>
    {
        private Type() {}
        public static final Type INSTANCE = new Type();

        public static final Identifier ID = new Identifier(WintersAppend.MOD_ID, "tonic_brewing");
    }

    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    class TonicStandRecipeJson
    {
        Integer purity_needed;
        Integer fuel_price;
        Integer brewing_time;
        JsonArray inputs;
        String output;
        Integer application_limit;
    }

}
