package wintermourn.wintersappend.mixin.common;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wintermourn.wintersappend.effects.TemporaryEnchantEffect;

import java.util.List;
import java.util.Map;

@Mixin(Block.class)
public class BlockMixin {
    @Inject(method = "getDroppedStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)Ljava/util/List;", at = @At("HEAD"), cancellable = true)
    private static void getDroppedStacks(BlockState state, ServerWorld world, BlockPos pos, BlockEntity blockEntity, Entity entity, ItemStack stack, CallbackInfoReturnable<List<ItemStack>> cir)
    {
        if (!(entity instanceof LivingEntity living)) return;

        Map<Enchantment, Integer> enchantsOriginal = EnchantmentHelper.get(stack);
        Map<Enchantment, Integer> enchants = EnchantmentHelper.get(stack);

        for (StatusEffectInstance instance : living.getStatusEffects())
        {
            if (!(instance.getEffectType() instanceof TemporaryEnchantEffect effect)) continue;

            if (effect.shouldEnchantActivate(instance.getAmplifier()))
            {
                Enchantment enchant = effect.getEnchantment();

                if (enchants.containsKey(enchant)) enchants.put(enchant, enchants.get(enchant) + 1);
                else enchants.put(enchant, 1);
            }
        }

        if (enchants.equals(enchantsOriginal)) return;

        EnchantmentHelper.set(enchants, stack);

        cir.setReturnValue(
                state.getDroppedStacks(
                        (new LootContextParameterSet.Builder(world))
                                .add(LootContextParameters.ORIGIN, Vec3d.ofCenter(pos))
                                .add(LootContextParameters.TOOL, stack)
                                .addOptional(LootContextParameters.THIS_ENTITY, entity)
                                .addOptional(LootContextParameters.BLOCK_ENTITY, blockEntity)
                )
        );

        EnchantmentHelper.set(enchantsOriginal, stack);

    }
}
