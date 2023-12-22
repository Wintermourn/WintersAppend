package wintermourn.wintersappend.effects;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DamageResistStatusEffect extends StatusEffect {
    public List<String> immuneSources = new ArrayList<>();
    public double protectionPercentage;
    public double bonusProtectionAmplified = 0;
    public DamageResistStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    public DamageResistStatusEffect addSources(String... immuneSource)
    {
        immuneSources.addAll(Arrays.asList(immuneSource));
        return this;
    }

    public DamageResistStatusEffect setProtection(double percent)
    {
        protectionPercentage = percent;
        return this;
    }

    public DamageResistStatusEffect setProtection(int percent)
    {
        protectionPercentage = percent / 100.0;
        return this;
    }

    public DamageResistStatusEffect setAmplifierProtection(double percent)
    {
        bonusProtectionAmplified = percent;
        return this;
    }

    public DamageResistStatusEffect setAmplifierProtection(int percent)
    {
        bonusProtectionAmplified = percent / 100.0;
        return this;
    }

}
