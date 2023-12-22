package wintermourn.wintersappend.item;

import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TooltipBlockItem extends BlockItem {
    List<Text> tooltipContents;

    public TooltipBlockItem(Block block, Settings settings) {
        super(block, settings);
        tooltipContents = new ArrayList<>();
    }
    public TooltipBlockItem(Block block, Settings settings, List<Text> tooltip) {
        super(block, settings);
        tooltipContents = List.copyOf(tooltip);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        tooltip.addAll(tooltipContents);
    }
}
