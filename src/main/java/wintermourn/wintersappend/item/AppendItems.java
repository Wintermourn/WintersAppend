package wintermourn.wintersappend.item;

import net.minecraft.item.BoneMealItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import wintermourn.wintersappend.WintersAppend;

import java.util.List;

public class AppendItems {
    public static final Item TONIC;
    public static final BoneMealItem SOUL_MEAL;

    static
    {
        TONIC = new TonicItem(new Item.Settings().maxCount(3));
        SOUL_MEAL = new ExtendedBoneMealItem(new Item.Settings(), List.of(
                Text.translatable("tooltip.winters_append.soul_meal.0"),
                Text.translatable("tooltip.winters_append.soul_meal.1")
        ));
    }

    public static void Register()
    {
        Registry.register(Registries.ITEM, new Identifier(WintersAppend.MOD_ID, "tonic"), TONIC);
        Registry.register(Registries.ITEM, new Identifier(WintersAppend.MOD_ID, "soul_meal"), SOUL_MEAL);
    }

    public static void addTabEntries(ItemGroup.Entries entries) {
        entries.add(SOUL_MEAL);
    }
}
