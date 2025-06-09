package tld.unknown.mystery.client;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.client.rendering.debug.TubeDebugRenderer;

@EventBusSubscriber(modid = Thaumcraft.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ThaumcraftClient {

    public static TubeDebugRenderer TUBE_DEBUG_RENDERER;

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        TUBE_DEBUG_RENDERER = new TubeDebugRenderer(Minecraft.getInstance());
    }
}
