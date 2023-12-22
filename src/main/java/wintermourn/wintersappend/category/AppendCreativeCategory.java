package wintermourn.wintersappend.category;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import wintermourn.wintersappend.WintersAppend;
import wintermourn.wintersappend.block.AppendBlocks;
import wintermourn.wintersappend.effects.ImmunizationEffects;
import wintermourn.wintersappend.effects.MinorEffects;
import wintermourn.wintersappend.item.AppendItems;
import wintermourn.wintersappend.item.TonicUtil;

public class AppendCreativeCategory {
    public static ItemGroup APPEND_GROUP = FabricItemGroup.builder()
            .icon(() -> AppendBlocks.GYPSOPHILA.getItem().getDefaultStack())
            .displayName(Text.translatable("group.winters_append.main"))
            .entries((ctx, entries) -> {
                AppendBlocks.addTabEntries(entries);

                AppendItems.addTabEntries(entries);

                TonicUtil.registerCreativeItems(entries, MinorEffects.HEALTH_BOOST);
                TonicUtil.registerCreativeItems(entries, StatusEffects.HEALTH_BOOST, 1);
                TonicUtil.registerCreativeItems(entries, MinorEffects.ABSORPTION);
                TonicUtil.registerCreativeItems(entries, StatusEffects.ABSORPTION);
                TonicUtil.registerCreativeItems(entries, MinorEffects.ARMOR);
                TonicUtil.registerCreativeItems(entries, MinorEffects.SPEED);
                TonicUtil.registerCreativeItems(entries, MinorEffects.RESISTANCE_PHYSICAL);
                TonicUtil.registerCreativeItems(entries, MinorEffects.RESISTANCE_PROJECTILE);

                TonicUtil.registerCreativeItems(entries, ImmunizationEffects.ANTIVENOM, 1);
                TonicUtil.registerCreativeItems(entries, ImmunizationEffects.DETHORNING, 2);
                TonicUtil.registerCreativeItems(entries, ImmunizationEffects.KINETIC_RESIST, 3);



            })
            .build();

    public static void Register()
    {
        Registry.register(Registries.ITEM_GROUP, new Identifier(WintersAppend.MOD_ID, "main"), APPEND_GROUP);
    }
}
