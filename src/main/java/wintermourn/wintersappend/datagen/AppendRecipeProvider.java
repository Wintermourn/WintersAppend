package wintermourn.wintersappend.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.Items;
import wintermourn.wintersappend.block.AppendBlocks;

import java.util.function.Consumer;

public class AppendRecipeProvider extends FabricRecipeProvider {
    public AppendRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        //offerPlanksRecipe(exporter, Items.WHITE_DYE, TagKey.of(RegistryKeys.ITEM, ));
        offerSingleOutputShapelessRecipe(exporter, Items.WHITE_DYE, AppendBlocks.GYPSOPHILA.getItem(), "white_dye");
    }

}
