package art.arcane.thaumcraft.client;

import art.arcane.thaumcraft.data.recipes.InfusionRecipe;
import lombok.Setter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.*;

public class ThaumcraftClientRecipes {

	@Setter
	private static RecipeMap recipeMap;

	public static <I extends RecipeInput, T extends Recipe<I>> RecipeHolder<T> getRecipe(RecipeType<T> recipeType, ResourceLocation id) {
		return recipeMap.byType(recipeType).stream().filter(r -> r.id().location().equals(id)).findFirst().orElse(null);
	}
}
