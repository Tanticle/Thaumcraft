package tld.unknown.mystery.util;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import tld.unknown.mystery.api.capabilities.IResearchCapability;
import tld.unknown.mystery.data.aspects.AspectList;
import tld.unknown.mystery.data.recipes.AlchemyRecipe;
import tld.unknown.mystery.data.recipes.ArcaneCraftingRecipe;
import tld.unknown.mystery.registries.ConfigRecipeTypes;

import java.util.Optional;

public final class CraftingUtils {

    public static Optional<RecipeHolder<AlchemyRecipe>> findAlchemyRecipe(ServerLevel p, AspectList list, ItemStack stack, IResearchCapability research) {
        AlchemyRecipe.Input input = new AlchemyRecipe.Input(research, stack, list);
        return p.recipeAccess().recipeMap().byType(ConfigRecipeTypes.ALCHEMY.type()).stream()
                .filter(holder -> holder.value().matches(input, p))
                .findFirst();
    }

    public static Optional<RecipeHolder<ArcaneCraftingRecipe>> findArcaneCraftingRecipe(ServerLevel p, RecipeInput craftingSlots, IResearchCapability research) {
        return p.recipeAccess().recipeMap().byType(ConfigRecipeTypes.ARCANE_CRAFTING.type()).stream()
                .filter(holder -> holder.value().isValid(p, craftingSlots, research))
                .findFirst();
    }
}
