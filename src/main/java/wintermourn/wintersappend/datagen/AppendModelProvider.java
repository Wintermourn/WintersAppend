package wintermourn.wintersappend.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import wintermourn.wintersappend.item.AppendItems;

public class AppendModelProvider extends FabricModelProvider {
    public AppendModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator) {
        //generator.registerFlowerPotPlant(AppendBlocks.GYPSOPHILA.getBlock(), AppendBlocks.GYPSOPHILA_POTTED, BlockStateModelGenerator.TintType.NOT_TINTED);
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {

        //generator.register(AppendBlocks.GYPSOPHILA.getItem(), Models.GENERATED);
        generator.register(AppendItems.SOUL_MEAL, Models.GENERATED);

    }
}
