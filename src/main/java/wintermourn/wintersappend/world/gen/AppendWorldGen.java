package wintermourn.wintersappend.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import wintermourn.wintersappend.world.feature.AppendPlacedFeatures;

public class AppendWorldGen {
    public static void RegisterWorldGen()
    {
        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(
                        BiomeKeys.BIRCH_FOREST,
                        BiomeKeys.BEACH,
                        BiomeKeys.FLOWER_FOREST,
                        BiomeKeys.FOREST,
                        BiomeKeys.WINDSWEPT_HILLS,
                        BiomeKeys.WINDSWEPT_FOREST,
                        BiomeKeys.PLAINS
                ),
                GenerationStep.Feature.VEGETAL_DECORATION,
                AppendPlacedFeatures.GYPSOPHILA_FLOWER_PLACED
        );
    }
}
