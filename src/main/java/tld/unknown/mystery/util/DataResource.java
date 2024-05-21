package tld.unknown.mystery.util;

import com.mojang.datafixers.util.Either;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import tld.unknown.mystery.util.codec.data.CodecDataManager;

import java.util.function.Supplier;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class DataResource<T> implements Supplier<T> {

    private final Either<CodecDataManager<T>, Registry<T>> registry;
    @Getter
    private final ResourceLocation id;

    public static <T> DataResource<T> of(CodecDataManager<T> registry, ResourceLocation id) {
        return new DataResource<>(Either.left(registry), id);
    }

    public static <T> DataResource<T> of(Registry<T> registry, ResourceLocation id) {
        return new DataResource<>(Either.right(registry), id);
    }

    @Override
    public T get() {
        if(registry.left().isPresent())
            return registry.left().get().get(this.id);
        return registry.right().get().get(this.id);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DataResource<?> dr) {
            if(registry.left().isPresent())
                return dr.registry.left().get().getName().equals(this.registry.left().get().getName()) && dr.id == this.id;
            return dr.registry.right().get().equals(this.registry.right().get()) && dr.id == this.id;
        }
        return false;
    }
}
