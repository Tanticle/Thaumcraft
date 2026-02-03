package art.arcane.thaumcraft.networking.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.phys.Vec3;
import art.arcane.thaumcraft.api.ThaumcraftData;

public record ClientboundBamfEffectPacket(
        Vec3 pos,
        int color,
        boolean playSound,
        boolean flair
) implements CustomPacketPayload {

    public ClientboundBamfEffectPacket(BlockPos blockPos, int color, boolean playSound, boolean flair) {
        this(Vec3.atCenterOf(blockPos), color, playSound, flair);
    }

    public static final StreamCodec<FriendlyByteBuf, ClientboundBamfEffectPacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.fromCodec(Vec3.CODEC), ClientboundBamfEffectPacket::pos,
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
