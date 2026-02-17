package art.arcane.thaumcraft.events.client.handlers;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.registries.ConfigItems;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import tld.unknown.baubles.api.BaublesEvent;

@EventBusSubscriber(modid = Thaumcraft.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class MiscEvents {

	@SubscribeEvent
	public static void onBaubleRendererRegister(BaublesEvent.RendererRegistration event) {
		event.registerRenderer(ThaumcraftData.Items.GOGGLES, ConfigItems.GOGGLES.get());
	}
}
