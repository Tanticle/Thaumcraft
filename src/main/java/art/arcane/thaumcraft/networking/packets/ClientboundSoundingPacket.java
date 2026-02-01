package art.arcane.thaumcraft.networking.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import art.arcane.thaumcraft.api.ThaumcraftData;

public record ClientboundSoundingPacket(BlockPos origin, int level) implements CustomPacketPayload {

    public static final StreamCodec<FriendlyByteBuf, ClientboundSoundingPacket> STREAM_CODEC =
        StreamCodec.composite(
            BlockPos.STREAM_CODEC, ClientboundSoundingPacket::origin,
            ByteBufCodecs.VAR_INT, ClientboundSoundingPacket::level,
            ClientboundSoundingPacket::new
        );

    public static final Type<ClientboundSoundingPacket> TYPE =
        new Type<>(ThaumcraftData.Networking.SOUNDING_SCAN);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
