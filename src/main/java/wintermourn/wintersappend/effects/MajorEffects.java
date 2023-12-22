package wintermourn.wintersappend.effects;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class MajorEffects {
    public static final StatusEffect FORTUNE;
    public static final StatusEffect MINERS_DELIGHT;

    static
    {
        FORTUNE = new NormalStatusEffect(StatusEffectCategory.BENEFICIAL, 0xaaffaa);
        MINERS_DELIGHT = new NormalStatusEffect(StatusEffectCategory.BENEFICIAL, 0xaaffaa);
    }
}
