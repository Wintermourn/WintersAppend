package wintermourn.wintersappend;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;
import wintermourn.wintersappend.datagen.AppendLootTableProvider;
import wintermourn.wintersappend.datagen.AppendModelProvider;
import wintermourn.wintersappend.datagen.AppendWorldGenProvider;
import wintermourn.wintersappend.world.feature.AppendConfiguredFeatures;
import wintermourn.wintersappend.world.feature.AppendPlacedFeatures;

public class WintersAppendDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(AppendLootTableProvider::new);
        pack.addProvider(AppendWorldGenProvider::new);
        pack.addProvider(AppendModelProvider::new);
    }

    @Override
    public void buildRegistry(RegistryBuilder registryBuilder) {
        registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, AppendConfiguredFeatures::bootstrap);
        registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, AppendPlacedFeatures::bootstrap);
    }
}
