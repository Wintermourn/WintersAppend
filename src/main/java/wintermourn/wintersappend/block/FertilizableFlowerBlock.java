package wintermourn.wintersappend.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.FlowerBlock;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.util.HashMap;
import java.util.Map;

public class FertilizableFlowerBlock extends FlowerBlock implements Fertilizable, VariablyFertilizable {
    double growChance = 0;
    Map<Item, Double> alternateChances = new HashMap<>();
    public FertilizableFlowerBlock(StatusEffect suspiciousStewEffect, int effectDuration, double growChance, Settings settings) {
        super(suspiciousStewEffect, effectDuration, settings);
        this.growChance = growChance;
    }

    public FertilizableFlowerBlock addAlternateChance(Item item, Double chance)
    {
        alternateChances.put(item, chance);
        return this;
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state, boolean isClient) {
        return world.isSkyVisible(pos);
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return world.isSkyVisible(pos);
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        if (world.random.nextDouble() < this.growChance)
        {
            dropStack(world, pos, new ItemStack(this));
        }
    }

    @Override
    public void growWithItem(ServerWorld world, Random random, BlockPos pos, BlockState state, ItemStack item) {
        double currentChance = this.growChance;

        if (alternateChances.containsKey(item.getItem()))
        {
            currentChance = alternateChances.get(item.getItem());
        }

        if (world.random.nextDouble() < currentChance)
        {
            dropStack(world, pos, new ItemStack(this));
        }
    }
}
