package tld.unknown.mystery.util;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import tld.unknown.mystery.api.capabilities.IResearchCapability;
import tld.unknown.mystery.data.aspects.AspectList;
import tld.unknown.mystery.data.recipes.AlchemyRecipe;
import tld.unknown.mystery.data.recipes.ArcaneCraftingRecipe;
import tld.unknown.mystery.registries.ConfigRecipeTypes;

import java.util.Optional;

public final class CraftingUtils {

    public static Optional<RecipeHolder<AlchemyRecipe>> findAlchemyRecipe(Level p, AspectList list, ItemStack stack, IResearchCapability research) {
        return p.getRecipeManager().getAllRecipesFor(ConfigRecipeTypes.ALCHEMY.type()).stream()
                .filter(r -> r.value().isValid(list, stack, research))
                .findFirst();
    }

    public static Optional<RecipeHolder<ArcaneCraftingRecipe>> findArcaneCraftingRecipe(Level p, SimpleContainer craftingSlots, IResearchCapability research) {
        return p.getRecipeManager().getAllRecipesFor(ConfigRecipeTypes.ARCANE_CRAFTING.type()).stream()
                .filter(r -> r.value().isValidPattern(craftingSlots))
                .findFirst();
    }
}
