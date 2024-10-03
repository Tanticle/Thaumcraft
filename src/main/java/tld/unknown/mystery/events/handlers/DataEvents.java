package tld.unknown.mystery.events.handlers;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.registries.ConfigDataRegistries;

@EventBusSubscriber(modid = Thaumcraft.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class DataEvents {

    @SubscribeEvent
    public static void onDataSyncEvent(OnDatapackSyncEvent event) {
        event.getRelevantPlayers().forEach(p -> p.connection.send(ConfigDataRegistries.ASPECT_REGISTRY.serialize()));
    }

    @SubscribeEvent
    public static void onReloadListenerEvent(AddReloadListenerEvent event) {
        event.addListener(ConfigDataRegistries.ASPECT_REGISTRY);
    }
}
