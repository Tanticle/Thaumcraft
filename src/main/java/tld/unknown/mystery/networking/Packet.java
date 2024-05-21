package tld.unknown.mystery.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public interface Packet extends CustomPacketPayload {

    void read(FriendlyByteBuf buffer);
    void handle(IPayloadContext ctx);

    NetworkDirection direction();

    enum NetworkDirection { CLIENTBOUND, SERVERBOUND }
    enum NetworkStage { CONFIG, PLAY, COMMON }
}
