package art.arcane.thaumcraft.integrations.jei;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import art.arcane.thaumcraft.api.aspects.Aspect;
import art.arcane.thaumcraft.registries.ConfigDataRegistries;

import java.util.ArrayList;
import java.util.List;

public class AspectFromItemStack {
    public ResourceKey<Aspect> aspect;
    public ArrayList<ItemStack> items;

    public AspectFromItemStack(List<ItemStack> items, ResourceKey<Aspect> aspect) {
        this.aspect = aspect;
        this.items = new ArrayList<>();

        for (ItemStack item : items) {
            var itemAspects = ConfigDataRegistries.ASPECT_REGISTRY.getAspects(item);
            if (itemAspects.contains(aspect)) {
                // Create a copy of the item, abusing it's count as indicator for the amount of the target aspect
                var recipeItem = item.copy();
                recipeItem.setCount(itemAspects.getAspect(aspect));
                this.items.add(recipeItem);
            }
        }

        // Sort by aspect count descending
        this.items.sort((a, b) -> Integer.compare(b.getCount(), a.getCount()));
    }

    public List<AspectFromItemStack> split(int max) {
        List<AspectFromItemStack> result = new ArrayList<>();
        for (int i = 0; i < items.size(); i += max) {
            int end = Math.min(i + max, items.size());
            result.add(new AspectFromItemStack(items.subList(i, end), aspect));
        }
        return result;
    }
}
