package art.arcane.thaumcraft.api.components;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record WarpingComponent(int level) {
    public static final Codec<WarpingComponent> CODEC = Codec.BYTE.xmap(i -> new WarpingComponent((byte)i), c -> (byte)c.level());
    public static StreamCodec<ByteBuf, WarpingComponent> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);
}
