package wintermourn.wintersappend.recipe;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import wintermourn.wintersappend.WintersAppend;

public class TonicStandRecipeSerializer implements RecipeSerializer<TonicStandRecipe>
{
    private TonicStandRecipeSerializer() {}

    public static final TonicStandRecipeSerializer INSTANCE = new TonicStandRecipeSerializer();
    public static final Identifier ID = new Identifier(WintersAppend.MOD_ID, "tonic_stand_recipe");

    public TonicStandRecipe read(Identifier id, JsonObject json) {
        TonicStandRecipe.TonicStandRecipeJson recipeJson = new Gson().fromJson(json, TonicStandRecipe.TonicStandRecipeJson.class);

        if (recipeJson.inputs == null) throw new JsonSyntaxException("Recipe inputs array is missing!");
        if (recipeJson.output == null) throw new JsonSyntaxException("Recipe output id is missing!");

        DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(3, Ingredient.EMPTY);

        for (int i = 0; i < 3; i++) {
            JsonElement element = recipeJson.inputs.get(i);

            if (element == null)
            {
                break;
            }
            ingredients.set(i, Ingredient.fromJson(element));
        }

        Identifier output = new Identifier(recipeJson.output);

        int brewTime = recipeJson.brewing_time != null ? recipeJson.brewing_time :  200;
        int fuelCost = recipeJson.fuel_price != null ? recipeJson.fuel_price :  10;
        int purityCost = recipeJson.purity_needed != null ? recipeJson.purity_needed :  10;
        int applicationLimit = recipeJson.application_limit != null ? recipeJson.application_limit :  99;

        return new TonicStandRecipe(id, output, ingredients, brewTime, fuelCost, purityCost, applicationLimit);
    }

    public TonicStandRecipe read(Identifier id, PacketByteBuf buf) {
        DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(3, Ingredient.EMPTY);
        for (int i = 0; i < 3; i++) {
            ingredients.set(i, Ingredient.fromPacket(buf));
        }
        int brewTime = buf.readInt();
        int fuelCost = buf.readInt();
        int purityCost = buf.readInt();
        int applicationLimit = buf.readInt();
        Identifier statusEffect = buf.readIdentifier();

        return new TonicStandRecipe(id, statusEffect, ingredients, brewTime, fuelCost, purityCost, applicationLimit);
    }

    public void write(PacketByteBuf buf, TonicStandRecipe recipe) {
        for (int i = 0; i < 3; i++) {
            recipe.ingredients.get(i).write(buf);
        }
        buf.writeInt(recipe.maxBrewingTime);
        buf.writeInt(recipe.maxFuelCost);
        buf.writeInt(recipe.maxPurityCost);
        buf.writeInt(recipe.applicationLimit);
        buf.writeIdentifier(recipe.output);
    }
}
