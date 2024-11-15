package wintermourn.wintersappend.recipe;


import com.google.gson.*;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import wintermourn.wintersappend.WintersAppend;

public class CatalystRecipeSerializer implements RecipeSerializer<CatalystRecipe>
{
    private CatalystRecipeSerializer() {}

    public static final CatalystRecipeSerializer INSTANCE = new CatalystRecipeSerializer();
    public static final Identifier ID = new Identifier(WintersAppend.MOD_ID, "catalyst_recipe");

    public CatalystRecipe read(Identifier id, JsonObject json) {
        CatalystRecipe.CatalystRecipeJson recipeJson = new Gson().fromJson(json, CatalystRecipe.CatalystRecipeJson.class);

        if (recipeJson.inputs == null) throw new JsonSyntaxException("Recipe inputs array is missing!");
        if (recipeJson.output == null) throw new JsonSyntaxException("Recipe output string is missing!");

        DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(3, Ingredient.EMPTY);

        for (int i = 0; i < 3; i++) {
            JsonElement element = recipeJson.inputs.get(i);

            if (element == null || element instanceof JsonNull)
            {
                break;
            }
            ingredients.set(i, Ingredient.fromJson(element, true));
        }

        String output = recipeJson.output;

        int brewTime = recipeJson.brewing_time != null ? recipeJson.brewing_time :  200;
        int fuelCost = recipeJson.fuel_price != null ? recipeJson.fuel_price :  10;
        int purityCost = recipeJson.purity_needed != null ? recipeJson.purity_needed :  10;
        int color = recipeJson.color != null ? recipeJson.color : -1;

        return new CatalystRecipe(id, output, ingredients, brewTime, fuelCost, purityCost, color);
    }

    public CatalystRecipe read(Identifier id, PacketByteBuf buf) {
        DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(3, Ingredient.EMPTY);
        for (int i = 0; i < 3; i++) {
            ingredients.set(i, Ingredient.fromPacket(buf));
        }
        int brewTime = buf.readInt();
        int fuelCost = buf.readInt();
        int purityCost = buf.readInt();
        int color = buf.readInt();
        String output = buf.readString();

        return new CatalystRecipe(id, output, ingredients, brewTime, fuelCost, purityCost, color);
    }

    public void write(PacketByteBuf buf, CatalystRecipe recipe) {
        for (int i = 0; i < 3; i++) {
            recipe.ingredients.get(i).write(buf);
        }
        buf.writeInt(recipe.brewingTime);
        buf.writeInt(recipe.fuelCost);
        buf.writeInt(recipe.purityCost);
        buf.writeInt(recipe.color);
        buf.writeString(recipe.catalystID);
    }
}
