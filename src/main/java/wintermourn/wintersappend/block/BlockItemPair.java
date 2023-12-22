package wintermourn.wintersappend.block;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import wintermourn.wintersappend.item.TooltipBlockItem;

import java.util.AbstractMap;
import java.util.List;

public class BlockItemPair extends AbstractMap.SimpleEntry<Block, BlockItem> {
    public BlockItemPair(Block key, BlockItem value) {
        super(key, value);
    }

    public static BlockItemPair fromBlock(Block key)
    {
        return new BlockItemPair(key, new BlockItem(key, new Item.Settings()));
    }
    public static BlockItemPair fromBlock(Block key, Item.Settings settings)
    {
        return new BlockItemPair(key, new BlockItem(key, settings));
    }
    public static BlockItemPair fromBlock(Block key, List<Text> tooltip)
    {
        return new BlockItemPair(key, new TooltipBlockItem(key, new Item.Settings(), tooltip));
    }
    public static BlockItemPair fromBlock(Block key, Item.Settings settings, List<Text> tooltip)
    {
        return new BlockItemPair(key, new TooltipBlockItem(key, settings, tooltip));
    }

    public Block getBlock() {return this.getKey();}
    public BlockItem getItem() {return this.getValue();}
}
