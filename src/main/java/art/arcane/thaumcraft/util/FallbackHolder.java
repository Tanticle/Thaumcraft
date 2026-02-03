package art.arcane.thaumcraft.util;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class FallbackHolder<T> implements Holder<T> {

    private final ResourceKey<T> key;
    private final T value;

    public FallbackHolder(ResourceKey<T> key, T value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public T value() {
        return value;
    }

    @Override
    public boolean isBound() {
        return true;
    }

    @Override
    public boolean is(ResourceLocation pLocation) {
        return pLocation.equals(key.location());
    }

    @Override
    public boolean is(ResourceKey<T> pResourceKey) {
        return pResourceKey.equals(key);
    }

    @Override
    public boolean is(Predicate<ResourceKey<T>> pPredicate) {
        return pPredicate.test(key);
    }

    @Override
    public boolean is(TagKey<T> pTagKey) {
        return pTagKey.location().equals(key.location());
    }

    @Override
    public boolean is(Holder<T> pHolder) {
        return pHolder.equals(this);
    }

    @Override
    public Stream<TagKey<T>> tags() {
        return Stream.empty();
    }

    @Override
    public Either<ResourceKey<T>, T> unwrap() {
        return Either.left(key);
    }

    @Override
    public Optional<ResourceKey<T>> unwrapKey() {
        return Optional.of(key);
    }

    @Override
    public Kind kind() {
        return Kind.REFERENCE;
    }

    @Override
    public boolean canSerializeIn(HolderOwner<T> pOwner) {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Holder<?> other)) return false;
        return other.unwrapKey().map(k -> k.equals(key)).orElse(false);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }
}
