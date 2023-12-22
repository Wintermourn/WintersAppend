package wintermourn.wintersappend.mixin.common;

import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wintermourn.wintersappend.attributes.PercentAttributes;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "createPlayerAttributes", at = @At("RETURN"))
    private static void createAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir)
    {
        cir.getReturnValue().add(PercentAttributes.RESISTANCE_PHYSICAL);
        cir.getReturnValue().add(PercentAttributes.RESISTANCE_PROJECTILE);
    }

}
