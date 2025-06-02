package tld.unknown.mystery.events.client.handlers;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.client.ThaumcraftClient;
import tld.unknown.mystery.registries.client.ConfigKeybinds;

@EventBusSubscriber(modid = Thaumcraft.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public class KeyEvents {

    @SubscribeEvent
    public static void onKeyPressed(ClientTickEvent.Pre event) {
        if(ConfigKeybinds.TUBE_DEBUG_RENDERER.isDown())
            ThaumcraftClient.TUBE_DEBUG_RENDERER.active = !ThaumcraftClient.TUBE_DEBUG_RENDERER.active;
    }
}
