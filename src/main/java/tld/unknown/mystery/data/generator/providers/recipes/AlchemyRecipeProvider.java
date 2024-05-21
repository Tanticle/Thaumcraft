package tld.unknown.mystery.data.generator.providers.recipes;

import com.google.gson.JsonElement;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.api.ChaumtraftIDs;
import tld.unknown.mystery.data.aspects.AspectList;
import tld.unknown.mystery.data.recipes.AlchemyRecipe;
import tld.unknown.mystery.util.codec.data.CodecDataProvider;

import static tld.unknown.mystery.api.ChaumtraftIDs.Recipes;

public class AlchemyRecipeProvider extends CodecDataProvider<AlchemyRecipe> {

    public AlchemyRecipeProvider(PackOutput generator) {
        super(generator, "AlchemyRecipes", "recipes/alchemy", AlchemyRecipe.CODEC);
    }

    @Override
    protected void createEntries() {
        recipe(Recipes.ALCHEMY_DOUBLE_SLIME,
                Ingredient.of(new ItemStack(Items.SLIME_BALL)),
                new AspectList().add(ChaumtraftIDs.Aspects.WATER, 5).add(ChaumtraftIDs.Aspects.LIFE, 5).add(ChaumtraftIDs.Aspects.ALCHEMY, 1),
                new ItemStack(Items.SLIME_BALL, 2));

        recipe(Thaumcraft.id("debug"),
                Ingredient.of(new ItemStack(Items.STICK)),
                new AspectList().add(ChaumtraftIDs.Aspects.WATER, 5).add(ChaumtraftIDs.Aspects.EARTH, 5),
                new ItemStack(Items.DIAMOND));
    }

    @Override
    protected void processJson(JsonElement element) {
        element.getAsJsonObject().addProperty("type", Recipes.TYPE_ALCHEMY.toString());
    }

    private void recipe(ResourceLocation id, Ingredient catalyst, AspectList aspects, ItemStack result) {
        register(id, new AlchemyRecipe(catalyst, aspects, result));
    }
}
