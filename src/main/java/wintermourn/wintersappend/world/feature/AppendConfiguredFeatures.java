package wintermourn.wintersappend.world.feature;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import wintermourn.wintersappend.WintersAppend;
import wintermourn.wintersappend.block.AppendBlocks;

public class AppendConfiguredFeatures {
    public static final RegistryKey<ConfiguredFeature<?, ?>> GYPSOPHILA_FLOWER = registerKey("gypsophila_flower");

    private static RegistryKey<ConfiguredFeature<?, ?>> registerKey(String path)
    {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, new Identifier(WintersAppend.MOD_ID, path));
    }
    private static <C extends FeatureConfig, F extends Feature<C>> void register(
            Registerable<ConfiguredFeature<?, ?>> ctx,
            RegistryKey<ConfiguredFeature<?, ?>> key, F feature, C config)
    {
        ctx.register(key, new ConfiguredFeature<>(feature, config));
    }

    public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> ctx)
    {
        register(ctx, GYPSOPHILA_FLOWER, Feature.FLOWER,
                new RandomPatchFeatureConfig(
                        40, 3, 3, PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK,
                        new SimpleBlockFeatureConfig(BlockStateProvider.of(AppendBlocks.GYPSOPHILA.getBlock())))
                ));
    }
}
