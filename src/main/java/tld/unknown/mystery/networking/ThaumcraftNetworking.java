package tld.unknown.mystery.networking;

import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.networking.packets.ClientboundAspectRegistrySyncPacket;
import tld.unknown.mystery.registries.ConfigDataRegistries;

@EventBusSubscriber(modid = Thaumcraft.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ThaumcraftNetworking {

    private static final String PROTOCOL = "0.1";

    @SubscribeEvent
    public static void onRegisterPayloadHander(RegisterPayloadHandlersEvent e) {
        final PayloadRegistrar registrar = e.registrar(PROTOCOL);

        registrar.playToClient(ClientboundAspectRegistrySyncPacket.TYPE, ClientboundAspectRegistrySyncPacket.STREAM_CODEC, (data, ctx) -> {
            ctx.enqueueWork(() -> ConfigDataRegistries.ASPECT_REGISTRY.deserialize(data)).exceptionally(error -> {
                ctx.disconnect(Component.translatable("network.thaumcraft.aspect_registry_disconnect"));
                return null;
            });
        });
    }
}
