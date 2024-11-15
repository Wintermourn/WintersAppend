package wintermourn.wintersappend.mixin.common;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.JsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wintermourn.wintersappend.item.AppendItems;

@Mixin(Ingredient.class)
public class IngredientMixin {
    @Inject(method = "entryFromJson", at = @At("HEAD"), cancellable = true)
    private static void test(JsonObject json, CallbackInfoReturnable<Ingredient.Entry> cir)
    {
        if (json.has("catalyst")) {
            String catalyst =JsonHelper.getString(json, "catalyst");
            ItemStack stack = new ItemStack(AppendItems.CATALYST);
            NbtCompound compound = new NbtCompound();
            compound.putString("id", catalyst);
            stack.setNbt(compound);

            cir.setReturnValue(new Ingredient.StackEntry(stack));
        }
    }
}
