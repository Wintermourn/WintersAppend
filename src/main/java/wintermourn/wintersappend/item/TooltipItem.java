package wintermourn.wintersappend.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TooltipItem extends Item {
    List<Text> tooltipContents;

    public TooltipItem(Settings settings) {
        super(settings);
        tooltipContents = new ArrayList<>();
    }
    public TooltipItem(Settings settings, List<Text> tooltip) {
        super(settings);
        tooltipContents = List.copyOf(tooltip);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        tooltip.addAll(tooltipContents);
    }
}
