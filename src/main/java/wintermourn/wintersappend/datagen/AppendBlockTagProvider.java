package wintermourn.wintersappend.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import wintermourn.wintersappend.block.AppendBlocks;

import java.util.concurrent.CompletableFuture;

public class AppendBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    // TODO: use this probably
    public AppendBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(BlockTags.FLOWERS)
                .add(AppendBlocks.GYPSOPHILA.getBlock());
    }
}
