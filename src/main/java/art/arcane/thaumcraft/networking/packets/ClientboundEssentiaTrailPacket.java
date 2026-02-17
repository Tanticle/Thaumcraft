package art.arcane.thaumcraft.networking.packets;

import art.arcane.thaumcraft.api.ThaumcraftData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.phys.Vec3;

public record ClientboundEssentiaTrailPacket(
        Vec3 from,
        Vec3 to,
        int color,
        int ext
) implements CustomPacketPayload {

    public static final StreamCodec<FriendlyByteBuf, ClientboundEssentiaTrailPacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.fromCodec(Vec3.CODEC), ClientboundEssentiaTrailPacket::from,
                    ByteBufCodecs.fromCodec(Vec3.CODEC), ClientboundEssentiaTrailPacket::to,
                    ByteBufCodecs.VAR_INT, ClientboundEssentiaTrailPacket::color,
                    ByteBufCodecs.VAR_INT, ClientboundEssentiaTrailPacket::ext,
                    ClientboundEssentiaTrailPacket::new
            );

    public static final Type<ClientboundEssentiaTrailPacket> TYPE =
            new Type<>(ThaumcraftData.Networking.ESSENTIA_TRAIL);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
