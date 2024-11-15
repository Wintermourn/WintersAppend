package wintermourn.wintersappend.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import java.awt.*;

public class CatalystUtil {

    public static int getHueColor(float hue)
    {
        if (hue > 1) hue = hue % 1800 / 1800;

        Color color = Color.getHSBColor(hue, 1f, 1f);
        return (color.getRed() << 16) | (color.getGreen() << 8) | color.getBlue();
    }

    public static int getColor(ItemStack item)
    {
        if (!item.hasNbt()) return 0xffffff;
        NbtCompound nbt = item.getNbt();
        assert nbt != null;
        return nbt.contains("color") ? nbt.getInt("color") : getHueColor(nbt.getString("id").hashCode());
    }


    public static ItemStack getStack(String catalystID)
    {
        return getStack(catalystID, getHueColor(catalystID.hashCode()));
    }
    public static ItemStack getStack(String catalystID, int color)
    {
        ItemStack catalyst = new ItemStack(AppendItems.CATALYST);
        NbtCompound compound = catalyst.getOrCreateNbt();
        compound.putString("id", catalystID);
        compound.putInt("color", color);

        return catalyst;
    }

}
