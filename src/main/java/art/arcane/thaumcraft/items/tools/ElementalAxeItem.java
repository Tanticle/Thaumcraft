package art.arcane.thaumcraft.items.tools;

import art.arcane.thaumcraft.api.ThaumcraftMaterials;
import art.arcane.thaumcraft.api.enums.InfusionEnchantments;
import art.arcane.thaumcraft.api.components.InfusionEnchantmentComponent;
import art.arcane.thaumcraft.registries.ConfigItemComponents;
import art.arcane.thaumcraft.util.EntityUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Map;

// TODO: AXE Bubbles look like ass lmao, all jumpy. should be a trail. and magnet should pull UP as well, to properly get blocks stuck (also axe items should float through other blocks.
public class ElementalAxeItem extends AxeItem {

	private static final int MAX_USE_DURATION = 72000;

	public ElementalAxeItem(Properties props) {
		super(ThaumcraftMaterials.Tools.ELEMENTAL, ThaumcraftMaterials.Tools.ELEMENTAL.attackDamageBonus(), ThaumcraftMaterials.Tools.ELEMENTAL.speed(),
				props.rarity(Rarity.RARE).component(
						ConfigItemComponents.INFUSION_ENCHANTMENT.value(), new InfusionEnchantmentComponent(Map.of(
								InfusionEnchantments.COLLECTOR, (byte) 1,
								InfusionEnchantments.BURROWING, (byte) 1))));
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		player.startUsingItem(hand);
		return InteractionResult.CONSUME;
	}

	@Override
	public ItemUseAnimation getUseAnimation(ItemStack stack) {
		return ItemUseAnimation.BOW;
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity entity) {
		return MAX_USE_DURATION;
	}

	@Override
	public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
		List<Entity> stuff = EntityUtils.getEntitiesInRange(pLivingEntity.level(), pLivingEntity.position(), 15.0F, pLivingEntity, EntityType.ITEM);
		stuff.forEach(e -> {
			if (e.isAlive()) {
				double d6 = e.getX() - pLivingEntity.getX();
				double d7 = e.getY() - pLivingEntity.getY() + pLivingEntity.getEyeHeight() / 2.0f;
				double d8 = e.getZ() - pLivingEntity.getZ();
				double d9 = Math.sqrt(d6 * d6 + d7 * d7 + d8 * d8);
				d6 /= d9;
				d7 /= d9;
				d8 /= d9;
				double d10 = 0.4;
				double vX = Mth.clamp(e.getDeltaMovement().x() - d6 * d10, -0.35D, 0.35D);
				double vY = Mth.clamp(e.getDeltaMovement().y() - d7 * d10 - 0.1, -0.35D, 0.35D);
				double vZ = Mth.clamp(e.getDeltaMovement().z() - d8 * d10, -0.35D, 0.35D);
				e.setDeltaMovement(vX, vY, vZ);
				if (pLevel.isClientSide()) {
					RandomSource rand = pLevel.getRandom();
					pLevel.addParticle(ParticleTypes.WITCH,
							e.getX() + (rand.nextFloat() - rand.nextFloat()) * 0.3f,
							e.getY() + rand.nextFloat() * 0.5f,
							e.getZ() + (rand.nextFloat() - rand.nextFloat()) * 0.3f,
							(rand.nextFloat() - 0.5f) * 0.1f,
							rand.nextFloat() * 0.05f,
							(rand.nextFloat() - 0.5f) * 0.1f);
				}
			}
		});
	}
}
