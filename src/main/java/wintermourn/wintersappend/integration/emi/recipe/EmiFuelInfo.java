package wintermourn.wintersappend.integration.emi.recipe;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import wintermourn.wintersappend.WintersAppend;
import wintermourn.wintersappend.integration.emi.category.EmiFuelCategory;
import wintermourn.wintersappend.screen.TonicStandScreen;

import java.util.List;

public class EmiFuelInfo implements EmiRecipe {
    final Item item;
    final EmiIngredient emiIngredient;
    final int fuel;
    final int purity;

    public EmiFuelInfo(Item item, int fuel, int purity)
    {
        this.item = item;
        this.emiIngredient = EmiStack.of(item);
        this.fuel = fuel;
        this.purity = purity;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return EmiFuelCategory.CATEGORY;
    }

    @Override
    public @Nullable Identifier getId() {
        Identifier itemId = Registries.ITEM.getId(item);
        return new Identifier("generated", WintersAppend.MOD_ID +'/'+ itemId.getNamespace() +'/'+ itemId.getPath());
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(emiIngredient);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of();
    }

    @Override
    public int getDisplayWidth() {
        return 28 + 24;
    }

    @Override
    public int getDisplayHeight() {
        return 20;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        int offset = 1;
        widgets.addSlot(this.emiIngredient,0 + offset,0 + offset);

        widgets.addTexture(TonicStandScreen.TEXTURE, 18 + 2 + offset, 3 + offset,
            19, 6, 61, 43);
        widgets.addTexture(TonicStandScreen.TEXTURE, 18 + 2 + offset, 4 + offset,
            Math.round(18 * (this.fuel / 100f)), 4, 176, 29);
        widgets.addTexture(TonicStandScreen.TEXTURE, 18 + 2 + offset, 9 + offset,
            28, 6, 52, 50);
        widgets.addTexture(TonicStandScreen.TEXTURE, 18 + 2 + offset, 10 + offset,
            Math.round(27 * (this.purity / 200f)), 4, 176, 41);

        widgets.addTooltip(
            List.of(TooltipComponent.of(
                Text.translatable("recipe.winters_append.fuel.fuel"+ (fuel > -1 ? "" : ".none"), fuel).asOrderedText()
            )),
            18 + 2 + offset, 3 + offset, 19, 6
        );
        widgets.addTooltip(
            List.of(TooltipComponent.of(
                Text.translatable("recipe.winters_append.fuel.purity"+ (purity > -1 ? "" : ".none"), purity).asOrderedText()
            )),
            18 + 2 + offset, 9 + offset, 28, 6
        );
    }
}
