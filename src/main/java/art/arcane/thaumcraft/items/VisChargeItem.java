package art.arcane.thaumcraft.items;

import art.arcane.thaumcraft.registries.ConfigItemComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class VisChargeItem extends Item {

	public VisChargeItem(int maxCharge, Properties properties) {
		super(properties.component(ConfigItemComponents.VIS_CHARGE_MAX.value(), maxCharge));
	}

	public int drainCharge(ItemStack stack, int amount) {
		if(!stack.has(ConfigItemComponents.VIS_CHARGE_MAX.value()))
			return 0;

		int currentCharge = stack.getOrDefault(ConfigItemComponents.VIS_CHARGE.value(), 0);
		int newCharge = Math.max(0, currentCharge - amount);
		stack.set(ConfigItemComponents.VIS_CHARGE.value(), newCharge);
		return newCharge;
	}

	public void addCharge(ItemStack stack, int amount) {
		if(!stack.has(ConfigItemComponents.VIS_CHARGE_MAX.value()))
			return;

		int maxCharge = stack.get(ConfigItemComponents.VIS_CHARGE_MAX.value());
		int currentCharge = stack.getOrDefault(ConfigItemComponents.VIS_CHARGE.value(), 0);
		stack.set(ConfigItemComponents.VIS_CHARGE.value(), Math.min(maxCharge, currentCharge + amount));
	}

	public int getCharge(ItemStack stack) {
		return stack.getOrDefault(ConfigItemComponents.VIS_CHARGE.value(), 0);
	}

	public float getChargePercentage(ItemStack stack) {
		if(!stack.has(ConfigItemComponents.VIS_CHARGE_MAX.value()))
			return 0.0F;

		int maxCharge = stack.get(ConfigItemComponents.VIS_CHARGE_MAX.value());
		return (float)(getCharge(stack) / maxCharge);
	}
}
