package tld.unknown.mystery.data.generator.providers.recipes;

import com.google.gson.JsonElement;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import tld.unknown.mystery.api.ThaumcraftData;
import tld.unknown.mystery.api.aspects.Aspect;
import tld.unknown.mystery.data.recipes.ArcaneCraftingRecipe;
import tld.unknown.mystery.registries.ConfigBlocks;
import tld.unknown.mystery.util.codec.data.CodecDataProvider;

import java.util.Map;

public class ArcaneCraftingProvider extends CodecDataProvider<ArcaneCraftingRecipe> {

    public ArcaneCraftingProvider(PackOutput output) {
        super(output, "Arcane Crafting Recipes", "recipe/arcane_crafting", ArcaneCraftingRecipe.CODEC);
    }

    @Override
    protected void createEntries() {
        register(ThaumcraftData.Recipes.ArcaneCrafting.DEBUG.location(), new ArcaneCraftingRecipe.Builder(new ItemStack(Items.DIAMOND, 2))
                .setPattern(
                        "#+#",
                        "+ +",
                        "#+#",
                        Map.of('#', Ingredient.of(Items.STICK), '+', Ingredient.of(Items.GOLD_INGOT)))
                .setCrystalCost(Map.of(Aspect.Primal.AIR, 1))
                .setVisCost(20).build());

        register(ThaumcraftData.Recipes.ArcaneCrafting.TUBE.location(), new ArcaneCraftingRecipe.Builder(new ItemStack(ConfigBlocks.TUBE.item(), 4))
                .setPattern(
                        "###",
                        "+++",
                        "###",
                        Map.of('#', Ingredient.of(Items.STONE), '+', Ingredient.of(Items.GLASS)))
                .setCrystalCost(Map.of(Aspect.Primal.ORDER, 2))
                .setVisCost(20).build());
    }

    @Override
    protected void processJson(JsonElement element) {
        element.getAsJsonObject().addProperty("type", ThaumcraftData.Recipes.Types.ARCANE_CRAFTING.location().toString());
        super.processJson(element);
    }
}
