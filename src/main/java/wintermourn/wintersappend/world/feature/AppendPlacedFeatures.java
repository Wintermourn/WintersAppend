package wintermourn.wintersappend.world.feature;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.placementmodifier.RarityFilterPlacementModifier;
import wintermourn.wintersappend.WintersAppend;
import wintermourn.wintersappend.block.AppendBlocks;

import java.util.List;

public class AppendPlacedFeatures {
    public static final RegistryKey<PlacedFeature> GYPSOPHILA_FLOWER_PLACED = registerKey("gypsophila_placed");

    public static RegistryKey<PlacedFeature> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier(WintersAppend.MOD_ID, name));
    }

    private static void register(Registerable<PlacedFeature> context, RegistryKey<PlacedFeature> key, RegistryEntry<ConfiguredFeature<?, ?>> config,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(config, List.copyOf(modifiers)));
    }

    public static void bootstrap(Registerable<PlacedFeature> ctx)
    {
        RegistryEntryLookup<ConfiguredFeature<?, ?>> featureEntryLookup = ctx.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);

        register(ctx, GYPSOPHILA_FLOWER_PLACED, featureEntryLookup.getOrThrow(AppendConfiguredFeatures.GYPSOPHILA_FLOWER),
                VegetationPlacedFeatures.treeModifiersWithWouldSurvive(
                        RarityFilterPlacementModifier.of(30), AppendBlocks.GYPSOPHILA.getBlock()));
    }
}
