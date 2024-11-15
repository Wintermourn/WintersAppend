package wintermourn.wintersappend.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CatalystItem extends Item {
    public CatalystItem(Settings settings) {
        super(settings);
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        if (!stack.hasNbt()) return "winters_append.catalyst_none";
        assert stack.getNbt() != null;

        return "custom.catalyst." +stack.getNbt().getString("id");
    }
}
