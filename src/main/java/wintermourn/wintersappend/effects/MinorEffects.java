package wintermourn.wintersappend.effects;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.HealthBoostStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import wintermourn.wintersappend.WintersAppend;
import wintermourn.wintersappend.attributes.PercentAttributes;
import wintermourn.wintersappend.item.TonicUtil;

import java.awt.*;

public class MinorEffects {

    public static final StatusEffect HEALTH_BOOST;
    public static final StatusEffect ABSORPTION;
    public static final StatusEffect ARMOR;
    public static final StatusEffect REDUCE_ARMOR;
    public static final StatusEffect SPEED;
    public static final StatusEffect SLOWNESS;
    public static final StatusEffect RESISTANCE_PHYSICAL;
    public static final StatusEffect RESISTANCE_PROJECTILE;

    static
    {
        HEALTH_BOOST = new HealthBoostStatusEffect(StatusEffectCategory.BENEFICIAL, new Color(191, 82, 27).getRGB())
                .addAttributeModifier(
                        EntityAttributes.GENERIC_MAX_HEALTH,
                        "c5197c3f-9443-4537-8ffb-a87b0b1a02c3",
                        2.0,
                        EntityAttributeModifier.Operation.ADDITION);
        ABSORPTION = new ModifiableAbsorptionStatusEffect(StatusEffectCategory.BENEFICIAL, 2445989, 2);
        SPEED = new NormalStatusEffect(StatusEffectCategory.BENEFICIAL, new Color(191, 82, 27).getRGB())
                .addAttributeModifier(
                        EntityAttributes.GENERIC_MOVEMENT_SPEED,
                        "65f14d6d-a10b-46a0-889f-796605f7269a",
                        0.05,
                        EntityAttributeModifier.Operation.MULTIPLY_TOTAL);

        RESISTANCE_PHYSICAL = new NormalStatusEffect(StatusEffectCategory.BENEFICIAL, 0xaaaaaa)
                .addAttributeModifier(
                        PercentAttributes.RESISTANCE_PHYSICAL,
                        "729c9176-9e0e-40a8-b8a4-a41af3ca1f72",
                        0.05,
                        EntityAttributeModifier.Operation.MULTIPLY_TOTAL
                );
        RESISTANCE_PROJECTILE = new NormalStatusEffect(StatusEffectCategory.BENEFICIAL, 0xaaaaaa)
                .addAttributeModifier(
                        PercentAttributes.RESISTANCE_PROJECTILE,
                        "ef7b3c66-40b4-42e5-bcb9-e4f8d7f26e86",
                        0.05,
                        EntityAttributeModifier.Operation.MULTIPLY_TOTAL
                );

        ARMOR = new NormalStatusEffect(StatusEffectCategory.BENEFICIAL, 0xaaaaaa)
                .addAttributeModifier(EntityAttributes.GENERIC_ARMOR, "de4e7355-1703-4ec5-98fe-e31c8e05eb4e", 1.0, EntityAttributeModifier.Operation.ADDITION);
        REDUCE_ARMOR = new NormalStatusEffect(StatusEffectCategory.HARMFUL, new Color(191, 82, 27).getRGB())
                .addAttributeModifier(EntityAttributes.GENERIC_ARMOR, "57f5d601-303a-4ae0-8ee9-eaad55443016", -1.0, EntityAttributeModifier.Operation.ADDITION);
        SLOWNESS = new NormalStatusEffect(StatusEffectCategory.HARMFUL, new Color(191, 82, 27).getRGB())
                .addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "0b03bd2d-1a5c-417d-88b8-8d3ea921be16", -0.05, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);

    }

    public static void Register()
    {
        Registry.register(Registries.STATUS_EFFECT, new Identifier(WintersAppend.MOD_ID, "minor/pos/health_boost"), HEALTH_BOOST);
        Registry.register(Registries.STATUS_EFFECT, new Identifier(WintersAppend.MOD_ID, "minor/pos/absorption"), ABSORPTION);
        Registry.register(Registries.STATUS_EFFECT, new Identifier(WintersAppend.MOD_ID, "minor/pos/armor"), ARMOR);
        Registry.register(Registries.STATUS_EFFECT, new Identifier(WintersAppend.MOD_ID, "minor/pos/speed"), SPEED);
        Registry.register(Registries.STATUS_EFFECT, new Identifier(WintersAppend.MOD_ID, "minor/pos/resistance/phys"), RESISTANCE_PHYSICAL);
        Registry.register(Registries.STATUS_EFFECT, new Identifier(WintersAppend.MOD_ID, "minor/pos/resistance/proj"), RESISTANCE_PROJECTILE);

        Registry.register(Registries.STATUS_EFFECT, new Identifier(WintersAppend.MOD_ID, "minor/neg/armor"), REDUCE_ARMOR);
        Registry.register(Registries.STATUS_EFFECT, new Identifier(WintersAppend.MOD_ID, "minor/neg/speed"), SLOWNESS);

        TonicUtil.registerColor(HEALTH_BOOST, 0xE86D13);
        TonicUtil.registerColor(ABSORPTION, 0x357285);
        TonicUtil.registerColor(SPEED, 0x53cBcF);

        TonicUtil.registerText(ABSORPTION, i -> Text.translatable("tonic.effect.absorption", (i+1)));

        TonicUtil.registerName(HEALTH_BOOST, "tonic.name.health_boost");
        TonicUtil.registerName(ABSORPTION, "tonic.name.absorption");
        TonicUtil.registerName(SPEED, "tonic.name.speed");
        TonicUtil.registerName(RESISTANCE_PHYSICAL, "tonic.name.resistance.physical");
        TonicUtil.registerName(RESISTANCE_PROJECTILE, "tonic.name.resistance.projectile");
    }
}
