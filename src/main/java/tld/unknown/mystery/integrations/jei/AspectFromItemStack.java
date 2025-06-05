package tld.unknown.mystery.integrations.jei;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import tld.unknown.mystery.api.aspects.Aspect;
import tld.unknown.mystery.registries.ConfigDataRegistries;

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
}
