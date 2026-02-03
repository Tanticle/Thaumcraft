package art.arcane.thaumcraft.data.aspects.fallback;

import art.arcane.thaumcraft.data.aspects.AspectList;
import art.arcane.thaumcraft.data.recipes.AlchemyRecipe;
import art.arcane.thaumcraft.data.recipes.ArcaneCraftingRecipe;
import art.arcane.thaumcraft.data.recipes.InfusionRecipe;
import art.arcane.thaumcraft.registries.ConfigRecipeTypes;
import art.arcane.thaumcraft.util.codec.recipes.CodecRecipeSerializer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public final class RecipeAspectCalculator {

    private static final float REDUCTION_FACTOR = 0.75F;
    private static final int MAX_RECURSION_DEPTH = 10;

    private static RecipeManager recipeManager;
    private static Map<Item, List<RecipeInfo>> recipeCache;
    private static Function<ItemStack, AspectList> aspectLookup;

    public static void initialize(RecipeManager manager, Function<ItemStack, AspectList> lookup) {
        recipeManager = manager;
        aspectLookup = lookup;
        recipeCache = new HashMap<>();
        buildRecipeCache();
    }

    public static void clear() {
        recipeManager = null;
        aspectLookup = null;
        if (recipeCache != null) {
            recipeCache.clear();
            recipeCache = null;
        }
    }

    public static boolean isInitialized() {
        return recipeManager != null;
    }

    public static AspectList calculate(Item item) {
        if (!isInitialized()) return new AspectList();
        return calculateWithHistory(item, new HashSet<>(), 0);
    }

    private static AspectList calculateWithHistory(Item item, Set<Item> history, int depth) {
        if (depth >= MAX_RECURSION_DEPTH || history.contains(item))
            return new AspectList();

        List<RecipeInfo> recipes = recipeCache.get(item);
        if (recipes == null || recipes.isEmpty())
            return new AspectList();

        history.add(item);

        AspectList bestAspects = null;
        int lowestTotal = Integer.MAX_VALUE;

        for (RecipeInfo recipe : recipes) {
            AspectList aspects = new AspectList();

            for (Ingredient ingredient : recipe.ingredients) {
                AspectList ingAspects = getIngredientAspects(ingredient, new HashSet<>(history), depth + 1);
                aspects.merge(ingAspects);
            }

            float modifier = REDUCTION_FACTOR / recipe.outputCount;
            aspects = aspects.modify(modifier);
            aspects = AspectCuller.cull(aspects);

            if (aspects.size() < lowestTotal || (aspects.size() == lowestTotal && aspects.aspectCount() < (bestAspects != null ? bestAspects.aspectCount() : 0))) {
                lowestTotal = aspects.size();
                bestAspects = aspects;
            }
        }

        history.remove(item);
        return bestAspects != null ? bestAspects : new AspectList();
    }

    private static AspectList getIngredientAspects(Ingredient ingredient, Set<Item> history, int depth) {
        Optional<Item> firstItem = ingredient.items()
                .map(holder -> holder.value())
                .findFirst();

        if (firstItem.isEmpty())
            return new AspectList();

        ItemStack representativeStack = new ItemStack(firstItem.get());
        AspectList existingAspects = aspectLookup.apply(representativeStack);

        if (!existingAspects.isEmpty())
            return existingAspects;

        return calculateWithHistory(firstItem.get(), history, depth);
    }

    private static void buildRecipeCache() {
        recipeCache.clear();

        recipeManager.recipeMap().byType(RecipeType.CRAFTING).forEach(holder -> {
            CraftingRecipe recipe = holder.value();
            ItemStack result = getRecipeResult(recipe);
            if (!result.isEmpty() && !recipe.placementInfo().ingredients().isEmpty()) {
                addToCache(result.getItem(), recipe.placementInfo().ingredients(), result.getCount());
            }
        });

        recipeManager.recipeMap().byType(RecipeType.SMELTING).forEach(holder -> {
            Recipe<?> recipe = holder.value();
            ItemStack result = getRecipeResult(recipe);
            if (!result.isEmpty() && !recipe.placementInfo().ingredients().isEmpty()) {
                addToCache(result.getItem(), recipe.placementInfo().ingredients(), result.getCount());
            }
        });

        recipeManager.recipeMap().byType(RecipeType.BLASTING).forEach(holder -> {
            Recipe<?> recipe = holder.value();
            ItemStack result = getRecipeResult(recipe);
            if (!result.isEmpty() && !recipe.placementInfo().ingredients().isEmpty()) {
                addToCache(result.getItem(), recipe.placementInfo().ingredients(), result.getCount());
            }
        });

        recipeManager.recipeMap().byType(RecipeType.SMOKING).forEach(holder -> {
            Recipe<?> recipe = holder.value();
            ItemStack result = getRecipeResult(recipe);
            if (!result.isEmpty() && !recipe.placementInfo().ingredients().isEmpty()) {
                addToCache(result.getItem(), recipe.placementInfo().ingredients(), result.getCount());
            }
        });

        recipeManager.recipeMap().byType(ConfigRecipeTypes.ARCANE_CRAFTING.type()).forEach(holder -> {
            ArcaneCraftingRecipe recipe = holder.value();
            ItemStack result = recipe.result();
            if (!result.isEmpty()) {
                List<Ingredient> ingredients = flattenGrid(recipe.grid());
                addToCache(result.getItem(), ingredients, result.getCount());
            }
        });

        recipeManager.recipeMap().byType(ConfigRecipeTypes.ALCHEMY.type()).forEach(holder -> {
            AlchemyRecipe recipe = holder.value();
            ItemStack result = recipe.result();
            if (!result.isEmpty()) {
                addToCache(result.getItem(), List.of(recipe.catalyst()), result.getCount());
            }
        });

        recipeManager.recipeMap().byType(ConfigRecipeTypes.INFUSION.type()).forEach(holder -> {
            InfusionRecipe recipe = holder.value();
            ItemStack result = recipe.result();
            if (!result.isEmpty()) {
                List<Ingredient> ingredients = new ArrayList<>();
                ingredients.add(recipe.catalyst());
                ingredients.addAll(recipe.components());
                addToCache(result.getItem(), ingredients, result.getCount());
            }
        });
    }

    private static ItemStack getRecipeResult(Recipe<?> recipe) {
        try {
            for (RecipeDisplay display : recipe.display()) {
                SlotDisplay result = display.result();
                if (result instanceof SlotDisplay.ItemSlotDisplay itemDisplay) {
                    return new ItemStack(itemDisplay.item().value());
                }
                if (result instanceof SlotDisplay.ItemStackSlotDisplay stackDisplay) {
                    return stackDisplay.stack();
                }
            }
        } catch (Exception ignored) {}
        return ItemStack.EMPTY;
    }

    private static void addToCache(Item item, List<Ingredient> ingredients, int outputCount) {
        List<Ingredient> nonEmpty = ingredients.stream()
                .filter(ing -> !ing.isEmpty())
                .toList();

        if (nonEmpty.isEmpty())
            return;

        recipeCache.computeIfAbsent(item, k -> new ArrayList<>())
                .add(new RecipeInfo(nonEmpty, outputCount));
    }

    private static List<Ingredient> flattenGrid(CodecRecipeSerializer.CraftingGrid grid) {
        List<Ingredient> result = new ArrayList<>();
        for (String row : grid.pattern()) {
            for (char c : row.toCharArray()) {
                Ingredient ing = grid.keys().get(c);
                if (ing != null && !ing.isEmpty()) {
                    result.add(ing);
                }
            }
        }
        return result;
    }

    private record RecipeInfo(List<Ingredient> ingredients, int outputCount) {}
}
