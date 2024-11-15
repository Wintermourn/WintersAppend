package wintermourn.wintersappend.block.entity;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wintermourn.wintersappend.config.AppendFuelsConfig;
import wintermourn.wintersappend.item.TonicUtil;
import wintermourn.wintersappend.networking.AppendMessages;
import wintermourn.wintersappend.recipe.CatalystRecipe;
import wintermourn.wintersappend.recipe.TonicBrewingRecipe;
import wintermourn.wintersappend.recipe.TonicStandRecipe;
import wintermourn.wintersappend.screen.TonicStandScreenHandler;
import wintermourn.wintersappend.config.AppendServerConfig;

import java.util.Optional;

public class TonicStandBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, SidedInventory {
    public static final int MAX_FUEL = 100;
    public static final int MAX_PURITY = 120;
    public static final int OUTPUT_SLOT_ID = 0;
    public static final int FUEL_SLOT_ID = 4;
    public static final int PURITY_SLOT_ID = 5;

    float fuelEfficiency = 1;
    float purityEfficiency = 1;
    float timeEfficiency = 1;

    DefaultedList<ItemStack> inventory;
    PropertyDelegate propertyDelegate;

    boolean amIDirty = false;
    int brewTime = 0;
    int fuel = 0;
    int purity = 0;
    int fuelPartialTicks = 0;
    int purityPartialTicks = 0;

    int brewingMode = 0;
    // 0 = tonic, 1 = catalyst

    TonicStandRecipe recipe;
    public TonicStandBlockEntity(BlockPos pos, BlockState state) {
        super(AppendBlockEntities.TONIC_STAND_BLOCK_ENTITY, pos, state);
        this.inventory = DefaultedList.ofSize(6, ItemStack.EMPTY);
        this.propertyDelegate = new PropertyDelegate() {
            public int get(int index) {
                return switch (index) {
                    case 0 -> TonicStandBlockEntity.this.brewTime;
                    case 1 -> TonicStandBlockEntity.this.fuel;
                    case 2 -> TonicStandBlockEntity.this.purity;
                    case 3 -> TonicStandBlockEntity.this.getFuelEstimate();
                    case 4 -> TonicStandBlockEntity.this.getMaxBrewTime();
                    case 5 -> TonicStandBlockEntity.this.getPurityEstimate();
                    case 6 -> TonicStandBlockEntity.this.brewingMode;
                    default -> 0;
                };
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0 -> TonicStandBlockEntity.this.brewTime = value;
                    case 1 -> TonicStandBlockEntity.this.fuel = value;
                    case 2 -> TonicStandBlockEntity.this.purity = value;
                    case 6 -> TonicStandBlockEntity.this.brewingMode = value;
                }

            }

            public int size() {
                return 7;
            }
        };
    }

    public static TonicStandBlockEntity withEfficiency(BlockPos pos, BlockState state, float fuel, float purity, float time)
    {
        TonicStandBlockEntity entity = new TonicStandBlockEntity(pos, state);
        entity.fuelEfficiency = fuel;
        entity.purityEfficiency = purity;
        entity.timeEfficiency = time;

        return entity;
    }

    public void setEfficiency(float fuel, float purity, float time)
    {
        this.fuelEfficiency = fuel;
        this.purityEfficiency = purity;
        this.timeEfficiency = time;
    }

    public float getPurityEfficiency() {
        return 1;
    }

    public int getMaxPurity() {
        return TonicStandBlockEntity.MAX_PURITY;
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        return new int[0];
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return true;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return true;
    }

    public int size() {
        return this.inventory.size();
    }

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public ItemStack getStack(int slot) {
        return inventory.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack result = Inventories.splitStack(inventory, slot, amount);
        if (!result.isEmpty())
        {
            amIDirty = true;
            markDirty();
        }
        return result;
    }

    @Override
    public ItemStack removeStack(int slot) {
        ItemStack slotItem = inventory.get(slot);
        inventory.set(slot, ItemStack.EMPTY);
        amIDirty = true;
        markDirty();
        return slotItem;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        inventory.set(slot, stack);
        if (stack.getCount() > getMaxCountPerStack()) {
            stack.setCount(getMaxCountPerStack());
        }
        amIDirty = true;
        markDirty();
//        if (inventory.get(slot).isEmpty())
//        {
//            inventory.set(slot, stack);
//        }
    }

    public boolean canPlayerUse(PlayerEntity player) {
        return Inventory.canPlayerUse(this, player);
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new TonicStandScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new TonicStandScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    public static void tick(World world, BlockPos pos, BlockState state, TonicStandBlockEntity me)
    {
        if (world.isClient()) return;

        if (me.isOutputValid())
        {
            if (me.hasRecipe())
            {
                me.increaseCraftProgress();
                markDirty(world, pos, state);

                if (me.isCraftingFinished())
                {
                    me.craftItem();
                    me.resetProgress();
                    markDirty(world, pos, state);
                }
            } else
            {
                if (me.decreaseCraftProgress())
                    me.amIDirty = true;
            }
        } else
        {
            if (me.decreaseCraftProgress())
                me.amIDirty = true;
        }

        if (me.amIDirty)
            markDirty(world, pos, state);

        ItemStack fuelStack = me.inventory.get(TonicStandBlockEntity.FUEL_SLOT_ID);
        if (!fuelStack.isEmpty())
        {
            int value = AppendFuelsConfig.GetHeatFuel(fuelStack);
            if (value > -1 && me.fuel <= me.getMaxFuel() - value)
            {
                me.fuel += value;
                fuelStack.decrement(1);
            }
        }

        ItemStack purityStack = me.inventory.get(TonicStandBlockEntity.PURITY_SLOT_ID);
        if (!purityStack.isEmpty())
        {
            int value = AppendFuelsConfig.GetPurity(purityStack);
            if (value > -1 && me.purity <= me.getMaxPurity() - value)
            {
                me.purity += value;
                purityStack.decrement(1);
            }
        }
    }

    public static void markDirty(World world, BlockPos pos, BlockState myState) {
        if (world.getBlockEntity(pos) instanceof TonicStandBlockEntity me)
        {
            me.markDirty();
//            if (me.inventory.get(OUTPUT_SLOT_ID).isEmpty() && myState.get(TonicStandBlock.HAS_BOTTLE))
//            {
//                world.setBlockState(pos, myState.with(TonicStandBlock.HAS_BOTTLE, false));
//            } else if (!me.inventory.get(OUTPUT_SLOT_ID).isEmpty() && !myState.get(TonicStandBlock.HAS_BOTTLE))
//            {
//                world.setBlockState(pos, myState.with(TonicStandBlock.HAS_BOTTLE, true));
//            }
        }
    }


    private boolean decreaseCraftProgress() {
        if (brewTime > 1)
        {
            brewTime -= 2;
            return true;
        } else if (brewTime > 0)
        {
            brewTime--;
            return true;
        }
        return false;
    }

    private void resetProgress() {
        brewTime = 0;
    }

    private void craftItem() {
        ItemStack outputStack = inventory.get(TonicStandBlockEntity.OUTPUT_SLOT_ID);

        inventory.set(TonicStandBlockEntity.OUTPUT_SLOT_ID, this.recipe.craft(this, null));

        inventory.get(1).decrement(1);
        inventory.get(2).decrement(1);
        inventory.get(3).decrement(1);

        if (!AppendServerConfig.spendFuelDuringBrew)
        {
            boolean hasPurity = this.getPurity() > this.recipe.purityCost / this.getPurityEfficiency();
            fuel -= (int) (Math.round(this.recipe.fuelCost / this.fuelEfficiency) * (hasPurity ? 1 : AppendServerConfig.impureFuelPenalty));
            purity -= Math.round(this.recipe.purityCost / this.getPurityEfficiency());
            if (fuel < 0) fuel = 0;
            if (purity < 0) purity = 0;
        }

        if (this.world != null && !this.world.isClient)
        {
            for (PlayerEntity player : this.world.getPlayers()) {
                ServerPlayNetworking.send(
                        (ServerPlayerEntity) player,
                        AppendMessages.BREWING_FINISHED,
                        PacketByteBufs.create().writeBlockPos(this.pos).writeItemStack(inventory.get(OUTPUT_SLOT_ID)));
            }
        }
    }

    private boolean isCraftingFinished() {
        return (hasRecipe() && getMaxBrewTime() <= this.brewTime);
    }

    private void increaseCraftProgress() {
        brewTime++;

        if (AppendServerConfig.spendFuelDuringBrew)
        {
            float fuelPrice = (recipe.fuelCost / (recipe.brewingTime * this.timeEfficiency * ((purity > 0) ? 1 : AppendServerConfig.impureTimePenalty)))
                    * this.fuelEfficiency * (fuelPartialTicks + 1)
                    * ((purity > 0) ? 1 : AppendServerConfig.impureFuelPenalty);
            float purityPrice = (recipe.purityCost / (recipe.brewingTime * this.timeEfficiency * ((purity > 0) ? 1 : AppendServerConfig.impureTimePenalty)))
                    * this.purityEfficiency * (purityPartialTicks + 1);
            if (fuelPrice < 1) fuelPartialTicks++; else
            {
                fuel -= (int) fuelPrice;
                fuelPartialTicks = 0;
            }
            if (purityPrice < 1) purityPartialTicks++; else
            {
                purity -= (int) purityPrice;
                purityPartialTicks = 0;
            }
        }
    }

    boolean hasRecipe() {
        if (getWorld() == null) return false;
        Optional<?> recipe = switch (brewingMode) {
            case 0 -> getWorld().getRecipeManager().getFirstMatch(TonicBrewingRecipe.Type.INSTANCE, this, world);
            case 1 -> getWorld().getRecipeManager().getFirstMatch(CatalystRecipe.Type.INSTANCE, this, world);
            default -> Optional.empty();
        };

        this.recipe = (TonicStandRecipe) recipe.orElse(null);

        return this.recipe != null;
    }

    boolean isOutputValid()
    {
        ItemStack stack = inventory.get(0);
        return recipe != null && recipe.isOutputValid(stack);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        nbt.putInt("fuel",fuel);
        if (fuelPartialTicks > 0) nbt.putInt("fuelP",fuelPartialTicks);
        nbt.putInt("brewTime",brewTime);
        nbt.putInt("purity",purity);
        if (purityPartialTicks > 0) nbt.putInt("purityP",purityPartialTicks);
        nbt.putInt("brewingMode", brewingMode);

        NbtList stacks = new NbtList();

        for (ItemStack stack : inventory) {
            NbtCompound item = new NbtCompound();
            stack.writeNbt(item);
            stacks.add(item);
        }

        nbt.put("contents", stacks);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        fuel = nbt.getInt("fuel");
        fuelPartialTicks = nbt.getInt("fuelP");
        brewTime = nbt.getInt("brewTime");
        purity = nbt.getInt("purity");
        purityPartialTicks = nbt.getInt("purityP");
        brewingMode = nbt.getInt("brewingMode");

        NbtList stacks = nbt.getList("contents", NbtElement.COMPOUND_TYPE);

        for (int i = 0; i < stacks.size(); i++) {
            if (stacks.get(i) instanceof NbtCompound compound)
            {
                ItemStack stack = ItemStack.fromNbt(compound);
                inventory.set(i, stack != null ? stack : ItemStack.EMPTY);
            }
        }
    }

    public int getFuel() {
        return this.fuel;
    }
    public int getMaxFuel() {
        return TonicStandBlockEntity.MAX_FUEL;
    }
    public int getBrewTime() {
        return this.brewTime;
    }
    public int getMaxBrewTime() {
        if (this.hasRecipe())
        {
            boolean hasPurity = this.getPurity() > this.recipe.purityCost / this.getPurityEfficiency();
            return Math.round(this.recipe.brewingTime * (hasPurity ? 1 : AppendServerConfig.impureTimePenalty));
        } else
        {
            return 9999;
        }
    }
    public float getBrewPercent() { return getBrewTime()/(float)getMaxBrewTime(); }

    @Nullable
    public Integer getFluidColor()
    {
        if (inventory.get(OUTPUT_SLOT_ID).isEmpty()) return null;
        return TonicUtil.getColor(inventory.get(OUTPUT_SLOT_ID));
    }

    public int[] getFluidColorRGB()
    {
        if (inventory.get(OUTPUT_SLOT_ID).isEmpty()) return new int[]{0,0,0};
        return TonicUtil.getColorRGB(inventory.get(OUTPUT_SLOT_ID));
    }

    public void setOutputStack(ItemStack stack)
    {
        inventory.set(OUTPUT_SLOT_ID, stack);
    }

    public int getBrewingMode()
    {
        return brewingMode;
    }

    public void setBrewingMode(int mode)
    {
        this.brewingMode = mode;
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public int getFuelEstimate() {
        if (this.hasRecipe())
        {
            boolean hasPurity = this.getPurity() > this.recipe.purityCost / this.getPurityEfficiency();

            if (AppendServerConfig.spendFuelDuringBrew)
                return this.fuel - (int)Math.ceil(this.recipe.fuelCost / this.fuelEfficiency * (hasPurity ? 1 : AppendServerConfig.impureFuelPenalty) * (1-getBrewPercent()));
            else
                return this.fuel - Math.round(this.recipe.fuelCost / this.fuelEfficiency * (hasPurity ? 1 : AppendServerConfig.impureFuelPenalty));
        } else return this.getFuel();
    }

    private int getPurityEstimate() {
        if (this.hasRecipe())
        {
            if (AppendServerConfig.spendFuelDuringBrew)
                return this.purity - (int)Math.ceil(this.recipe.purityCost / this.getPurityEfficiency() * (1-getBrewPercent()));
            else
                return this.purity - Math.round(this.recipe.purityCost / this.getPurityEfficiency());
        } else return this.getPurity();
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.winters_append.tonic_stand");
    }

    public int getPurity() {
        return purity;
    }

    public boolean hasOutput() {
        return !this.inventory.get(OUTPUT_SLOT_ID).isEmpty();
    }
}
