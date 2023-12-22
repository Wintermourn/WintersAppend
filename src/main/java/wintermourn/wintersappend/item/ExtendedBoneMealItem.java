package wintermourn.wintersappend.item;

import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wintermourn.wintersappend.block.VariablyFertilizable;

import java.util.ArrayList;
import java.util.List;

public class ExtendedBoneMealItem extends BoneMealItem {
    List<Text> tooltipContents;

    public ExtendedBoneMealItem(Settings settings) {
        super(settings);
        tooltipContents = new ArrayList<>();
    }
    public ExtendedBoneMealItem(Settings settings, List<Text> tooltip) {
        super(settings);
        tooltipContents = List.copyOf(tooltip);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        tooltip.addAll(tooltipContents);
    }

    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockPos blockPos2 = blockPos.offset(context.getSide());
        if (useOnFertilizable(context.getStack(), world, blockPos, context.getStack())) {
            if (!world.isClient) {
                world.syncWorldEvent(1505, blockPos, 0);
            }

            return ActionResult.success(world.isClient);
        } else {
            BlockState blockState = world.getBlockState(blockPos);
            boolean bl = blockState.isSideSolidFullSquare(world, blockPos, context.getSide());
            if (bl && useOnGround(context.getStack(), world, blockPos2, context.getSide())) {
                if (!world.isClient) {
                    world.syncWorldEvent(1505, blockPos2, 0);
                }

                return ActionResult.success(world.isClient);
            } else {
                return ActionResult.PASS;
            }
        }
    }

    public static boolean useOnFertilizable(ItemStack stack, World world, BlockPos pos, ItemStack fertilizer) {
        BlockState blockState = world.getBlockState(pos);
        if (blockState.getBlock() instanceof VariablyFertilizable fertilizable) {
            if (fertilizable.isFertilizable(world, pos, blockState, world.isClient)) {
                if (world instanceof ServerWorld) {
                    if (fertilizable.canGrow(world, world.random, pos, blockState)) {
                        fertilizable.growWithItem((ServerWorld)world, world.random, pos, blockState, fertilizer);
                    }

                    stack.decrement(1);
                }

                return true;
            }
        }

        return false;
    }

}
