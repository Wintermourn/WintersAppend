package wintermourn.wintersappend.effects;

import com.demonwav.mcdev.annotations.Translatable;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import wintermourn.wintersappend.WintersAppend;
import wintermourn.wintersappend.item.TonicUtil;

public class MajorEffects {
    public static final StatusEffect CONCEALED;
    public static final TemporaryEnchantEffect SILK_TOUCH;
    public static final TemporaryEnchantEffect FORTUNE;

    static
    {
        CONCEALED = new NormalStatusEffect(StatusEffectCategory.BENEFICIAL, 0x999999).addAttributeModifier(
                EntityAttributes.GENERIC_FOLLOW_RANGE,
                "dc09df33-323b-48bc-b2b0-936d8ffe7e8d",
                -10,
                EntityAttributeModifier.Operation.ADDITION
        );
        SILK_TOUCH = new TemporaryEnchantEffect(StatusEffectCategory.BENEFICIAL, 0x99FFDD);
        SILK_TOUCH.set(Enchantments.SILK_TOUCH, i -> i > 0 || WintersAppend.RANDOM.nextFloat() <= .8f);

        FORTUNE = new TemporaryEnchantEffect(StatusEffectCategory.BENEFICIAL, 0x88ff88);
        FORTUNE.set(Enchantments.FORTUNE, i -> WintersAppend.RANDOM.nextFloat() <= 1 / (1 + Math.exp(-1 * (i+1))));
    }

    public static void Register()
    {
        Registry.register(Registries.STATUS_EFFECT, new Identifier(WintersAppend.MOD_ID, "major/concealed"), CONCEALED);
        TonicUtil.registerColor(CONCEALED, 0x999999);
        TonicUtil.registerText(CONCEALED, i -> Text.translatable("tonic.effect.concealed"));
        TonicUtil.registerName(CONCEALED, "tonic.name.concealed");

        Registry.register(Registries.STATUS_EFFECT, new Identifier(WintersAppend.MOD_ID, "major/silk_touch"), SILK_TOUCH);
        TonicUtil.registerColor(SILK_TOUCH, 0x99FFDD);
        TonicUtil.registerText(SILK_TOUCH, i -> VariableDescription( "tonic.effect.silk_touch", i ));
        TonicUtil.registerName(SILK_TOUCH, "tonic.name.silk_touch");

        Registry.register(Registries.STATUS_EFFECT, new Identifier(WintersAppend.MOD_ID, "major/fortune"), FORTUNE);
        TonicUtil.registerColor(FORTUNE, 0x88ff88);
        TonicUtil.registerText(FORTUNE, i -> Text.translatable( "tonic.effect.fortune", Math.round((1 / (1 + Math.exp(-1 * (i+1)))) * 1000) /10f ));
        TonicUtil.registerName(FORTUNE, "tonic.name.fortune");
    }

    static Text VariableDescription(String key, int value)
    {
        return Text.translatable(key + "." + value);
    }

    static Text VariableText(@Translatable String key, double value)
    {
        return Text.translatable(key, value);
    }
}
