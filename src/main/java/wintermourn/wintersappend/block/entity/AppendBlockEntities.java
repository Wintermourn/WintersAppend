package wintermourn.wintersappend.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import wintermourn.wintersappend.WintersAppend;
import wintermourn.wintersappend.block.AppendBlocks;

public class AppendBlockEntities {
    public static final BlockEntityType<TonicStandBlockEntity> TONIC_STAND_BLOCK_ENTITY;

    static
    {
        TONIC_STAND_BLOCK_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier(WintersAppend.MOD_ID, "tonic_stand"),
                FabricBlockEntityTypeBuilder.create(TonicStandBlockEntity::new, AppendBlocks.TONIC_STAND.getBlock()).build());
    }

    public static void Register()
    {

    }

}
