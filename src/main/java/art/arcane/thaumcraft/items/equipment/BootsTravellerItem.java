package art.arcane.thaumcraft.items.equipment;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.api.ThaumcraftMaterials;
import art.arcane.thaumcraft.items.VisChargeItem;
import art.arcane.thaumcraft.registries.ConfigItemComponents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.Level;

public class BootsTravellerItem extends VisChargeItem {

	private static final int MAX_CHARGE = 240;
	private static final int TICKS_PER_CHARGE = 60;

	private AttributeModifier MODIFIER_STEP_HEIGHT = new AttributeModifier(Thaumcraft.id("step_height"), 0.5F, AttributeModifier.Operation.ADD_VALUE);

	public BootsTravellerItem(Properties p) {
		super(MAX_CHARGE, ThaumcraftMaterials.Armor.TRAVELLER.humanoidProperties(p, ArmorType.BOOTS));
	}

	//TODO: Is there a way to avoid the zoom-in when giving the speed effect?
	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
		if(!level.isClientSide() && entity instanceof Player p && slotId == 36) {
			int itemCharge = ((VisChargeItem)stack.getItem()).getCharge(stack);
			if(checkTimer(stack) > 0 || itemCharge > 0) {
				p.addEffect(new MobEffectInstance(MobEffects.JUMP, 1, 2, false, false, false));
				p.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1, 1, false, false, false));
				stack.set(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.builder().add(Attributes.STEP_HEIGHT, MODIFIER_STEP_HEIGHT, EquipmentSlotGroup.FEET).build());
				if(p.tickCount % 20 == 0 && decrementTimer(stack) <= 0) {
					if(itemCharge > 0) {
						if(((VisChargeItem)stack.getItem()).drainCharge(stack, 1) > 0) {
							setTimer(stack, TICKS_PER_CHARGE);
						} else {
							stack.remove(DataComponents.ATTRIBUTE_MODIFIERS);
						}
					}
				}
			}
		}
	}



	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return false;
	}

	public int checkTimer(ItemStack stack) {
		return stack.getOrDefault(ConfigItemComponents.TIMER.value(), 0);
	}

	public int setTimer(ItemStack stack, int amount) {
		int value = Math.max(amount, 0);
		stack.set(ConfigItemComponents.TIMER.value(), value);
		return value;
	}

	public int decrementTimer(ItemStack stack) {
		return setTimer(stack, Math.max(0, checkTimer(stack) - 1));
	}
}
