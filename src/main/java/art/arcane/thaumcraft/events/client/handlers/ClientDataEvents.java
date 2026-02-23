package art.arcane.thaumcraft.events.client.handlers;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.client.ThaumcraftClientRecipes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;

@EventBusSubscriber(modid = Thaumcraft.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public final class ClientDataEvents {

	@SubscribeEvent
	public static void onRecipesReceivedEvent(RecipesReceivedEvent event) {
		ThaumcraftClientRecipes.setRecipeMap(event.getRecipeMap());
	}
}
