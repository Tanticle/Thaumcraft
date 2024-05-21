package tld.unknown.mystery.api.aspects;

import net.minecraft.resources.ResourceLocation;
import tld.unknown.mystery.data.aspects.AspectList;

public interface AspectContainer {

    AspectList getAspectList();

    int drainAspect(ResourceLocation aspect);
    int drainAspect(ResourceLocation aspect, int amount);

    int addAspect(ResourceLocation aspect, int amount);

    int getAspectCount(ResourceLocation aspect);

    default boolean hasAspect(ResourceLocation aspect, int amount) {
        return getAspectCount(aspect) >= amount;
    }

    default boolean hasAspect(ResourceLocation aspect) {
        return hasAspect(aspect, 1);
    }
}
