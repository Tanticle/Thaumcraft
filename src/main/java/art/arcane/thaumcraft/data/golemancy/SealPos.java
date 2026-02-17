package art.arcane.thaumcraft.data.golemancy;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record SealPos(BlockPos pos, Direction face) {

    public static final Codec<SealPos> CODEC = RecordCodecBuilder.create(i -> i.group(
            BlockPos.CODEC.fieldOf("pos").forGetter(SealPos::pos),
            Direction.CODEC.fieldOf("face").forGetter(SealPos::face)
    ).apply(i, SealPos::new));

    public static final StreamCodec<FriendlyByteBuf, SealPos> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, SealPos::pos,
            Direction.STREAM_CODEC, SealPos::face,
            SealPos::new
    );
}
