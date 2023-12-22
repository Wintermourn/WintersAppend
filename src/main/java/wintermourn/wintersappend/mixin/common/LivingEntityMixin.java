package wintermourn.wintersappend.mixin.common;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.ProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wintermourn.wintersappend.WintersAppend;
import wintermourn.wintersappend.attributes.PercentAttributes;
import wintermourn.wintersappend.effects.DamageResistStatusEffect;
import wintermourn.wintersappend.effects.ImmunizationStatusEffect;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "modifyAppliedDamage", at = @At(value = "RETURN"), cancellable = true)
    private void OnHurt(DamageSource source, float amount, CallbackInfoReturnable<Float> cir)
    {
        LivingEntity me = (LivingEntity) ((Object) this);

        for (StatusEffectInstance instance : me.getStatusEffects()) {
            if (instance.getEffectType() instanceof DamageResistStatusEffect effect)
            {
                if (effect.immuneSources.contains(source.getType().msgId()))
                {
                    amount *= (float) (1.0 - effect.protectionPercentage - effect.bonusProtectionAmplified * instance.getAmplifier());
                }
            }
        }

        if (source.getSource() != null)
        {
            WintersAppend.LOGGER.info(source.getSource().toString());
            if (source.getSource() instanceof ProjectileEntity)
            {
                if (me.getAttributes().hasAttribute(PercentAttributes.RESISTANCE_PROJECTILE))
                {
                    amount *= 2 - (float) me.getAttributeValue(PercentAttributes.RESISTANCE_PROJECTILE);
                }
            } else if (source.getSource() instanceof LivingEntity)
            {
                if (me.getAttributes().hasAttribute(PercentAttributes.RESISTANCE_PHYSICAL))
                {
                    amount *= 2 - (float) me.getAttributeValue(PercentAttributes.RESISTANCE_PHYSICAL);
                }
            }
        }

        cir.setReturnValue(amount);
    }

//    @Inject(method = "addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z", at = @At("HEAD"), cancellable = true)
//    private void OnAddEffect(StatusEffectInstance effect, Entity source, CallbackInfoReturnable<Boolean> cir)
//    {
//        LivingEntity me = (LivingEntity) ((Object) this);
//
//        for (StatusEffectInstance instance : me.getStatusEffects()) {
//            if (instance.getEffectType() instanceof ImmunizationStatusEffect immunization)
//            {
//                if (immunization.effects.contains(effect.getEffectType()))
//                {
//                    cir.setReturnValue(false);
//                }
//            }
//        }
//    }

    @Inject(method = "canHaveStatusEffect", at = @At("RETURN"), cancellable = true)
    private void CanHaveEffect(StatusEffectInstance effect, CallbackInfoReturnable<Boolean> cir)
    {
        LivingEntity me = (LivingEntity) ((Object) this);

        for (StatusEffectInstance instance : me.getStatusEffects()) {
            if (instance.getEffectType() instanceof ImmunizationStatusEffect immunization)
            {
                if (immunization.effects.contains(effect.getEffectType()))
                {
                    cir.setReturnValue(false);
                }
            }
        }
    }
}
