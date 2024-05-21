package tld.unknown.mystery.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.networking.clientbound.DataConfigPacket;

import java.util.function.Supplier;

@EventBusSubscriber(modid = Thaumcraft.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ThaumcraftNetworking {

    private static final String PROTOCOL = "0.1";

    @SubscribeEvent
    public static void onRegisterPayloadHander(RegisterPayloadHandlersEvent e) {
        PayloadRegistrar handler = e.registrar(Thaumcraft.MOD_ID).versioned(PROTOCOL);

        handler.configurationToClient()
        registerPacket(handler, DataConfigPacket.ID, Packet.NetworkStage.CONFIG, DataConfigPacket::new);
    }

    private static <P extends Packet> void registerPacket(PayloadRegistrar registrar, ResourceLocation id, Packet.NetworkStage stage, Packet.NetworkDirection direction, Supplier<P> factory) {
        FriendlyByteBuf packetCreation = buffer -> {
            P p = factory.get();
            p.read(buffer);
            return p;
        };
        switch(stage) {
            case COMMON -> registrar.common(id, packetCreation, h -> h.client(ThaumcraftNetworking::handleServer).client(ThaumcraftNetworking::handleClient));
            case PLAY -> registrar.play(id, packetCreation, h -> h.client(ThaumcraftNetworking::handleServer).client(ThaumcraftNetworking::handleClient));
            case CONFIG -> registrar.configuration(id, packetCreation, h -> h.client(ThaumcraftNetworking::handleServer).client(ThaumcraftNetworking::handleClient));
        }
    }

    private static <P extends Packet> void handleClient(P packet, IPayloadContext ctx) {
        if(packet.direction() == Packet.NetworkDirection.CLIENTBOUND) {
            ctx.workHandler().submitAsync(() -> packet.handle(ctx)).exceptionally(e -> {
                ctx.packetHandler().disconnect(Component.literal("Networking broke: " + e.getClass().getCanonicalName() + " | " + e.getMessage()));
                return null;
            });
        }
    }

    private static <P extends Packet> void handleServer(P packet, IPayloadContext ctx) {
        if(packet.direction() == Packet.NetworkDirection.SERVERBOUND) {
            try {
                packet.handle(ctx);
            } catch(Exception e) {
                ctx.packetHandler().disconnect(Component.literal("Networking broke: " + e.getClass().getCanonicalName() + " | " + e.getMessage()));
            }
        }
    }
}
