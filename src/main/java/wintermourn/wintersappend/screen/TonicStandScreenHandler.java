package wintermourn.wintersappend.screen;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import wintermourn.wintersappend.block.entity.TonicStandBlockEntity;
import wintermourn.wintersappend.config.AppendFuelsConfig;
import wintermourn.wintersappend.item.TonicItem;
import wintermourn.wintersappend.item.TonicUtil;

public class TonicStandScreenHandler extends ScreenHandler {

    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    public final TonicStandBlockEntity blockEntity;

    private final Slot ingredientSlot0;
    private final Slot ingredientSlot1;
    private final Slot ingredientSlot2;
    private final Slot fuelSlot;
    private final Slot puritySlot;
    //private final Slot chargeSlot;

    public TonicStandScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, playerInventory.player.getWorld().getBlockEntity(buf.readBlockPos()), new ArrayPropertyDelegate(7));
    }

    public TonicStandScreenHandler(int syncId, PlayerInventory playerInventory, BlockEntity blockEntity,
                                   PropertyDelegate delegate) {
        super(AppendScreenHandlers.TONIC_STAND_SCREEN_HANDLER, syncId);

        this.blockEntity = (TonicStandBlockEntity) blockEntity;

        checkSize((Inventory) blockEntity, 6);
        checkDataCount(delegate, 7);
        this.inventory = (Inventory) blockEntity;
        inventory.onOpen(playerInventory.player);
        propertyDelegate = delegate;

        addSlot(new ResultSlot(inventory, 0, 80, 58));

        ingredientSlot0 = this.addSlot(new IngredientSlot(inventory, 1, 80, 17));
        ingredientSlot1 = this.addSlot(new IngredientSlot(inventory, 2, 60, 58));
        ingredientSlot2 = this.addSlot(new IngredientSlot(inventory, 3, 100, 58));

        fuelSlot = this.addSlot(new FuelSlot(inventory, 4, 18, 17));

        puritySlot = this.addSlot(new PuritySlot(inventory, 5, 18, 39));

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        addProperties(propertyDelegate);
    }

    private void addPlayerInventory(PlayerInventory playerInventory)
    {
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory)
    {
        for(int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slotId) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = this.slots.get(slotId);
        if (slot2 != null && slot2.hasStack()) {
            ItemStack itemStack2 = slot2.getStack();
            itemStack = itemStack2.copy();
            if (slotId < inventory.size()) {
                if (!this.insertItem(itemStack2, inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(itemStack2, 0, inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot2.setStack(ItemStack.EMPTY);
            } else {
                slot2.markDirty();
            }
        }

        return itemStack;
    }

    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    public boolean isCrafting()
    {
        return propertyDelegate.get(0) > 0;
    }

    public float getScaledProgress()
    {
        return propertyDelegate.get(0) / ((float) propertyDelegate.get(4));
    }

    public float getScaledFuel()
    {
        return propertyDelegate.get(1) / (float) blockEntity.getMaxFuel();
    }
    public float getScaledFuelEstimate()
    {
        return propertyDelegate.get(3) / (float) blockEntity.getMaxFuel();
    }
    public float getScaledPurity()
    {
        return propertyDelegate.get(2) / (float) blockEntity.getMaxPurity();
    }
    public int getBrewingMode() {
        return propertyDelegate.get(6);
    }
    public void setBrewingMode(int mode) {
        propertyDelegate.set(6, mode);
    }

    public float getScaledPurityEstimate() {
        return propertyDelegate.get(5) / (float) blockEntity.getMaxPurity();
    }

    private class IngredientSlot extends Slot
    {
        public IngredientSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }
    }
    private class FuelSlot extends Slot
    {
        public FuelSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return AppendFuelsConfig.GetHeatFuel(stack) > -1;
        }
    }
    private class PuritySlot extends Slot
    {
        public PuritySlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return AppendFuelsConfig.GetPurity(stack) > -1;
        }
    }
    private class ResultSlot extends Slot
    {
        public ResultSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return (stack.getItem() instanceof TonicItem && TonicUtil.getEffectsCount(stack) < ((TonicItem) stack.getItem()).getMaxEffects())
                    || PotionUtil.getPotion(stack) == Potions.WATER;
        }

        @Override
        public int getMaxItemCount() {
            return 1;
        }

        @Override
        public int getMaxItemCount(ItemStack stack) {
            return 1;
        }
    }
}
