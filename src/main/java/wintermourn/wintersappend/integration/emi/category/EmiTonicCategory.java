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
import wintermourn.wintersappend.recipe.TonicBrewingRecipe;

import java.util.ArrayList;
import java.util.List;

public class EmiTonicCategory {
    public static final Identifier TEXTURE = new Identifier(WintersAppend.MOD_ID, "textures/gui/emi/tonic_icon.png");
    public static final EmiStack WORKSTATION = EmiStack.of(AppendBlocks.TONIC_STAND.getBlock());

    public static final Comparison TONIC_COMPARISON = Comparison.of((recipe, item) -> {
        if (!(recipe.getItemStack().getItem() instanceof TonicItem) || !(item.getItemStack().getItem() instanceof TonicItem)) return false;

        if (!recipe.hasNbt() || !item.hasNbt()) return false;

        NbtList listA = recipe.getNbt().getList(TonicUtil.TONIC_EFFECTS_KEY, NbtElement.LIST_TYPE);
        NbtList listB = item.getNbt().getList(TonicUtil.TONIC_EFFECTS_KEY, NbtElement.LIST_TYPE);
        List<String> stringsA = new ArrayList<>(listA.size());
        List<String> stringsB = new ArrayList<>(listB.size());
        for (NbtElement element : listA)
        {
            if (element instanceof NbtList list)
            {
                String id = list.getString(0);
                if (id != null) stringsA.add(id);
            }
        }
        for (NbtElement element : listB)
        {
            if (element instanceof NbtList list)
            {
                String id = list.getString(0);
                if (id != null) stringsB.add(id);
            }
        }

        if (stringsA.size() != stringsB.size()) return false;

        for (String entry : stringsA)
        {
            if (!stringsB.contains(entry)) return false;
        }return true;
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

        registry.setDefaultComparison(EmiStack.of(AppendItems.TONIC), TONIC_COMPARISON); //Comparison.compareNbt()

        RecipeManager manager = registry.getRecipeManager();

        //registry.removeEmiStacks((stack) -> stack.getItemStack().getItem() == AppendItems.TONIC);

        List<StatusEffect> generatedEffects = new ArrayList<>();

        for (TonicBrewingRecipe recipe : manager.listAllOfType(TonicBrewingRecipe.Type.INSTANCE))
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
