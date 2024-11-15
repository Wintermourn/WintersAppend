package wintermourn.wintersappend.effects;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

import java.util.function.Function;

public class TemporaryEnchantEffect extends StatusEffect {
    Enchantment linkedEnchantment;
    Function<Integer, Boolean> formula;

    protected TemporaryEnchantEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    public void set(Enchantment enchantment, Function<Integer, Boolean> func)
    {
        linkedEnchantment = enchantment;
        formula = func;
    }

    public boolean shouldEnchantActivate(int level)
    {
        return formula.apply(level);
    }

    public Enchantment getEnchantment() { return linkedEnchantment; }

}
