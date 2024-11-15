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
import wintermourn.wintersappend.item.AppendItems;
import wintermourn.wintersappend.item.TonicItem;
import wintermourn.wintersappend.item.TonicUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TonicBrewingRecipe extends TonicStandRecipe implements Recipe<TonicStandBlockEntity> {
    final Identifier id;

    public final int applicationLimit;
    public final int spanOffset;

    public final Identifier output;
    public final StatusEffect outputEffect;
    public final ItemStack outputStack;

    public TonicBrewingRecipe(Identifier id, Identifier output, DefaultedList<Ingredient> input, int brewTime, int fuelCost, int purityCost)
    {
        this(id, output, input, brewTime, fuelCost, purityCost, 99, 0);
    }
    public TonicBrewingRecipe(Identifier id, Identifier output, DefaultedList<Ingredient> input, int brewTime, int fuelCost, int purityCost, int applicationLimit, int spanOffset)
    {
        super(input, brewTime, fuelCost, purityCost);
        this.id = id;
        if (input.size() > 3) throw new ArithmeticException("Ingredients count too long (must be <=3)");

        DefaultedList<Ingredient> ings = DefaultedList.ofSize(3, Ingredient.EMPTY);
        Collections.copy(ings, input);

        this.output = output;
        this.outputEffect = Registries.STATUS_EFFECT.get(output);
        this.outputStack = TonicUtil.getStack(outputEffect);
        this.applicationLimit = applicationLimit;
        this.spanOffset = spanOffset;
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
        Map<StatusEffect, Integer> effects = TonicUtil.getTonicEffects(outputSlot);

        return PotionUtil.getPotion(outputSlot) == Potions.WATER ||
                (outputSlot.getItem() instanceof TonicItem item
                        && item.getMaxEffects() > TonicUtil.getEffectsCount(outputSlot)
                        && (!effects.containsKey(outputEffect) || effects.get(outputEffect) + 1 < this.applicationLimit));
    }

    @Override
    public boolean isOutputValid(ItemStack output) {
        return PotionUtil.getPotion(output) == Potions.WATER ||
                (output.getItem() instanceof TonicItem item && item.getMaxEffects() > TonicUtil.getEffectsCount(output));
    }

    public ItemStack craft(TonicStandBlockEntity inventory, DynamicRegistryManager registryManager) {
        ItemStack outputStack = inventory.getStack(TonicStandBlockEntity.OUTPUT_SLOT_ID);
        ItemStack output;
        if (!outputStack.isEmpty() && outputStack.getItem() instanceof TonicItem)
        {
            List<StatusEffect> effects = TonicUtil.getTonicEffectsListFlat(outputStack);
            WintersAppend.LOGGER.info(effects.toString());
            effects.add(outputEffect);

            output = TonicUtil.getStack(effects);
        }
        else
            output = TonicUtil.getStack(outputEffect);

        TonicUtil.setEffectSpan(output, spanOffset + (outputStack.hasNbt() ? Objects.requireNonNull(outputStack.getNbt()).getInt("spanOffset") : 0));
        return output;
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
    public boolean isIgnoredInRecipeBook() { return true; }

    public RecipeSerializer<?> getSerializer() {
        return TonicBrewingRecipeSerializer.INSTANCE;
    }

    public static class Type implements RecipeType<TonicBrewingRecipe>
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
        Integer span_offset;
        Integer fuel_price;
        Integer brewing_time;
        JsonArray inputs;
        String output;
        Integer application_limit;
    }

}
