package tld.unknown.mystery.util.codec;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.Registry;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public final class StreamCodecs {

    public static <T> StreamCodec<ByteBuf, TagKey<T>> tagKey(ResourceKey<? extends Registry<T>> key) {
        return ResourceLocation.STREAM_CODEC.map(rl -> TagKey.create(key, rl), TagKey::location);
    }
}
