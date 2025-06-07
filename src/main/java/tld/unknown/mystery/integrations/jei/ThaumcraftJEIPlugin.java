package tld.unknown.mystery.integrations.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.recipe.types.IRecipeType;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.api.ThaumcraftData;
import tld.unknown.mystery.data.aspects.AspectList;
import tld.unknown.mystery.data.recipes.ArcaneCraftingRecipe;
import tld.unknown.mystery.integrations.jei.aspect.AspectIngredientCodec;
import tld.unknown.mystery.integrations.jei.aspect.AspectIngredientHelper;
import tld.unknown.mystery.integrations.jei.aspect.AspectIngredientRenderer;
import tld.unknown.mystery.integrations.jei.category.ArcaneCraftingRecipeCategory;
import tld.unknown.mystery.integrations.jei.category.AspectFromItemStackCategory;
import tld.unknown.mystery.registries.ConfigBlocks;
import tld.unknown.mystery.registries.ConfigDataRegistries;
import tld.unknown.mystery.registries.ConfigItems;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class ThaumcraftJEIPlugin implements IModPlugin {
    public static final IIngredientType<AspectList> ASPECT_LIST = () -> AspectList.class;
    public static final IRecipeType<AspectFromItemStack> ASPECT_FROM_ITEM_STACK_RECIPE = IRecipeType.create(ResourceLocation.fromNamespaceAndPath("thaumcraft", "aspect_from_item_stack"), AspectFromItemStack.class);
    public static final IRecipeType<ArcaneCraftingRecipe> ARCANE_RECIPE = IRecipeType.create(ThaumcraftData.Recipes.Types.ARCANE_CRAFTING.location(), ArcaneCraftingRecipe.class);


    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(Thaumcraft.MOD_ID, "jei");
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registration) {
        Thaumcraft.info("Registering JEI ingredients");
        ArrayList<AspectList> ingredients = new ArrayList<>();
        ConfigDataRegistries.ASPECTS.keys(Minecraft.getInstance().getConnection().registryAccess()).forEach(aspectKey -> {
            AspectList aspectList = new AspectList();
            aspectList.add(aspectKey, 1);
            ingredients.add(aspectList);
        });
        registration.register(ASPECT_LIST, ingredients, new AspectIngredientHelper(), new AspectIngredientRenderer(), new AspectIngredientCodec());
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        var jeiHelpers = registration.getJeiHelpers();
        var icon = jeiHelpers.getGuiHelper().createDrawableItemLike(ConfigItems.SCRIBING_TOOLS.value());
        registration.addRecipeCategories(new AspectFromItemStackCategory(icon));

        registration.addRecipeCategories(new ArcaneCraftingRecipeCategory());
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        var items = registration.getIngredientManager().getAllIngredients(VanillaTypes.ITEM_STACK).stream().toList();
        var aspectRecipes = new ArrayList<AspectFromItemStack>();
        ConfigDataRegistries.ASPECTS.keys(Minecraft.getInstance().getConnection().registryAccess()).forEach(aspect -> {
            var recipe = new AspectFromItemStack(items, aspect);
            if (!recipe.items.isEmpty()) {
                aspectRecipes.addAll(recipe.split(AspectFromItemStackCategory.COLUMNS * AspectFromItemStackCategory.ROWS));
            }
        });

        registration.addRecipes(ASPECT_FROM_ITEM_STACK_RECIPE, aspectRecipes);

        var recipes = ServerLifecycleHooks.getCurrentServer().getRecipeManager();
        for (var recipe : recipes.getRecipes()) {
            if (recipe.value() instanceof ArcaneCraftingRecipe arcaneRecipe) {
                registration.addRecipes(ARCANE_RECIPE, List.of(arcaneRecipe));
            }
        }
        Thaumcraft.info("Game has " + recipes.getRecipes().toArray().length + " recipes");
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(ARCANE_RECIPE, ConfigBlocks.ARCANE_WORKBENCH.item());
    }
}
