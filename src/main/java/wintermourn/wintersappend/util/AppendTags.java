package wintermourn.wintersappend.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import wintermourn.wintersappend.WintersAppend;

public class AppendTags {
    public static class Blocks
    {
        private static TagKey<Block> createTag(String name)
        {
            return TagKey.of(RegistryKeys.BLOCK, new Identifier(WintersAppend.MOD_ID, name));
        }
    }
    public static class Items
    {
        private static TagKey<Item> createTag(String name)
        {
            return TagKey.of(RegistryKeys.ITEM, new Identifier(WintersAppend.MOD_ID, name));
        }
    }
}
