package wintermourn.wintersappend.effects;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import wintermourn.wintersappend.WintersAppend;
import wintermourn.wintersappend.enums.DamageTypes;
import wintermourn.wintersappend.item.TonicUtil;

public class ImmunizationEffects {
    public static final StatusEffect ANTIVENOM;
    public static final StatusEffect DETHORNING;
    public static final StatusEffect KINETIC_RESIST;

    static
    {
        ANTIVENOM = new ImmunizationStatusEffect(StatusEffectCategory.BENEFICIAL, 0xaaffaa)
                .addSources(StatusEffects.POISON).addAttributeModifier(
                        EntityAttributes.GENERIC_ATTACK_DAMAGE,
                        "5bcad147-0abc-4101-a93e-3becacfedf9e",
                        -0.1,
                        EntityAttributeModifier.Operation.MULTIPLY_TOTAL
                );

        DETHORNING = new DamageResistStatusEffect(StatusEffectCategory.BENEFICIAL, 0xaaffaa)
                .addSources(DamageTypes.THORNS.toString(), DamageTypes.SWEET_BERRY.toString())
                .setProtection(0.5).setAmplifierProtection(0.5).addAttributeModifier(
                        EntityAttributes.GENERIC_ATTACK_SPEED,
                        "c84dc22b-b8ef-4e61-8829-961988cd9151",
                        -0.05,
                        EntityAttributeModifier.Operation.MULTIPLY_TOTAL
                ).addAttributeModifier(
                        EntityAttributes.GENERIC_MOVEMENT_SPEED,
                        "cca22cf4-2eb7-454b-941a-71e6b43f0d87",
                        -0.05,
                        EntityAttributeModifier.Operation.MULTIPLY_TOTAL
                );

        KINETIC_RESIST = new DamageResistStatusEffect(StatusEffectCategory.BENEFICIAL, 0xaaffaa)
                .addSources(DamageTypes.ELYTRA.toString(), DamageTypes.FALLING.toString(), DamageTypes.CACTUS.toString())
                .setProtection(0.5).setAmplifierProtection(0.1).addAttributeModifier(
                        EntityAttributes.GENERIC_ARMOR,
                        "adf62d26-0cb1-4421-935a-af3d478c2ffb",
                        -0.10,
                        EntityAttributeModifier.Operation.MULTIPLY_TOTAL
                );

    }

    public static void Register()
    {
        Registry.register(Registries.STATUS_EFFECT, new Identifier(WintersAppend.MOD_ID, "immune/antivenom"), ANTIVENOM);
        Registry.register(Registries.STATUS_EFFECT, new Identifier(WintersAppend.MOD_ID, "immune/thorns"), DETHORNING);
        Registry.register(Registries.STATUS_EFFECT, new Identifier(WintersAppend.MOD_ID, "immune/kinetic"), KINETIC_RESIST);

        TonicUtil.registerColor(ANTIVENOM, 0x38A8D8);
        TonicUtil.registerColor(DETHORNING, 0xF8A828);

        TonicUtil.registerText(ANTIVENOM, i -> Text.translatable( "tonic.effect.antivenom" ));
        TonicUtil.registerText(DETHORNING, i -> ResistText( "tonic.effect.dethorn", (1-i) * 50 ));
        TonicUtil.registerText(KINETIC_RESIST, i -> ResistText( "tonic.effect.kinetic", (5-i) * 10 ));

        TonicUtil.registerName(ANTIVENOM, "tonic.name.antivenom");
        TonicUtil.registerName(DETHORNING, "tonic.name.dethorn");
        TonicUtil.registerName(KINETIC_RESIST, "tonic.name.kinetic");
    }

    static Text ResistText(String key, double value)
    {
        if (value > 0)
            return Text.translatable(key, value);
        else
            return Text.translatable(key+".full");
    }

}
