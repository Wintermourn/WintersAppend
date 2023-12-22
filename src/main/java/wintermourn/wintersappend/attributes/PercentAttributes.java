package wintermourn.wintersappend.attributes;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import wintermourn.wintersappend.WintersAppend;

public class PercentAttributes {
    public static final EntityAttribute RESISTANCE_PHYSICAL;
    public static final EntityAttribute RESISTANCE_PROJECTILE;

    static
    {
        RESISTANCE_PHYSICAL = new ClampedEntityAttribute(
                "attribute.name.append.resistance.physical",
                1,
                -2^11,
                2
        ).setTracked(true);
        RESISTANCE_PROJECTILE = new ClampedEntityAttribute(
                "attribute.name.append.resistance.projectile",
                1,
                -2^11,
                2
        ).setTracked(true);
    }

    public static void Register()
    {
        Registry.register(Registries.ATTRIBUTE, new Identifier(WintersAppend.MOD_ID, "resistance_physical"), RESISTANCE_PHYSICAL);
        Registry.register(Registries.ATTRIBUTE, new Identifier(WintersAppend.MOD_ID, "resistance_projectile"), RESISTANCE_PROJECTILE);
    }

}
