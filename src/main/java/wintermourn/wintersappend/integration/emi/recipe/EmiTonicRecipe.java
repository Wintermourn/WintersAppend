package wintermourn.wintersappend.integration.emi.recipe;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.Ingredient;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringHelper;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;
import wintermourn.wintersappend.integration.emi.category.EmiTonicCategory;
import wintermourn.wintersappend.item.TonicUtil;
import wintermourn.wintersappend.recipe.TonicBrewingRecipe;
import wintermourn.wintersappend.screen.TonicStandScreen;
import wintermourn.wintersappend.config.AppendServerConfig;

import java.util.ArrayList;
import java.util.List;

public class EmiTonicRecipe implements EmiRecipe {

    private final Identifier id;
    private final DefaultedList<EmiIngredient> ingredients;
    private final EmiIngredient output;
    private final int fuelCost;
    private final int purityCost;
    private final int brewTime;
    private final int applicationLimit;
    private final int spanOffset;

    public EmiTonicRecipe(TonicBrewingRecipe recipe)
    {
        this.id = recipe.getId();

        DefaultedList<Ingredient> recipeIngredients = recipe.getIngredients();
        this.ingredients = DefaultedList.ofSize(4, EmiIngredient.of(Ingredient.EMPTY));
        this.ingredients.set(0, EmiIngredient.of(recipeIngredients.get(0)));
        this.ingredients.set(1, EmiIngredient.of(recipeIngredients.get(1)));
        this.ingredients.set(2, EmiIngredient.of(recipeIngredients.get(2)));
        this.ingredients.set(3, EmiIngredient.of(Ingredient.ofStacks(
            PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.WATER),
            TonicUtil.getRepresentative()
        )));

        List<ItemStack> tonics = new ArrayList<>(Math.min(recipe.applicationLimit, AppendServerConfig.defaultTonicEffects));


        List<StatusEffect> effectList = new ArrayList<>();
        for (int i = 0; i < Math.min(recipe.applicationLimit, AppendServerConfig.defaultTonicEffects); i++) {
            effectList.add(recipe.outputEffect);
            if (recipe.spanOffset != 0)
            {
                tonics.add(TonicUtil.getStack(effectList, recipe.spanOffset));
            } else
                tonics.add(TonicUtil.getStack(effectList));
        }
        this.output = EmiIngredient.of(Ingredient.ofStacks(tonics.toArray(new ItemStack[0])));

        this.fuelCost = recipe.fuelCost;
        this.purityCost = recipe.purityCost;
        this.brewTime = recipe.brewingTime;
        this.applicationLimit = recipe.applicationLimit;
        this.spanOffset = recipe.spanOffset;
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
        return this.output.getEmiStacks();
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

        widgets.addSlot(output, 62, 42);

        widgets.addTexture(TonicStandScreen.TEXTURE, 44, 29,
            Math.round(18 * (this.fuelCost / 50f)), 4, 176, 33);
        widgets.addTexture(TonicStandScreen.TEXTURE, 44, 29,
            Math.round(18 * (this.fuelCost / 100f)), 4, 176, 29);
        widgets.addTexture(TonicStandScreen.TEXTURE, 35, 36,
            Math.round(27 * (this.purityCost / 200f)), 4, 176, 41);
        widgets.addTexture(TonicStandScreen.TEXTURE, 0, 45,
            11, 11, 176, 45);
        widgets.addTexture(TonicStandScreen.TEXTURE, 0, 45,
            11, 11, 198, 45);

        int iconsOffset = 0;
        if (this.applicationLimit < 99)
        {
            widgets.addTexture(TonicStandScreen.TEXTURE, 74, 33,
                11, 8, 209, 0);
            widgets.addTooltip(
                List.of(
                    TooltipComponent.of(Text.translatable("recipe.winters_append.cost.limit", this.applicationLimit).asOrderedText()),
                    TooltipComponent.of(Text.translatable("recipe.winters_append.explainLimit", this.applicationLimit).formatted(Formatting.GRAY).asOrderedText())
                ),
                74, 33, 11, 8
            );

            iconsOffset += 11;
        }

        if (this.spanOffset != 0)
        {
            if (this.spanOffset > 0)
            {
                widgets.addTexture(TonicStandScreen.TEXTURE, 75 + iconsOffset, 35,
                    10, 4, 209, 12);
                widgets.addTooltip(
                    List.of(
                        TooltipComponent.of(Text.translatable("recipe.winters_append.output.span.good").formatted(Formatting.AQUA, Formatting.BOLD).asOrderedText()),
                        TooltipComponent.of(Text.translatable("recipe.winters_append.output.span.good.desc", StringHelper.formatTicks(this.spanOffset))
                            .formatted(Formatting.GRAY).asOrderedText())
                    ),
                    75 + iconsOffset, 35, 10, 4
                );
            } else
            {
                widgets.addTexture(TonicStandScreen.TEXTURE, 75 + iconsOffset, 35,
                    10, 4, 209, 16);
                widgets.addTooltip(
                    List.of(
                        TooltipComponent.of(Text.translatable("recipe.winters_append.output.span.bad").formatted(Formatting.RED, Formatting.BOLD).asOrderedText()),
                        TooltipComponent.of(Text.translatable("recipe.winters_append.output.span.bad.desc", StringHelper.formatTicks(-this.spanOffset))
                            .formatted(Formatting.GRAY).asOrderedText())
                    ),
                    75 + iconsOffset, 35, 10, 4
                );
            }
        }


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
                TooltipComponent.of(Text.translatable("recipe.winters_append.type.tonic").asOrderedText())
            ),
            0, 45, 18, 11
        );
    }
}
