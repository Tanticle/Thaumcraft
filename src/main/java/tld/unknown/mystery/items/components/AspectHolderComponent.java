package tld.unknown.mystery.items.components;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFileCodec;
import tld.unknown.mystery.api.ThaumcraftData;
import tld.unknown.mystery.api.aspects.Aspect;

import java.util.Objects;

public record AspectHolderComponent(Holder<Aspect> aspect) {

    public static final Codec<AspectHolderComponent> CODEC = RegistryFileCodec.create(ThaumcraftData.Registries.ASPECT, Aspect.CODEC.codec()).xmap(AspectHolderComponent::new, AspectHolderComponent::aspect);
    public static final StreamCodec<RegistryFriendlyByteBuf, AspectHolderComponent> STREAM_CODEC = Aspect.REGISTRY_STREAM_CODEC.map(AspectHolderComponent::new, AspectHolderComponent::aspect);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AspectHolderComponent that)) return false;
        return Objects.equals(aspect, that.aspect);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(aspect);
    }
}
