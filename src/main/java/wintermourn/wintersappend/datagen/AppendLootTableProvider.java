package wintermourn.wintersappend.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import wintermourn.wintersappend.block.AppendBlocks;

public class AppendLootTableProvider extends FabricBlockLootTableProvider {
    public AppendLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        addDrop(AppendBlocks.TONIC_STAND.getBlock());
        addDrop(AppendBlocks.GYPSOPHILA.getBlock());
    }
}
