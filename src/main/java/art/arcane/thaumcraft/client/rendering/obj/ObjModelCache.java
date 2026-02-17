package art.arcane.thaumcraft.client.rendering.obj;

import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ObjModelCache {

    private static final Map<ResourceLocation, WavefrontObject> CACHE = new ConcurrentHashMap<>();

    private ObjModelCache() {}

    public static WavefrontObject get(ResourceLocation resource) {
        return CACHE.computeIfAbsent(resource, WavefrontObject::new);
    }

    public static void clear() {
        CACHE.clear();
    }
}
