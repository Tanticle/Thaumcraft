package art.arcane.thaumcraft.api;

import art.arcane.thaumcraft.api.aspects.Aspect;
import art.arcane.thaumcraft.api.capabilities.IEssentiaCapability;
import art.arcane.thaumcraft.registries.ConfigCapabilities;
import art.arcane.thaumcraft.registries.ConfigItemComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import tld.unknown.baubles.api.BaubleType;
import tld.unknown.baubles.api.Baubles;

public final class ThaumcraftUtils {

    public static boolean shouldShowAspects() {
        Minecraft mc = Minecraft.getInstance();
        return mc.screen != null && mc.level != null && mc.options.keyShift.isDown();
    }

	public static boolean playerHasGoggleSight(Player player) {
		return player.getInventory().getArmor(3).has(ConfigItemComponents.GOGGLE_SIGHT.value()) ||
				Baubles.API.getBaublesInventory(player).getBaubleInSlot(BaubleType.HEAD).has(ConfigItemComponents.GOGGLE_SIGHT.value()) ||
				player.getMainHandItem().has(ConfigItemComponents.GOGGLE_SIGHT.value()) ||
				player.getOffhandItem().has(ConfigItemComponents.GOGGLE_SIGHT.value());
	}

	public static boolean drainClosestEssentiaSource(Level level, BlockPos pos, int range, ResourceKey<Aspect> aspect, int amount, Direction direction) {
		if(direction == null)
			direction = Direction.UP;

		IEssentiaCapability closest = null;
		double closestDistance = Double.MAX_VALUE;
		for(int y = -range; y < range; y++) {
			for(int x = -range; x < range; x++) {
				for(int z = -range; z < range; z++) {
					BlockPos offset = pos.offset(x, y, z);
					IEssentiaCapability capability = level.getCapability(ConfigCapabilities.ESSENTIA, offset, direction);
					if(capability != null && capability.contains(aspect, amount, direction)) {
						double distance = pos.distSqr(offset);
						if(distance < closestDistance) {
							closestDistance = distance;
							closest = capability;
						}
					}
				}
			}
		}

		if(closest == null)
			return false;

		closest.drainAspect(aspect, amount, direction);
		return true;
	}
}
