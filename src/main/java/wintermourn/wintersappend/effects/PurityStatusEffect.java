package wintermourn.wintersappend.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import wintermourn.wintersappend.WintersAppend;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PurityStatusEffect extends StatusEffect {
    public StatusEffectCategory PurificationCategory;

    protected PurityStatusEffect(StatusEffectCategory category, int color, StatusEffectCategory type) {
        super(category, color);
        PurificationCategory = type;
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        List<StatusEffect> toRemove = new ArrayList<>();
        for (StatusEffectInstance effect : entity.getStatusEffects()) {
            if (effect.getEffectType().getCategory() == PurificationCategory)
            {
                toRemove.add(effect.getEffectType());
                amplifier--;

                if (amplifier < 0)
                {
                    break;
                }
            }
        }

        if (!toRemove.isEmpty())
        {
            for (StatusEffect effect : toRemove) {
                entity.removeStatusEffect(effect);
            }

            if (amplifier >= 0)
            {
                StatusEffectInstance newInstance = new StatusEffectInstance(this, Objects.requireNonNull(entity.getStatusEffect(this)).getDuration(), amplifier);

                WintersAppend.LOGGER.info(amplifier + "");
                entity.removeStatusEffect(this);
                entity.setStatusEffect(newInstance, null);
            } else {
                entity.removeStatusEffect(this);
                entity.setStatusEffect(new StatusEffectInstance(this, 1, 0), null);
            }

        }

    }
}
