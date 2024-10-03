package tld.unknown.mystery.api.aspects;

import net.minecraft.resources.ResourceKey;
import tld.unknown.mystery.data.aspects.AspectList;

public interface AspectContainer {

    AspectList getAspectList();

    int drainAspect(ResourceKey<Aspect> aspect);
    int drainAspect(ResourceKey<Aspect> aspect, int amount);

    int addAspect(ResourceKey<Aspect> aspect, int amount);

    int getAspectCount(ResourceKey<Aspect> aspect);

    default boolean hasAspect(ResourceKey<Aspect> aspect, int amount) {
        return getAspectCount(aspect) >= amount;
    }

    default boolean hasAspect(ResourceKey<Aspect> aspect) {
        return hasAspect(aspect, 1);
    }
}
