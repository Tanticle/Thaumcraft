package tld.unknown.mystery.items.components;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.StreamCodec;

import java.util.Objects;
import java.util.UUID;

public record CollectorMarkerComponent(UUID target) {

    public static final Codec<CollectorMarkerComponent> CODEC = UUIDUtil.CODEC.xmap(CollectorMarkerComponent::new, CollectorMarkerComponent::target);
    public static final StreamCodec<ByteBuf, CollectorMarkerComponent> STREAM_CODEC = UUIDUtil.STREAM_CODEC.map(CollectorMarkerComponent::new, CollectorMarkerComponent::target);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CollectorMarkerComponent that)) return false;
        return Objects.equals(target, that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(target);
    }
}
