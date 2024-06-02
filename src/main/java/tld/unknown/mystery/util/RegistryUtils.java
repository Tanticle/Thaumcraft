package tld.unknown.mystery.util;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;

public final class RegistryUtils {

    public static <T> ResourceLocation getKey(Holder<T> content) {
        return content.unwrapKey().get().location();
    }
}
