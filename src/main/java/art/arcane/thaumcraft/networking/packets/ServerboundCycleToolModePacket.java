package art.arcane.thaumcraft.networking.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import art.arcane.thaumcraft.api.ThaumcraftData;

public record ServerboundCycleToolModePacket() implements CustomPacketPayload {

    public static final StreamCodec<FriendlyByteBuf, ServerboundCycleToolModePacket> STREAM_CODEC =
        StreamCodec.unit(new ServerboundCycleToolModePacket());

    public static final Type<ServerboundCycleToolModePacket> TYPE =
        new Type<>(ThaumcraftData.Networking.CYCLE_TOOL_MODE);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
