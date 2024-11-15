package wintermourn.wintersappend.item;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.StringHelper;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import wintermourn.wintersappend.config.AppendClientConfig;
import wintermourn.wintersappend.config.AppendServerConfig;

import java.util.List;

public class TonicItem extends PotionItem {
    private static final int MAX_USE_TIME = 96;
    public TonicItem(Settings settings) {
        super(settings);
    }

    public int getMaxEffects() { return AppendServerConfig.defaultTonicEffects; }
    public int getMaxUseTime(ItemStack stack)
    {
        return MAX_USE_TIME;
    }
    public int getEffectLifetime() { return AppendServerConfig.defaultTonicLength; }

    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        PlayerEntity playerEntity = user instanceof PlayerEntity ? (PlayerEntity)user : null;
        if (playerEntity instanceof ServerPlayerEntity) {
            Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity)playerEntity, stack);
        }

        if (!world.isClient) {
            List<StatusEffectInstance> list = TonicUtil.getTonicEffectInstances(stack); //PotionUtil.getPotionEffects(stack);
            assert list != null;

            for (StatusEffectInstance statusEffectInstance : list) {
                if (statusEffectInstance.getEffectType().isInstant()) {
                    statusEffectInstance.getEffectType().applyInstantEffect(playerEntity, playerEntity, user, statusEffectInstance.getAmplifier(), 1.0);
                } else {
                    user.addStatusEffect(new StatusEffectInstance(statusEffectInstance));
                }
            }
        }

        if (playerEntity != null) {
            playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
            if (!playerEntity.getAbilities().creativeMode) {
                stack.decrement(1);
            }
        }

        if (playerEntity == null || !playerEntity.getAbilities().creativeMode) {
            if (stack.isEmpty()) {
                return new ItemStack(Items.GLASS_BOTTLE);
            }

            if (playerEntity != null) {
                playerEntity.getInventory().insertStack(new ItemStack(Items.GLASS_BOTTLE));
            }
        }

        user.emitGameEvent(GameEvent.DRINK);
        return stack;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        int spanOffsetter;
        if (stack.hasNbt()) {
            assert stack.getNbt() != null;
            spanOffsetter = (stack.getNbt().contains("spanOffset") ? stack.getNbt().getInt("spanOffset") : 0);
        } else {
            spanOffsetter = 0;
        }
        tooltip.add(Text.translatable("tonic.duration", StringHelper.formatTicks(this.getEffectLifetime() + spanOffsetter)).formatted(Formatting.GRAY));
        TonicUtil.buildTooltip(stack, tooltip, 1.0F);

        if (spanOffsetter != 0 && AppendClientConfig.displayPenalty)
        {
            tooltip.add(Text.translatable("tonic.duration.penalty."+ (spanOffsetter > 0 ? "pos" : "neg"),
                    StringHelper.formatTicks(spanOffsetter)).formatted(Formatting.DARK_GRAY));
        }
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return "tonic.name.none";
    }

    @Override
    public Text getName(ItemStack stack) {
        if (stack.hasCustomName())
            return super.getName(stack);
        return TonicUtil.getName(stack);
    }
}
