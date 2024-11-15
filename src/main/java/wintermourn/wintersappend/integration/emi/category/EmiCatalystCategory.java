package wintermourn.wintersappend.integration.emi.category;

import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.stack.Comparison;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.recipe.RecipeManager;
import wintermourn.wintersappend.block.AppendBlocks;
import wintermourn.wintersappend.integration.emi.recipe.EmiCatalystRecipe;
import wintermourn.wintersappend.item.*;
import wintermourn.wintersappend.recipe.CatalystRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static wintermourn.wintersappend.integration.emi.category.EmiTonicCategory.CATEGORY;

public class EmiCatalystCategory {
    public static final EmiStack WORKSTATION = EmiStack.of(AppendBlocks.TONIC_STAND.getBlock());

    public static final Comparison CATALYST_COMPARISON = Comparison.of((recipe, item) -> {
        if (!(recipe.getItemStack().getItem() instanceof CatalystItem) || !(item.getItemStack().getItem() instanceof CatalystItem)) return false;

        if (!recipe.hasNbt() || !item.hasNbt()) return false;

        String stringA = recipe.getNbt().getString("id");
        String stringB = item.getNbt().getString("id");

        return Objects.equals(stringA, stringB);
    });

    public static void Register(EmiRegistry registry)
    {
        registry.addCategory(CATEGORY);

        registry.addWorkstation(CATEGORY, WORKSTATION);

        registry.setDefaultComparison(EmiStack.of(AppendItems.CATALYST), CATALYST_COMPARISON); //Comparison.compareNbt()

        RecipeManager manager = registry.getRecipeManager();

        //registry.removeEmiStacks((stack) -> stack.getItemStack().getItem() == AppendItems.TONIC);
        List<String> catalystIDs = new ArrayList<>();

        for (CatalystRecipe recipe : manager.listAllOfType(CatalystRecipe.Type.INSTANCE))
        {
            registry.addRecipe(new EmiCatalystRecipe(recipe));

            if (!catalystIDs.contains(recipe.catalystID))
            {
                registry.addEmiStack(EmiStack.of(CatalystUtil.getStack(recipe.catalystID)));
                catalystIDs.add(recipe.catalystID);
            }
        }

    }
}
