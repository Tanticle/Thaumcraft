package art.arcane.thaumcraft.data.generator.providers.recipes;

import com.google.gson.JsonElement;
import art.arcane.thaumcraft.registries.ConfigItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.data.aspects.AspectList;
import art.arcane.thaumcraft.data.recipes.AlchemyRecipe;
import art.arcane.thaumcraft.util.codec.data.CodecDataProvider;

import static art.arcane.thaumcraft.api.ThaumcraftData.Recipes;

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

        recipe(Recipes.Alchemy.TALLOW.location(),
                Ingredient.of(Items.ROTTEN_FLESH),
                new AspectList().add(ThaumcraftData.Aspects.FIRE, 1),
                new ItemStack(ConfigItems.TALLOW.get()));
    }

    @Override
    protected void processJson(JsonElement element) {
        element.getAsJsonObject().addProperty("type", Recipes.Types.ALCHEMY.location().toString());
    }

    private void recipe(ResourceLocation id, Ingredient catalyst, AspectList aspects, ItemStack result) {
        register(id, new AlchemyRecipe(catalyst, aspects, result));
    }
}
