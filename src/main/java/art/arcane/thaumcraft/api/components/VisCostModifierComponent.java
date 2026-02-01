package art.arcane.thaumcraft.api.components;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record VisCostModifierComponent(float modifier) {
    public static final Codec<VisCostModifierComponent> CODEC = Codec.FLOAT.xmap(VisCostModifierComponent::new, VisCostModifierComponent::modifier);
    public static final StreamCodec<ByteBuf, VisCostModifierComponent> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);
}
