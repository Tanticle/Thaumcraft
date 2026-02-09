package art.arcane.thaumcraft.data.golemancy;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public final class SealBehaviors {

    private static final Map<ResourceLocation, SealBehavior> REGISTRY = new HashMap<>();

    public static void register(ResourceKey<SealType> key, SealBehavior behavior) {
        REGISTRY.put(key.location(), behavior);
    }

    public static SealBehavior get(ResourceKey<SealType> key) {
        return REGISTRY.get(key.location());
    }

    public static boolean has(ResourceKey<SealType> key) {
        return REGISTRY.containsKey(key.location());
    }
}
