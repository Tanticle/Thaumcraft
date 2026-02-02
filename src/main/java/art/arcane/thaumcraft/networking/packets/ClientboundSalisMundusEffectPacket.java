package art.arcane.thaumcraft.networking.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.phys.Vec3;
import art.arcane.thaumcraft.api.ThaumcraftData;

import java.util.List;

public record ClientboundSalisMundusEffectPacket(
        BlockPos target,
        Vec3 hitPos,
        Vec3 handPos,
        List<BlockPos> sparklePositions
) implements CustomPacketPayload {

    private static final StreamCodec<FriendlyByteBuf, Vec3> VEC3_CODEC = StreamCodec.of(
            (buf, vec) -> {
                buf.writeDouble(vec.x);
                buf.writeDouble(vec.y);
                buf.writeDouble(vec.z);
            },
            buf -> new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble())
    );

    public static final StreamCodec<FriendlyByteBuf, ClientboundSalisMundusEffectPacket> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC, ClientboundSalisMundusEffectPacket::target,
                    VEC3_CODEC, ClientboundSalisMundusEffectPacket::hitPos,
                    VEC3_CODEC, ClientboundSalisMundusEffectPacket::handPos,
                    BlockPos.STREAM_CODEC.apply(ByteBufCodecs.list()), ClientboundSalisMundusEffectPacket::sparklePositions,
                    ClientboundSalisMundusEffectPacket::new
            );

    public static final Type<ClientboundSalisMundusEffectPacket> TYPE =
            new Type<>(ThaumcraftData.Networking.SALIS_MUNDUS_EFFECT);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
