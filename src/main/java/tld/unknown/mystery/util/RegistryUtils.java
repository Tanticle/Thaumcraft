package tld.unknown.mystery.util;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.util.thread.EffectiveSide;
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

    public static ResourceLocation getBlockLocation(Holder<?> holder, String... subfolder) {
        ResourceLocation id = holder.getKey().location();
        if(subfolder.length == 0) {
            return id.withPath(s -> String.format("block/%s", s));
        }
        return id.withPath(s -> String.format("block/%s/%s", String.join("/", subfolder), s));
    }

    public static ResourceLocation getObjLocation(Holder<?> holder, String... subfolder) {
        return getBlockLocation(holder, subfolder).withPrefix("models/").withSuffix(".obj");
    }

    public static ResourceLocation getItemLocation(Holder<?> holder, String... subfolder) {
        ResourceLocation id = holder.getKey().location();
        if(subfolder.length == 0) {
            return id.withPath(s -> String.format("item/%s", s));
        }
        return id.withPath(s -> String.format("item/%s/%s", String.join("/", subfolder), s));
    }

    public static ResourceLocation getBlockItemLocation(Holder<?> holder) {
        return getItemLocation(holder, "block");
    }
}
