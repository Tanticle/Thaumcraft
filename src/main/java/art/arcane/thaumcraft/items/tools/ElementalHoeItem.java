package art.arcane.thaumcraft.items.tools;

import art.arcane.thaumcraft.api.ThaumcraftMaterials;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.common.ItemAbilities;

public class ElementalHoeItem extends HoeItem {

	public ElementalHoeItem(Properties props) {
		super(ThaumcraftMaterials.Tools.ELEMENTAL, ThaumcraftMaterials.Tools.ELEMENTAL.attackDamageBonus(), ThaumcraftMaterials.Tools.ELEMENTAL.speed(), props.rarity(Rarity.RARE));
	}

	@Override
	public InteractionResult useOn(UseOnContext pContext) {
		if (pContext.getPlayer() != null && pContext.getPlayer().isCrouching()) {
			return super.useOn(pContext);
		}

		Level level = pContext.getLevel();
		BlockPos clickedPos = pContext.getClickedPos();
		Player player = pContext.getPlayer();
		ItemStack stack = pContext.getItemInHand();
		boolean didTill = false;

		for (int xx = -1; xx <= 1; ++xx) {
			for (int zz = -1; zz <= 1; ++zz) {
				BlockPos targetPos = clickedPos.offset(xx, 0, zz);
				if (tryTillBlock(level, targetPos, pContext.getClickedFace(), player, stack)) {
					didTill = true;
				}
			}
		}

		if (!didTill) {
			int bonemealCount = 0;
			for (int xx = -1; xx <= 1; ++xx) {
				for (int zz = -1; zz <= 1; ++zz) {
					BlockPos targetPos = clickedPos.offset(xx, 0, zz);
					if (BoneMealItem.applyBonemeal(new ItemStack(Items.BONE_MEAL), level, targetPos, player)) {
						bonemealCount++;
						if (!level.isClientSide()) {
							spawnBonemealParticles((ServerLevel) level, targetPos);
						}
					}
				}
			}
			if (bonemealCount > 0) {
				didTill = true;
				stack.hurtAndBreak(bonemealCount, player, player.getEquipmentSlotForItem(stack));
				level.playSound(null, clickedPos, SoundEvents.BONE_MEAL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
			}
		}

		return didTill ? InteractionResult.SUCCESS : InteractionResult.PASS;
	}

	private boolean tryTillBlock(Level level, BlockPos pos, Direction face, Player player, ItemStack stack) {
		BlockState state = level.getBlockState(pos);
		BlockState tilledState = state.getToolModifiedState(
				new UseOnContext(level, player, player.getUsedItemHand(), stack,
						new net.minecraft.world.phys.BlockHitResult(
								pos.getCenter(), face, pos, false)),
				ItemAbilities.HOE_TILL, false);

		if (tilledState == null) {
			return false;
		}

		BlockPos above = pos.above();
		BlockState aboveState = level.getBlockState(above);
		if (!aboveState.isAir()) {
			return false;
		}

		if (!level.isClientSide()) {
			level.setBlock(pos, tilledState, 11);
			level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, tilledState));
			stack.hurtAndBreak(1, player, player.getEquipmentSlotForItem(stack));
			spawnTillParticles((ServerLevel) level, pos);
		}
		level.playSound(player, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
		return true;
	}

	private void spawnTillParticles(ServerLevel level, BlockPos pos) {
		double x = pos.getX() + 0.5;
		double y = pos.getY() + 1.01;
		double z = pos.getZ() + 0.5;
		level.sendParticles(ParticleTypes.HAPPY_VILLAGER, x, y, z, 2, 0.25, 0.1, 0.25, 0.01);
	}

	private void spawnBonemealParticles(ServerLevel level, BlockPos pos) {
		level.sendParticles(ParticleTypes.HAPPY_VILLAGER,
				pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
				5, 0.5, 0.5, 0.5, 0.01);
	}
}
