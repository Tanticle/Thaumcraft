package tld.unknown.mystery.networking.clientbound;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import tld.unknown.mystery.Thaumcraft;

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
