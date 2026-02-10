package art.arcane.thaumcraft.networking.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import art.arcane.thaumcraft.api.ThaumcraftData;

public record ClientboundSealSyncPacket(
        BlockPos pos,
        Direction face,
        ResourceLocation sealType,
        byte color,
        byte priority,
        int areaX,
        int areaY,
        int areaZ
) implements CustomPacketPayload {

    public static final StreamCodec<FriendlyByteBuf, ClientboundSealSyncPacket> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC, ClientboundSealSyncPacket::pos,
                    Direction.STREAM_CODEC, ClientboundSealSyncPacket::face,
                    ResourceLocation.STREAM_CODEC, ClientboundSealSyncPacket::sealType,
                    ByteBufCodecs.BYTE, ClientboundSealSyncPacket::color,
                    ByteBufCodecs.BYTE, ClientboundSealSyncPacket::priority,
                    ByteBufCodecs.VAR_INT, ClientboundSealSyncPacket::areaX,
                    ByteBufCodecs.VAR_INT, ClientboundSealSyncPacket::areaY,
                    ByteBufCodecs.VAR_INT, ClientboundSealSyncPacket::areaZ,
                    ClientboundSealSyncPacket::new
            );

    public static final Type<ClientboundSealSyncPacket> TYPE =
            new Type<>(ThaumcraftData.Networking.SEAL_SYNC);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
