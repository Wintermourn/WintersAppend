package wintermourn.wintersappend.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.world.gen.GenerationStep;
import wintermourn.wintersappend.world.feature.AppendPlacedFeatures;

public class AppendWorldGen {
    public static void RegisterWorldGen()
    {
        BiomeModifications.addFeature(
                BiomeSelectors.tag(BiomeTags.IS_SAVANNA).or(t -> t.hasTag(BiomeTags.IS_BADLANDS)),
                GenerationStep.Feature.VEGETAL_DECORATION,
                AppendPlacedFeatures.GYPSOPHILA_FLOWER_PLACED
        );

    }
}
