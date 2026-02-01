package art.arcane.thaumcraft.items.tools;

import art.arcane.thaumcraft.api.IArchitect;
import art.arcane.thaumcraft.api.ThaumcraftMaterials;
import art.arcane.thaumcraft.api.enums.InfusionEnchantments;
import art.arcane.thaumcraft.items.components.InfusionEnchantmentComponent;
import art.arcane.thaumcraft.registries.ConfigItemComponents;
import art.arcane.thaumcraft.util.EntityUtils;
import art.arcane.thaumcraft.util.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.Map;

public class ElementalShovelItem extends ShovelItem implements IArchitect {

	public ElementalShovelItem(Properties properties) {
		super(ThaumcraftMaterials.Tools.ELEMENTAL,
				ThaumcraftMaterials.Tools.ELEMENTAL.attackDamageBonus(), ThaumcraftMaterials.Tools.ELEMENTAL.speed(),
				properties.rarity(Rarity.RARE).component(
						ConfigItemComponents.INFUSION_ENCHANTMENT.value(), new InfusionEnchantmentComponent(Map.of(
								InfusionEnchantments.DESTRUCTIVE, (byte) 1))));
	}

	public static Direction.Axis getOrientation(ItemStack stack) {
		return stack.has(ConfigItemComponents.AXIS.value()) ? stack.get(ConfigItemComponents.AXIS.value()) : Direction.Axis.X;
	}

	public static void setOrientation(ItemStack stack, Direction.Axis axis) {
		stack.set(ConfigItemComponents.AXIS.value(), axis);
	}

	private static void spawnPlacementParticles(Level level, BlockPos pos) {
		if (level.isClientSide()) {
			Vec3 center = pos.getCenter();
			for (int i = 0; i < 5; i++) {
				level.addParticle(ParticleTypes.SOUL, // TODO: Make this that thaumic POOF partical
						center.x + (level.getRandom().nextFloat() - 0.5) * 0.5,
						center.y + (level.getRandom().nextFloat() - 0.5) * 0.5,
						center.z + (level.getRandom().nextFloat() - 0.5) * 0.5,
						0, 0.05, 0);
			}
		}
	}

	@Override
	public InteractionResult useOn(UseOnContext pContext) {
		if (!pContext.getPlayer().isCrouching()) {
			return InteractionResult.PASS;
		}

		BlockState bs = pContext.getLevel().getBlockState(pContext.getClickedPos());
		BlockEntity te = pContext.getLevel().getBlockEntity(pContext.getClickedPos());
		if (te != null) {
			return InteractionResult.PASS;
		}

		Direction side = pContext.getClickedFace();
		boolean placedAny = false;

		for (int aa = -1; aa <= 1; ++aa) {
			for (int bb = -1; bb <= 1; ++bb) {
				int xx = 0;
				int yy = 0;
				int zz = 0;
				Direction.Axis o = getOrientation(pContext.getItemInHand());
				if (o == Direction.Axis.Y) {
					yy = bb;
					if (side.ordinal() <= 1) {
						int l = Mth.floor(pContext.getPlayer().getYRot() * 4.0f / 360.0f + 0.5) & 0x3;
						if (l == 0 || l == 2) {
							xx = aa;
						} else {
							zz = aa;
						}
					} else if (side.ordinal() <= 3) {
						zz = aa;
					} else {
						xx = aa;
					}
				} else if (o == Direction.Axis.Z) {
					if (side.ordinal() <= 1) {
						int l = Mth.floor(pContext.getPlayer().getYRot() * 4.0f / 360.0f + 0.5) & 0x3;
						yy = bb;
						if (l == 0 || l == 2) {
							xx = aa;
						} else {
							zz = aa;
						}
					} else {
						zz = bb;
						xx = aa;
					}
				} else if (side.ordinal() <= 1) {
					xx = aa;
					zz = bb;
				} else if (side.ordinal() <= 3) {
					xx = aa;
					yy = bb;
				} else {
					zz = aa;
					yy = bb;
				}
				BlockPos p2 = pContext.getClickedPos().offset(side.getUnitVec3i()).offset(xx, yy, zz);
				BlockState existingState = pContext.getLevel().getBlockState(p2);

				if (!existingState.canBeReplaced()) {
					continue;
				}

				BlockState toPlace = bs;
				if (bs.getBlock() == Blocks.GRASS_BLOCK) {
					toPlace = Blocks.DIRT.defaultBlockState();
				}

				if (!toPlace.canSurvive(pContext.getLevel(), p2)) {
					continue;
				}

				boolean consumed = pContext.getPlayer().isCreative();
				if (!consumed) {
					if (bs.getBlock() == Blocks.GRASS_BLOCK) {
						consumed = ItemUtils.consumeItem(pContext.getPlayer(), Blocks.DIRT.asItem());
					} else {
						consumed = ItemUtils.consumeItem(pContext.getPlayer(), bs.getBlock().asItem());
					}
				}

				if (consumed) {
					pContext.getLevel().setBlock(p2, toPlace, Block.UPDATE_ALL);
					pContext.getLevel().playSound(pContext.getPlayer(), p2, toPlace.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 0.6f, 0.9f + pContext.getLevel().getRandom().nextFloat() * 0.2f);
					spawnPlacementParticles(pContext.getLevel(), p2);
					placedAny = true;

					if (!pContext.getPlayer().isCreative()) {
						pContext.getItemInHand().hurtAndBreak(1, pContext.getPlayer(), pContext.getPlayer().getEquipmentSlotForItem(pContext.getItemInHand()));
					}
				}
			}
		}

		if (placedAny) {
			pContext.getPlayer().swing(pContext.getHand());
			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}

	@Override
	public boolean useBlockHighlight(ItemStack stack) {
		return true;
	}

	@Override
	public BlockHitResult getArchitectMOP(ItemStack stack, Level world, LivingEntity player) {
		return EntityUtils.rayTrace(world, player, false);
	}

	@Override
	public List<BlockPos> getArchitectBlocks(ItemStack stack, Level world, BlockPos pos, Direction side, Player player) {
		List<BlockPos> b = Lists.newArrayList();
		if (!player.isCrouching()) {
			return b;
		}
		BlockState bs = world.getBlockState(pos);
		for (int aa = -1; aa <= 1; ++aa) {
			for (int bb = -1; bb <= 1; ++bb) {
				int xx = 0;
				int yy = 0;
				int zz = 0;
				Direction.Axis o = getOrientation(stack);
				if (o == Direction.Axis.Y) {
					yy = bb;
					if (side.ordinal() <= 1) {
						int l = Mth.floor(player.getYRot() * 4.0f / 360.0f + 0.5) & 0x3;
						if (l == 0 || l == 2) {
							xx = aa;
						} else {
							zz = aa;
						}
					} else if (side.ordinal() <= 3) {
						zz = aa;
					} else {
						xx = aa;
					}
				} else if (o == Direction.Axis.Z) {
					if (side.ordinal() <= 1) {
						int l = Mth.floor(player.getYRot() * 4.0f / 360.0f + 0.5) & 0x3;
						yy = bb;
						if (l == 0 || l == 2) {
							xx = aa;
						} else {
							zz = aa;
						}
					} else {
						zz = bb;
						xx = aa;
					}
				} else if (side.ordinal() <= 1) {
					xx = aa;
					zz = bb;
				} else if (side.ordinal() <= 3) {
					xx = aa;
					yy = bb;
				} else {
					zz = aa;
					yy = bb;
				}
				BlockPos p2 = pos.offset(side.getUnitVec3i()).offset(xx, yy, zz);
				BlockState existingState = world.getBlockState(p2);
				if (existingState.canBeReplaced()) {
					BlockState toPlace = bs;
					if (bs.getBlock() == Blocks.GRASS_BLOCK) {
						toPlace = Blocks.DIRT.defaultBlockState();
					}
					if (toPlace.canSurvive(world, p2)) {
						b.add(p2);
					}
				}
			}
		}
		return b;
	}

	@Override
	public boolean showAxis(ItemStack stack, Level world, Player player, Direction side, Direction.Axis axis) {
		return false;
	}
}
