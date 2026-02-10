package art.arcane.thaumcraft.api;

import art.arcane.thaumcraft.registries.ConfigItemComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import tld.unknown.baubles.api.BaubleType;
import tld.unknown.baubles.api.Baubles;

public final class ThaumcraftUtils {

    public static boolean shouldShowAspects() {
        Minecraft mc = Minecraft.getInstance();
        return mc.screen != null && mc.level != null && mc.options.keyShift.isDown();
    }

	public static boolean playerHasGoggleSight(Player player) {
		return player.getInventory().getArmor(0).has(ConfigItemComponents.GOGGLE_SIGHT.value()) ||
				Baubles.API.getBaublesInventory(player).getBaubleInSlot(BaubleType.HEAD).has(ConfigItemComponents.GOGGLE_SIGHT.value()) ||
				player.getMainHandItem().has(ConfigItemComponents.GOGGLE_SIGHT.value()) ||
				player.getOffhandItem().has(ConfigItemComponents.GOGGLE_SIGHT.value());
	}
}
