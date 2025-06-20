package tld.unknown.mystery.data.generator.providers.recipes;

import com.google.gson.JsonElement;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import tld.unknown.mystery.api.ThaumcraftData;
import tld.unknown.mystery.data.aspects.AspectList;
import tld.unknown.mystery.data.recipes.AlchemyRecipe;
import tld.unknown.mystery.util.codec.data.CodecDataProvider;

import static tld.unknown.mystery.api.ThaumcraftData.Recipes;

public class AlchemyProvider extends CodecDataProvider<AlchemyRecipe> {

    public AlchemyProvider(PackOutput generator) {
        super(generator, "AlchemyRecipes", "recipe/alchemy", AlchemyRecipe.CODEC);
    }

    @Override
    protected void createEntries() {
        /*recipe(Recipes.ALCHEMY_DOUBLE_SLIME,
                Ingredient.of(Items.SLIME_BALL),
                new AspectList().add(ThaumcraftData.Aspects.WATER, 5).add(ThaumcraftData.Aspects.LIFE, 5).add(ThaumcraftData.Aspects.ALCHEMY, 1),
                new ItemStack(Items.SLIME_BALL, 2));*/

        recipe(Recipes.Alchemy.DEBUG.location(),
                Ingredient.of(Items.STICK),
                new AspectList().add(ThaumcraftData.Aspects.WATER, 5).add(ThaumcraftData.Aspects.EARTH, 5),
                new ItemStack(Items.DIAMOND));
    }

    @Override
    protected void processJson(JsonElement element) {
        element.getAsJsonObject().addProperty("type", Recipes.Types.ALCHEMY.location().toString());
    }

    private void recipe(ResourceLocation id, Ingredient catalyst, AspectList aspects, ItemStack result) {
        register(id, new AlchemyRecipe(catalyst, aspects, result));
    }
}
