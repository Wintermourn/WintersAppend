package wintermourn.wintersappend.integration.emi.recipe;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.Ingredient;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;
import wintermourn.wintersappend.config.AppendServerConfig;
import wintermourn.wintersappend.integration.emi.category.EmiTonicCategory;
import wintermourn.wintersappend.item.CatalystUtil;
import wintermourn.wintersappend.recipe.CatalystRecipe;
import wintermourn.wintersappend.screen.TonicStandScreen;

import java.util.ArrayList;
import java.util.List;

public class EmiCatalystRecipe implements EmiRecipe {

    private final Identifier id;
    private final DefaultedList<EmiIngredient> ingredients;
    private final List<EmiStack> output;
    private final int fuelCost;
    private final int purityCost;
    private final int brewTime;

    public EmiCatalystRecipe(CatalystRecipe recipe)
    {
        this.id = recipe.getId();

        DefaultedList<Ingredient> recipeIngredients = recipe.getIngredients();
        this.ingredients = DefaultedList.ofSize(4, EmiIngredient.of(Ingredient.EMPTY));
        this.ingredients.set(0, EmiIngredient.of(recipeIngredients.get(0)));
        this.ingredients.set(1, EmiIngredient.of(recipeIngredients.get(1)));
        this.ingredients.set(2, EmiIngredient.of(recipeIngredients.get(2)));
        this.ingredients.set(3, EmiIngredient.of(Ingredient.ofStacks(
            PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.WATER)
        )));

        this.output = new ArrayList<>(1);
        this.output.add(0, EmiStack.of(CatalystUtil.getStack(recipe.catalystID)));

        this.fuelCost = recipe.fuelCost;
        this.purityCost = recipe.purityCost;
        this.brewTime = recipe.brewingTime;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return EmiTonicCategory.CATEGORY;
    }

    @Override
    public @Nullable Identifier getId() {
        return this.id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return this.ingredients;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return this.output;
    }

    @Override
    public int getDisplayWidth() {
        return 112;
    }

    @Override
    public int getDisplayHeight() {
        return 60;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(TonicStandScreen.TEXTURE, 0, 0, 112, 60, 17, 15);
        //168, 74
        widgets.addSlot(ingredients.get(0), 62, 1);
        widgets.addSlot(ingredients.get(1), 42, 42);
        widgets.addSlot(ingredients.get(2), 82, 42);

        widgets.addSlot(output.get(0), 62, 42);

        widgets.addTexture(TonicStandScreen.TEXTURE, 44, 29,
            Math.round(18 * (this.fuelCost / 50f)), 4, 176, 33);
        widgets.addTexture(TonicStandScreen.TEXTURE, 44, 29,
            Math.round(18 * (this.fuelCost / 100f)), 4, 176, 29);
        widgets.addTexture(TonicStandScreen.TEXTURE, 35, 36,
            Math.round(27 * (this.purityCost / 200f)), 4, 176, 41);
        widgets.addTexture(TonicStandScreen.TEXTURE, 7, 45,
            11, 11, 176, 45);
        widgets.addTexture(TonicStandScreen.TEXTURE, 7, 45,
            11, 11, 209, 45);

        widgets.addTooltip(
            this.fuelCost > 0 ?
            List.of(
                TooltipComponent.of(Text.translatable("recipe.winters_append.cost.fuelCost", this.fuelCost).asOrderedText()),
                TooltipComponent.of(Text.translatable("recipe.winters_append.explainFuel").formatted(Formatting.GRAY).asOrderedText()),
                TooltipComponent.of(ScreenTexts.SPACE.asOrderedText()),
                TooltipComponent.of(Text.translatable("recipe.winters_append.cost.noPurity").formatted(Formatting.GOLD).asOrderedText()),
                TooltipComponent.of(Text.translatable("recipe.winters_append.cost.fuelCost", this.fuelCost * AppendServerConfig.impureFuelPenalty).asOrderedText())
            ) :
            List.of(
                TooltipComponent.of(Text.translatable("recipe.winters_append.cost.fuelCost", this.fuelCost).asOrderedText())
            ),
            43, 28, 20, 6
        );
        widgets.addTooltip(
            List.of(
                TooltipComponent.of(Text.translatable("recipe.winters_append.cost.purityCost", this.purityCost).asOrderedText()),
                TooltipComponent.of(Text.translatable("recipe.winters_append.explainPurity").formatted(Formatting.GRAY).asOrderedText())
            ),
            34, 35, 29, 6
        );
        widgets.addTooltip(
            List.of(
                TooltipComponent.of(Text.translatable("recipe.winters_append.cost.time", this.brewTime / 20f).asOrderedText()),
                TooltipComponent.of(ScreenTexts.SPACE.asOrderedText()),
                TooltipComponent.of(Text.translatable("recipe.winters_append.cost.noPurity").formatted(Formatting.GOLD).asOrderedText()),
                TooltipComponent.of(Text.translatable("recipe.winters_append.cost.time", this.brewTime / 20f * AppendServerConfig.impureTimePenalty).asOrderedText())
            ),
            81, 2, 9, 27
        );
        widgets.addTooltip(
            List.of(
                TooltipComponent.of(Text.translatable("recipe.winters_append.type.catalyst").asOrderedText())
            ),
            0, 45, 18, 11
        );
    }
}
