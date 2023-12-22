package wintermourn.wintersappend.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

public interface VariablyFertilizable extends Fertilizable {
    void growWithItem(ServerWorld world, Random random, BlockPos pos, BlockState state, ItemStack item);
}
