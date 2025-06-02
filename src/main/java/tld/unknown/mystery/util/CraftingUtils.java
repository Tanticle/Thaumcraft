package tld.unknown.mystery.util;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import tld.unknown.mystery.api.capabilities.IResearchCapability;
import tld.unknown.mystery.data.aspects.AspectList;
import tld.unknown.mystery.data.recipes.AlchemyRecipe;
import tld.unknown.mystery.data.recipes.ArcaneCraftingRecipe;
import tld.unknown.mystery.registries.ConfigRecipeTypes;

import java.util.List;
import java.util.Optional;

public final class CraftingUtils {

    public static Optional<RecipeHolder<AlchemyRecipe>> findAlchemyRecipe(ServerLevel p, AspectList list, ItemStack stack, IResearchCapability research) {
        return p.recipeAccess().recipeMap().byType(ConfigRecipeTypes.ALCHEMY.type()).stream()
                .filter(holder -> holder.value().isValid(list, stack, research))
                .findFirst();
    }

    public static Optional<RecipeHolder<ArcaneCraftingRecipe>> findArcaneCraftingRecipe(ServerLevel p, SimpleContainer craftingSlots, IResearchCapability research) {
        return p.recipeAccess().recipeMap().byType(ConfigRecipeTypes.ARCANE_CRAFTING.type()).stream()
                .filter(holder -> holder.value().isValid(p, craftingSlots, research))
                .findFirst();
    }
}
