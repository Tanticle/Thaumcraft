package art.arcane.thaumcraft.events.handlers;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.data.aspects.fallback.AspectFallbackEngine;
import art.arcane.thaumcraft.registries.ConfigDataRegistries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;

@EventBusSubscriber(modid = Thaumcraft.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class DataEvents {

    @SubscribeEvent
    public static void onDataSyncEvent(OnDatapackSyncEvent event) {
        event.getRelevantPlayers().forEach(p -> p.connection.send(ConfigDataRegistries.ASPECT_REGISTRY.serialize()));
    }

    @SubscribeEvent
    public static void onReloadListenerEvent(AddServerReloadListenersEvent event) {
        event.addListener(ThaumcraftData.Registries.ASPECT_REGISTRY.location(), ConfigDataRegistries.ASPECT_REGISTRY);
    }

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        AspectFallbackEngine.initialize(
                event.getServer().getRecipeManager(),
                stack -> ConfigDataRegistries.ASPECT_REGISTRY.getRegisteredAspects(stack)
        );
        ConfigDataRegistries.ASPECT_REGISTRY.printUnaccounted();
    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        AspectFallbackEngine.clear();
    }
}
