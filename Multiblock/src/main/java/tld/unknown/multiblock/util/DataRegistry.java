package tld.unknown.multiblock.util;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

import java.util.stream.Stream;

public record DataRegistry<T>(ResourceKey<Registry<T>> key, Codec<T> codec, Codec<T> syncCodec, ResourceLocation missingId) {

    public T get(HolderLookup.Provider registries, ResourceKey<T> id) {
        return getHolder(registries, id).value();
    }

    public Holder<T> getHolder(HolderLookup.Provider registries, ResourceKey<T> id) {
        return registries.lookupOrThrow(key).get(id).orElse(defaultValue(registries));
    }

    public Stream<ResourceKey<T>> keys(HolderLookup.Provider access) {
        return access.lookupOrThrow(key).listElementIds();
    }

    public Stream<Holder.Reference<T>> holderStream(HolderLookup.Provider access) {
        return access.lookupOrThrow(key).listElements();
    }

    public void register(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(key, codec, syncCodec, builder -> builder.defaultKey(missingId));
    }

    private Holder.Reference<T> defaultValue(HolderLookup.Provider registries) {
        return registries.lookupOrThrow(key).get(ResourceKey.create(key, missingId)).get();
    }
}