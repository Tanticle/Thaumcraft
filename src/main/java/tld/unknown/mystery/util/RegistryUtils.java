package tld.unknown.mystery.util;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.util.thread.EffectiveSide;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public final class RegistryUtils {

    public static <T> ResourceKey<T> getKey(Holder<T> content) {
        return content.unwrapKey().get();
    }

    public static HolderLookup.Provider access() {
        if(FMLEnvironment.dist.isClient() && EffectiveSide.get().isClient()) {
            Minecraft client = Minecraft.getInstance();
            return client.level != null ? client.level.registryAccess() : null;
        } else {
            return ServerLifecycleHooks.getCurrentServer().registryAccess();
        }
    }
}
