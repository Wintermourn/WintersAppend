package wintermourn.wintersappend.client.colors;

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import wintermourn.wintersappend.item.AppendItems;
import wintermourn.wintersappend.item.CatalystUtil;
import wintermourn.wintersappend.item.TonicUtil;

public class ItemColorProviders {
    public static void Register()
    {
        //TONIC
        ColorProviderRegistry.ITEM.register((stack, layer) -> {
            if (layer == 0)
            {
                return TonicUtil.getColor(stack);
            }
            return 0xFFFFFF;
        }, AppendItems.TONIC);
        //CATALYST
        ColorProviderRegistry.ITEM.register((stack, layer) -> {
            if (layer == 0)
            {
                return CatalystUtil.getColor(stack);
            }
            return 0xFFFFFF;
        }, AppendItems.CATALYST);
    }
}
