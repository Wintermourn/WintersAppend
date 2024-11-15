package wintermourn.wintersappend.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import wintermourn.wintersappend.WintersAppend;
import wintermourn.wintersappend.config.AppendFuelsConfig;
import wintermourn.wintersappend.effects.MinorEffects;
import wintermourn.wintersappend.item.AppendItems;

public class AppendBlocks {
    public static final Identifier TONIC_STAND_ID = new Identifier(WintersAppend.MOD_ID, "tonic_stand");
    public static final BlockItemPair TONIC_STAND;

    public static final Identifier GYPSOPHILA_ID = new Identifier(WintersAppend.MOD_ID, "gypsophila");
    public static final BlockItemPair GYPSOPHILA;
    public static final Block GYPSOPHILA_POTTED;

    static
    {
        TONIC_STAND = BlockItemPair.fromBlock(
                new TonicStandBlock(FabricBlockSettings.copyOf(Blocks.BREWING_STAND)),
                new Item.Settings()
        );
        GYPSOPHILA = BlockItemPair.fromBlock(new FertilizableFlowerBlock(MinorEffects.SPEED, 20*30,0.08, FabricBlockSettings.copy(Blocks.DANDELION))
                .addAlternateChance(AppendItems.SOUL_MEAL, 0.3));
        GYPSOPHILA_POTTED = new FlowerPotBlock(GYPSOPHILA.getBlock(), FabricBlockSettings.copy(Blocks.POTTED_ALLIUM));
    }

    static void register(Identifier id, BlockItemPair block)
    {
        Registry.register(Registries.BLOCK, id, block.getBlock());
        Registry.register(Registries.ITEM, id, block.getItem());
    }
    static void register(Identifier id, Block block)
    {
        Registry.register(Registries.BLOCK, id, block);
    }

    public static void Register()
    {
        register(TONIC_STAND_ID, TONIC_STAND);
        register(GYPSOPHILA_ID, GYPSOPHILA);
        AppendFuelsConfig.RegisteredFuels.put(GYPSOPHILA_ID, new Pair<>(0, 60));
        register(new Identifier(WintersAppend.MOD_ID, "potted_gypsophila"), GYPSOPHILA_POTTED);
    }

    public static void addTabEntries(ItemGroup.Entries entries) {
        entries.add(TONIC_STAND.getItem());
        entries.add(GYPSOPHILA.getItem());
    }
}
