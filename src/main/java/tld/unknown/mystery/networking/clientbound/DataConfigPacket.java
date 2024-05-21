package tld.unknown.mystery.networking.clientbound;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.data.ThaumcraftData;
import tld.unknown.mystery.networking.Packet;
import tld.unknown.mystery.util.codec.data.CodecDataManager;

@NoArgsConstructor
@AllArgsConstructor
public class DataConfigPacket implements CustomPacketPayload {

    public static final ResourceLocation ID = Thaumcraft.id("data_config");

    private String managerName;
    private CompoundTag tagData;

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return new Type<>(ID);
    }
}
