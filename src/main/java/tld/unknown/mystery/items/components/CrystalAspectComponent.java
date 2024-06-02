package tld.unknown.mystery.items.components;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import tld.unknown.mystery.blocks.CrystalBlock;

public record CrystalAspectComponent(CrystalBlock.CrystalAspect value) {
    public static Codec<CrystalAspectComponent> CODEC = StringRepresentable.fromEnum(CrystalBlock.CrystalAspect::values).xmap(CrystalAspectComponent::new, CrystalAspectComponent::value);
    public static StreamCodec<ByteBuf, CrystalAspectComponent> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);
}
