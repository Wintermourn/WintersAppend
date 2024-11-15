package wintermourn.wintersappend.recipe;


import com.google.gson.*;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import wintermourn.wintersappend.WintersAppend;

public class TonicBrewingRecipeSerializer implements RecipeSerializer<TonicBrewingRecipe>
{
    private TonicBrewingRecipeSerializer() {}

    public static final TonicBrewingRecipeSerializer INSTANCE = new TonicBrewingRecipeSerializer();
    public static final Identifier ID = new Identifier(WintersAppend.MOD_ID, "tonic_stand_recipe");

    public TonicBrewingRecipe read(Identifier id, JsonObject json) {
        TonicBrewingRecipe.TonicStandRecipeJson recipeJson = new Gson().fromJson(json, TonicBrewingRecipe.TonicStandRecipeJson.class);

        if (recipeJson.inputs == null) throw new JsonSyntaxException("Recipe inputs array is missing!");
        if (recipeJson.output == null) throw new JsonSyntaxException("Recipe output id is missing!");

        DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(3, Ingredient.EMPTY);

        for (int i = 0; i < 3; i++) {
            JsonElement element = recipeJson.inputs.get(i);

            if (element == null || element instanceof JsonNull)
            {
                break;
            }
            ingredients.set(i, Ingredient.fromJson(element, true));
        }

        Identifier output = new Identifier(recipeJson.output);

        int brewTime = recipeJson.brewing_time != null ? recipeJson.brewing_time :  200;
        int fuelCost = recipeJson.fuel_price != null ? recipeJson.fuel_price :  10;
        int purityCost = recipeJson.purity_needed != null ? recipeJson.purity_needed :  10;
        int applicationLimit = recipeJson.application_limit != null ? recipeJson.application_limit :  99;
        int span_offset = recipeJson.span_offset != null ? recipeJson.span_offset :  0;

        return new TonicBrewingRecipe(id, output, ingredients, brewTime, fuelCost, purityCost, applicationLimit, span_offset);
    }

    public TonicBrewingRecipe read(Identifier id, PacketByteBuf buf) {
        DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(3, Ingredient.EMPTY);
        for (int i = 0; i < 3; i++) {
            ingredients.set(i, Ingredient.fromPacket(buf));
        }
        int brewTime = buf.readInt();
        int fuelCost = buf.readInt();
        int purityCost = buf.readInt();
        int applicationLimit = buf.readInt();
        int spanOffset = buf.readInt();
        Identifier statusEffect = buf.readIdentifier();

        return new TonicBrewingRecipe(id, statusEffect, ingredients, brewTime, fuelCost, purityCost, applicationLimit, spanOffset);
    }

    public void write(PacketByteBuf buf, TonicBrewingRecipe recipe) {
        for (int i = 0; i < 3; i++) {
            recipe.ingredients.get(i).write(buf);
        }
        buf.writeInt(recipe.brewingTime);
        buf.writeInt(recipe.fuelCost);
        buf.writeInt(recipe.purityCost);
        buf.writeInt(recipe.applicationLimit);
        buf.writeInt(recipe.spanOffset);
        buf.writeIdentifier(recipe.output);
    }
}
