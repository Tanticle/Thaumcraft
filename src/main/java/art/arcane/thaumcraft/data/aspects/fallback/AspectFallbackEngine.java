package art.arcane.thaumcraft.data.aspects.fallback;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.data.aspects.AspectList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Function;

public final class AspectFallbackEngine {

	private static final Map<ItemStack, AspectList> dynamicCache = new WeakHashMap<>();
	private static boolean initialized = false;
	private static Function<ItemStack, AspectList> registryLookup;

	public static void initialize(RecipeManager recipeManager, Function<ItemStack, AspectList> aspectRegistryLookup) {
		registryLookup = aspectRegistryLookup;
		RecipeAspectCalculator.initialize(recipeManager, aspectRegistryLookup);
		initialized = true;
		dynamicCache.clear();
		Thaumcraft.info("ASPECT FALLBACK SYSTEMS ENGAGED :)");
	}

	public static void clear() {
		RecipeAspectCalculator.clear();
		registryLookup = null;
		initialized = false;
		dynamicCache.clear();
	}

	public static boolean isInitialized() {
		return initialized;
	}

	public static AspectList calculate(ItemStack stack) {
		if (!initialized || stack.isEmpty())
			return new AspectList();

		AspectList cached = dynamicCache.get(stack);
		if (cached != null)
			return cached.clone();

		AspectList result = calculateInternal(stack);
		if (!result.isEmpty())
			dynamicCache.put(stack.copy(), result);

		return result;
	}

	private static AspectList calculateInternal(ItemStack stack) {
		AspectList baseAspects = RecipeAspectCalculator.calculate(stack.getItem());
		AspectList dynamicAspects = DynamicPropertyCalculator.calculate(stack);
		AspectList enchantAspects = EnchantmentAspectCalculator.calculate(stack);

		AspectList combined = baseAspects.clone();
		combined.merge(dynamicAspects);
		combined.merge(enchantAspects);

		return AspectCuller.cull(combined);
	}
}
