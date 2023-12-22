package wintermourn.wintersappend.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wintermourn.wintersappend.block.entity.AppendBlockEntities;
import wintermourn.wintersappend.block.entity.TonicStandBlockEntity;

public class TonicStandBlock extends BlockWithEntity implements BlockEntityProvider {

//    public static BooleanProperty HAS_BOTTLE = BooleanProperty.of("has_bottle");
    static final VoxelShape SHAPE = Block.createCuboidShape(2, 0, 2, 14, 7, 14);

    float fuelEfficiency = 1;
    float purityEfficiency = 1;
    float timeEfficiency = 1;


    protected TonicStandBlock(Settings settings) {
        super(settings);
//        setDefaultState(
//                getDefaultState()
//                        .with(HAS_BOTTLE, false)
//        );
    }

    public static TonicStandBlock withEfficiency(Settings settings, float fuel, float purity, float time)
    {
        TonicStandBlock block = new TonicStandBlock(settings);
        block.fuelEfficiency = fuel;
        block.purityEfficiency = purity;
        block.timeEfficiency = time;

        return block;
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
//        builder.add(HAS_BOTTLE);

    }
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return TonicStandBlockEntity.withEfficiency(pos, state, this.fuelEfficiency, this.purityEfficiency, this.timeEfficiency);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock())
        {
            BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof TonicStandBlockEntity)
            {
                ItemScatterer.spawn(world, pos, (TonicStandBlockEntity)entity);
                world.updateComparators(pos, this);
            }

            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient())
        {
            TonicStandBlockEntity entity = ((TonicStandBlockEntity) world.getBlockEntity(pos));

            if (entity != null)
            {
                player.openHandledScreen(entity);
            }
        }

        return ActionResult.SUCCESS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, AppendBlockEntities.TONIC_STAND_BLOCK_ENTITY, TonicStandBlockEntity::tick);
    }
}
