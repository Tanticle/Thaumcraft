package tld.unknown.mystery.api.aspects;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import tld.unknown.mystery.data.aspects.AspectList;

public interface AspectContainerItem {
    AspectList getAspects(ItemStack stack);

    static boolean hasAspect(ItemStack stack, ResourceKey<Aspect> aspect) {
        return stack.getItem() instanceof AspectContainerItem i && i.getAspects(stack).contains(aspect);
    }
}
