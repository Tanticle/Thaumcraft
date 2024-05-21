package tld.unknown.mystery.items.components;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public record AspectHolderComponent(ResourceLocation aspect) {
    public static final Codec<AspectHolderComponent> CODEC = ResourceLocation.CODEC.xmap(AspectHolderComponent::new, AspectHolderComponent::aspect);
    public static final StreamCodec<ByteBuf, AspectHolderComponent> STREAM_CODEC = ResourceLocation.STREAM_CODEC.map(AspectHolderComponent::new, AspectHolderComponent::aspect);

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
