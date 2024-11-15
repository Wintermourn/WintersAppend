package wintermourn.wintersappend.mixin.common;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobVisibilityCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wintermourn.wintersappend.effects.MajorEffects;

@Mixin(MobVisibilityCache.class)
public class MobVisibilityCacheMixin {
    @Inject(method = "canSee", at = @At("RETURN"), cancellable = true)
    void visibilityTest(Entity entity, CallbackInfoReturnable<Boolean> cir)
    {
        if (!(entity instanceof LivingEntity living)) return;

        if (living.hasStatusEffect(MajorEffects.CONCEALED)) cir.setReturnValue(false);
    }
}
