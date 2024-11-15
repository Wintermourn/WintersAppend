package wintermourn.wintersappend.integration.emi;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import wintermourn.wintersappend.integration.emi.category.EmiCatalystCategory;
import wintermourn.wintersappend.integration.emi.category.EmiFuelCategory;
import wintermourn.wintersappend.integration.emi.category.EmiTonicCategory;

public class AppendEmi implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {
        EmiTonicCategory.Register(registry);
        EmiCatalystCategory.Register(registry);
        EmiFuelCategory.Register(registry);
    }
}
