package wintermourn.wintersappend.mixin.common;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wintermourn.wintersappend.WintersAppend;
import wintermourn.wintersappend.effects.DamageResistStatusEffect;

@Mixin(Entity.class)
public class EntityMixin {
    @SuppressWarnings("UnreachableCode")
    @Inject(method = "isInvulnerableTo", at = @At("RETURN"), cancellable = true)
    private void invulnerabilityCheck(DamageSource source, CallbackInfoReturnable<Boolean> cir)
    {
        if (((Object) this) instanceof LivingEntity entity)
        {
            for (StatusEffectInstance instance : entity.getStatusEffects()) {
                if (instance.getEffectType() instanceof DamageResistStatusEffect effect)
                {
                    if (effect.immuneSources.contains(source.getType().msgId()))
                    {
                        WintersAppend.LOGGER.info(effect.protectionPercentage + (effect.bonusProtectionAmplified * instance.getAmplifier()) +"%");
                        if (effect.protectionPercentage + (effect.bonusProtectionAmplified * instance.getAmplifier()) >= 1) cir.setReturnValue(true);
                    }
                }
            }
        }
    }
}
