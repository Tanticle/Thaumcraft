package art.arcane.thaumcraft.api.components;

import art.arcane.thaumcraft.util.codec.Codecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record FortressFaceplateComponent(Type type, boolean hasGoggles) {

	public static final Codec<FortressFaceplateComponent> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codecs.enumCodec(Type.class).fieldOf("mask").forGetter(FortressFaceplateComponent::type),
			Codec.BOOL.fieldOf("goggles").forGetter(FortressFaceplateComponent::hasGoggles)
	).apply(i, FortressFaceplateComponent::new));

	public static final StreamCodec<ByteBuf, FortressFaceplateComponent> STREAM_CODEC = StreamCodec.composite(Codecs.enumStreamCodec(Type.class), FortressFaceplateComponent::type, ByteBufCodecs.BOOL, FortressFaceplateComponent::hasGoggles, FortressFaceplateComponent::new);

	@Getter
	@AllArgsConstructor
	public enum Type {
		NONE("none"),
		DEVIL("mask_devil"),
		GHOST("mask_ghost"),
		FIEND("mask_fiend");

		private final String modelPart;
	}
}
