package wintermourn.wintersappend.effects;

import net.minecraft.entity.effect.StatusEffects;
import wintermourn.wintersappend.item.TonicUtil;

public class VanillaEffects {
    public static void Register()
    {
        TonicUtil.registerName(StatusEffects.NAUSEA, "tonic.name.nausea");
    }

}
