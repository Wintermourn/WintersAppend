package wintermourn.wintersappend.recipe;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class AppendRecipes {
//    private static final RegistryKey<Registry<TonicStandRecipe>> TONIC_STAND_RECIPES_KEY = RegistryKey.ofRegistry(new Identifier(WintersAppend.MOD_ID, "tonic_brewing"));
//    public static final Registry<TonicStandRecipe> TONIC_STAND_RECIPES = FabricRegistryBuilder.createSimple(TONIC_STAND_RECIPES_KEY)
//            .attribute(RegistryAttribute.SYNCED)
//            .buildAndRegister();
    public static void Register()
    {
        Registry.register(Registries.RECIPE_SERIALIZER, TonicBrewingRecipeSerializer.ID, TonicBrewingRecipeSerializer.INSTANCE);
        Registry.register(Registries.RECIPE_TYPE, TonicBrewingRecipe.Type.ID, TonicBrewingRecipe.Type.INSTANCE);
        Registry.register(Registries.RECIPE_SERIALIZER, CatalystRecipeSerializer.ID, CatalystRecipeSerializer.INSTANCE);
        Registry.register(Registries.RECIPE_TYPE, CatalystRecipe.Type.ID, CatalystRecipe.Type.INSTANCE);
    }
}
