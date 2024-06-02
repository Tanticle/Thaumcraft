package tld.unknown.mystery.networking;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import tld.unknown.mystery.Thaumcraft;

@EventBusSubscriber(modid = Thaumcraft.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ThaumcraftNetworking {

    private static final String PROTOCOL = "0.1";

    @SubscribeEvent
    public static void onRegisterPayloadHander(RegisterPayloadHandlersEvent e) {}
}
