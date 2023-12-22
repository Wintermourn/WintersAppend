package wintermourn.wintersappend.integration.emi.category;

import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.Comparison;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Identifier;
import wintermourn.wintersappend.WintersAppend;
import wintermourn.wintersappend.block.AppendBlocks;
import wintermourn.wintersappend.category.AppendCreativeCategory;
import wintermourn.wintersappend.integration.emi.recipe.EmiTonicRecipe;
import wintermourn.wintersappend.item.AppendItems;
import wintermourn.wintersappend.item.TonicItem;
import wintermourn.wintersappend.item.TonicUtil;
import wintermourn.wintersappend.recipe.TonicStandRecipe;

import java.util.ArrayList;
import java.util.List;

public class EmiTonicCategory {
    public static final Identifier TEXTURE = new Identifier(WintersAppend.MOD_ID, "textures/gui/emi/tonic_icon.png");
    public static final EmiStack WORKSTATION = EmiStack.of(AppendBlocks.TONIC_STAND.getBlock());

    public static final Comparison TONIC_COMPARISON = Comparison.of((recipe, item) -> {
        if (!(recipe.getItemStack().getItem() instanceof TonicItem) || !(item.getItemStack().getItem() instanceof TonicItem)) return false;

        NbtList listA = recipe.getNbt().getList(TonicUtil.TONIC_EFFECTS_KEY, NbtElement.STRING_TYPE);
        NbtList listB = item.getNbt().getList(TonicUtil.TONIC_EFFECTS_KEY, NbtElement.STRING_TYPE);

        for (NbtElement element : listA) {
            if (!listB.contains(element)) return false;
        }
        return true;
    });
    public static final EmiRecipeCategory CATEGORY
            = new EmiRecipeCategory(
                    new Identifier(WintersAppend.MOD_ID, "tonic_stand"),
            WORKSTATION,
            new EmiTexture(TEXTURE, 0, 0, 16, 16)
    );

    public static void Register(EmiRegistry registry)
    {
        registry.addCategory(CATEGORY);

        registry.addWorkstation(CATEGORY, WORKSTATION);

        registry.setDefaultComparison(EmiStack.of(AppendItems.TONIC), Comparison.compareNbt());

        RecipeManager manager = registry.getRecipeManager();

        //registry.removeEmiStacks((stack) -> stack.getItemStack().getItem() == AppendItems.TONIC);

        List<StatusEffect> generatedEffects = new ArrayList<>();

        for (TonicStandRecipe recipe : manager.listAllOfType(TonicStandRecipe.Type.INSTANCE))
        {
            registry.addRecipe(new EmiTonicRecipe(recipe));

            if (generatedEffects.contains(recipe.outputEffect)) continue;
            List<StatusEffect> effects = new ArrayList<>();
            for (int i = 0; i < Math.min(recipe.applicationLimit, 3); i++) {
                effects.add(recipe.outputEffect);
                ItemStack thisStack = TonicUtil.getStack(effects);
                if (!AppendCreativeCategory.APPEND_GROUP.contains(thisStack))
                {
                    registry.addEmiStack(EmiStack.of(thisStack));
                    generatedEffects.add(recipe.outputEffect);
                }
            }
        }

    }
}
