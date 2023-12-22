package wintermourn.wintersappend.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImmunizationStatusEffect extends StatusEffect {
    public List<StatusEffect> effects = new ArrayList<>();
    public ImmunizationStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    public ImmunizationStatusEffect addSources(StatusEffect... immuneSource)
    {
        effects.addAll(Arrays.asList(immuneSource));
        return this;
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onApplied(entity, attributes, amplifier);

        for (StatusEffect effect : effects) {
            entity.removeStatusEffect(effect);
        }
    }
}
