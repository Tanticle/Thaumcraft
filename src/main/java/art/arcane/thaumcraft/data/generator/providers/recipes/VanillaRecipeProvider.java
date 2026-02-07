package art.arcane.thaumcraft.data.generator.providers.recipes;

import art.arcane.thaumcraft.registries.ConfigItems;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import art.arcane.thaumcraft.registries.ConfigBlocks;

import java.util.concurrent.CompletableFuture;

public class VanillaRecipeProvider extends RecipeProvider {

    private final HolderGetter<Item> items;

    protected VanillaRecipeProvider(HolderLookup.Provider provider, RecipeOutput output) {
        super(provider, output);
        this.items = provider.lookupOrThrow(Registries.ITEM);
    }

    @Override
    protected void buildRecipes() {
        plate(ConfigItems.PLATE_IRON.get(), Items.IRON_INGOT);
        plate(ConfigItems.PLATE_BRASS.get(), ConfigItems.INGOT_BRASS.get());
        plate(ConfigItems.PLATE_THAUMIUM.get(), ConfigItems.INGOT_THAUMIUM.get());
        plate(ConfigItems.PLATE_VOID.get(), ConfigItems.INGOT_VOID.get());

        nineToOne(ConfigItems.INGOT_BRASS.get(), ConfigItems.NUGGET_BRASS.get(), "nuggets");
        nineToOne(ConfigItems.INGOT_THAUMIUM.get(), ConfigItems.NUGGET_THAUMIUM.get(), "nuggets");
        nineToOne(ConfigItems.INGOT_VOID.get(), ConfigItems.NUGGET_VOID.get(), "nuggets");
        nineToOne(ConfigItems.QUICKSILVER.get(), ConfigItems.NUGGET_QUICKSILVER.get(), "nuggets");

        oneToNine(ConfigItems.NUGGET_BRASS.get(), ConfigItems.INGOT_BRASS.get(), "ingot");
        oneToNine(ConfigItems.NUGGET_THAUMIUM.get(), ConfigItems.INGOT_THAUMIUM.get(), "ingot");
        oneToNine(ConfigItems.NUGGET_VOID.get(), ConfigItems.INGOT_VOID.get(), "ingot");
        oneToNine(ConfigItems.NUGGET_QUICKSILVER.get(), ConfigItems.QUICKSILVER.get(), "ingot");

        nineToOne(ConfigBlocks.METAL_BRASS.item(), ConfigItems.INGOT_BRASS.get(), "ingots");
        nineToOne(ConfigBlocks.METAL_THAUMIUM.item(), ConfigItems.INGOT_THAUMIUM.get(), "ingots");
        nineToOne(ConfigBlocks.METAL_VOID.item(), ConfigItems.INGOT_VOID.get(), "ingots");

        oneToNine(ConfigItems.INGOT_BRASS.get(), ConfigBlocks.METAL_BRASS.item(), "block");
        oneToNine(ConfigItems.INGOT_THAUMIUM.get(), ConfigBlocks.METAL_THAUMIUM.item(), "block");
        oneToNine(ConfigItems.INGOT_VOID.get(), ConfigBlocks.METAL_VOID.item(), "block");

        nineToOne(ConfigBlocks.AMBER_BLOCK.item(), ConfigItems.AMBER.get(), "amber");
        oneToNine(ConfigItems.AMBER.get(), ConfigBlocks.AMBER_BLOCK.item(), "block");

        ShapedRecipeBuilder.shaped(items, RecipeCategory.BUILDING_BLOCKS, ConfigBlocks.AMBER_BRICK.item(), 4)
                .pattern("AA")
                .pattern("AA")
                .define('A', ConfigBlocks.AMBER_BLOCK.item())
                .unlockedBy("has_ingredient", has(ConfigBlocks.AMBER_BLOCK.item()))
                .save(this.output);

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, ConfigItems.FILTER.get())
                .pattern(" S ")
                .pattern("SGS")
                .pattern(" S ")
                .define('S', Items.STRING)
                .define('G', Items.GOLD_NUGGET)
                .unlockedBy("has_string", has(Items.STRING))
                .save(this.output);

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, ConfigItems.FABRIC.get())
                .pattern("SW")
                .pattern("WS")
                .define('S', Items.STRING)
                .define('W', ItemTags.WOOL)
                .unlockedBy("has_string", has(Items.STRING))
                .save(this.output);

    }

    private void plate(ItemLike result, ItemLike ingredient) {
        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, result)
                .pattern("II")
                .define('I', ingredient)
                .unlockedBy("has_ingredient", has(ingredient))
                .save(this.output);
    }

    private void nineToOne(ItemLike result, ItemLike ingredient, String suffix) {
        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, result)
                .pattern("NNN")
                .pattern("NNN")
                .pattern("NNN")
                .define('N', ingredient)
                .unlockedBy("has_ingredient", has(ingredient))
                .save(this.output, "thaumcraft:" + getItemName(result) + "_from_" + suffix);
    }

    private void oneToNine(ItemLike result, ItemLike ingredient, String suffix) {
        ShapelessRecipeBuilder.shapeless(items, RecipeCategory.MISC, result, 9)
                .requires(ingredient)
                .unlockedBy("has_ingredient", has(ingredient))
                .save(this.output, "thaumcraft:" + getItemName(result) + "_from_" + suffix);
    }

    public static class Runner extends RecipeProvider.Runner {

        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, lookupProvider);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput output) {
            return new VanillaRecipeProvider(provider, output);
        }

        @Override
        public String getName() {
            return "Thaumcraft Vanilla Recipes";
        }
    }
}
