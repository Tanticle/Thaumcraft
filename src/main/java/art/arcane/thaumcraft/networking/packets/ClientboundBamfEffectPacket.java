package art.arcane.thaumcraft.networking.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import art.arcane.thaumcraft.api.ThaumcraftData;

public record ClientboundBamfEffectPacket(
        BlockPos pos,
        int color,
        boolean playSound,
        boolean flair
) implements CustomPacketPayload {

    public static final StreamCodec<FriendlyByteBuf, ClientboundBamfEffectPacket> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC, ClientboundBamfEffectPacket::pos,
                    ByteBufCodecs.VAR_INT, ClientboundBamfEffectPacket::color,
                    ByteBufCodecs.BOOL, ClientboundBamfEffectPacket::playSound,
                    ByteBufCodecs.BOOL, ClientboundBamfEffectPacket::flair,
                    ClientboundBamfEffectPacket::new
            );

    public static final Type<ClientboundBamfEffectPacket> TYPE =
            new Type<>(ThaumcraftData.Networking.BAMF_EFFECT);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
