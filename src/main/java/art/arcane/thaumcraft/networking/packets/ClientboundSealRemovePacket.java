package art.arcane.thaumcraft.networking.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import art.arcane.thaumcraft.api.ThaumcraftData;

public record ClientboundSealRemovePacket(
        BlockPos pos,
        Direction face
) implements CustomPacketPayload {

    public static final StreamCodec<FriendlyByteBuf, ClientboundSealRemovePacket> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC, ClientboundSealRemovePacket::pos,
                    Direction.STREAM_CODEC, ClientboundSealRemovePacket::face,
                    ClientboundSealRemovePacket::new
            );

    public static final Type<ClientboundSealRemovePacket> TYPE =
            new Type<>(ThaumcraftData.Networking.SEAL_REMOVE);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
