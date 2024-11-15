package wintermourn.wintersappend.integration.emi.category;

import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import wintermourn.wintersappend.WintersAppend;
import wintermourn.wintersappend.block.AppendBlocks;
import wintermourn.wintersappend.config.AppendFuelsConfig;
import wintermourn.wintersappend.integration.emi.recipe.EmiFuelInfo;

import java.util.Map;

public class EmiFuelCategory {
    public static final EmiStack WORKSTATION = EmiStack.of(AppendBlocks.TONIC_STAND.getBlock());

    public static final Identifier TYPE = new Identifier(WintersAppend.MOD_ID, "tonic_stand_fuel");
    public static final EmiRecipeCategory CATEGORY
            = new EmiRecipeCategory(TYPE, EmiTonicCategory.WORKSTATION,
        new EmiTexture(EmiTonicCategory.TEXTURE, 0, 0, 16, 16));

    public static void Register(EmiRegistry registry)
    {
        registry.addCategory(CATEGORY);
        registry.addWorkstation(CATEGORY, WORKSTATION);

        for (Map.Entry<Identifier, Pair<Integer, Integer>> entry : AppendFuelsConfig.Fuels.entrySet())
        {
            registry.addRecipe(new EmiFuelInfo(Registries.ITEM.get(entry.getKey()), entry.getValue().getLeft(), entry.getValue().getRight()));
        }

    }
}
