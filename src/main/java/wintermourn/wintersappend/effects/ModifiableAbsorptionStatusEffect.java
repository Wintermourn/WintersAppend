package wintermourn.wintersappend.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class ModifiableAbsorptionStatusEffect extends StatusEffect {
    int powerLevel;
    public ModifiableAbsorptionStatusEffect(StatusEffectCategory statusEffectCategory, int i, int power) {
        super(statusEffectCategory, i);
        powerLevel = power;
    }

    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        entity.setAbsorptionAmount(entity.getAbsorptionAmount() - (float)(powerLevel * (amplifier + 1)));
        super.onRemoved(entity, attributes, amplifier);
    }

    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        entity.setAbsorptionAmount(entity.getAbsorptionAmount() + (float)(powerLevel * (amplifier + 1)));
        super.onApplied(entity, attributes, amplifier);
    }
}
