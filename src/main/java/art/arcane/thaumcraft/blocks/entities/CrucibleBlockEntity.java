package art.arcane.thaumcraft.blocks.entities;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.api.capabilities.IResearchCapability;
import art.arcane.thaumcraft.data.aspects.AspectList;
import art.arcane.thaumcraft.data.attachments.ResearchAttachment;
import art.arcane.thaumcraft.data.recipes.AlchemyRecipe;
import art.arcane.thaumcraft.networking.packets.ClientboundBamfEffectPacket;
import art.arcane.thaumcraft.registries.ConfigBlockEntities;
import art.arcane.thaumcraft.registries.ConfigCapabilities;
import art.arcane.thaumcraft.registries.ConfigDataRegistries;
import art.arcane.thaumcraft.registries.ConfigSounds;
import art.arcane.thaumcraft.util.CraftingUtils;
import art.arcane.thaumcraft.util.FluidHelper;
import art.arcane.thaumcraft.util.simple.SimpleBlockEntity;
import art.arcane.thaumcraft.util.simple.TickableBlockEntity;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class CrucibleBlockEntity extends SimpleBlockEntity implements IFluidHandler, TickableBlockEntity {

	private static final int MAX_ESSENTIA = 500;
	private static final int HEAT_THRESHOLD = 150;
	private static final int HEAT_MAX = 200;
	private static final int ALCHEMY_COLOR = 0x9922FF;

	private final FluidTank waterTank;
	@Getter
	private final AspectList aspects;

	@Getter
	private int heat;

	public CrucibleBlockEntity(BlockPos pPos, BlockState pBlockState) {
		super(ConfigBlockEntities.CRUCIBLE.entityType(), pPos, pBlockState);
		this.waterTank = new FluidTank(FluidType.BUCKET_VOLUME);
		this.aspects = new AspectList();
	}

	@Override
	public TickSetting getTickSetting() {
		return TickSetting.SERVER_AND_CLIENT;
	}

	@Override
	public void onServerTick() {
		int prevHeat = this.heat;
		if (!FluidHelper.isTankEmpty(this)) {
			if (level.getBlockState(this.getBlockPos().below()).getTags().anyMatch(tag -> tag == ThaumcraftData.Tags.CRUCIBLE_HEATER)) {
				this.heat += this.heat < HEAT_MAX ? 1 : 0;
				if (prevHeat < HEAT_THRESHOLD && this.heat >= HEAT_THRESHOLD) {
					this.sync();
				}
			} else if (this.heat > 0) {
				this.heat--;
				if (this.heat < HEAT_THRESHOLD) {
					this.sync();
				}
			}
		} else if (this.heat > 0) {
			this.heat--;
			if (this.heat < HEAT_THRESHOLD) {
				this.sync();
			}
		}
	}

	@Override
	public void onClientTick() {
		if (FluidHelper.isTankEmpty(this)) {
			return;
		}

		double x = getBlockPos().getX() + 0.2 + level.getRandom().nextFloat() * 0.6;
		double y = getBlockPos().getY() + 0.1 + getFluidPercentage() * 0.5;
		double z = getBlockPos().getZ() + 0.2 + level.getRandom().nextFloat() * 0.6;

		if (heat >= HEAT_THRESHOLD) {
			level.addParticle(ParticleTypes.BUBBLE_COLUMN_UP, x, y, z, 0.0, 0.02, 0.0);
		}

		if (level.getRandom().nextInt(6) == 0 && !aspects.isEmpty()) {
			level.addParticle(ParticleTypes.BUBBLE_COLUMN_UP, x, y, z, 0.0, 0.04, 0.0);
		}
	}

	@Override
	protected void readNbt(CompoundTag nbt, HolderLookup.Provider pRegistries) {
		this.aspects.deserializeNBT(pRegistries, nbt.getCompound("aspects"));
		this.waterTank.readFromNBT(pRegistries, nbt.getCompound("water"));
		this.heat = nbt.getShort("heat");
	}

	@Override
	protected void writeNbt(CompoundTag nbt, HolderLookup.Provider pRegistries) {
		nbt.put("aspects", aspects.serializeNBT(pRegistries));
		nbt.put("water", waterTank.writeToNBT(pRegistries, new CompoundTag()));
		nbt.putShort("heat", (short) heat);
	}

	// TODO: Flux Pollution
	public void emptyCrucible() {
		if (!FluidHelper.isTankEmpty(this)) {
			waterTank.setFluid(FluidStack.EMPTY);
			aspects.clear();
			float randomPitch = 1.0F + (getLevel().getRandom().nextFloat() - getLevel().getRandom().nextFloat()) * .3F;
			getLevel().playSound(null, getBlockPos().getX() + .5D, getBlockPos().getY() + .5D, getBlockPos().getZ() + .5D, ConfigSounds.SPILL.value(), SoundSource.BLOCKS, .33F, randomPitch);
			sync();
		}
	}

	public boolean processInput(ItemStack stack, Player player, RegistryAccess access, boolean wasClicked) {
		boolean crafted = false, consumed = false;
		for (int i = 0; i < stack.getCount(); i++) {
			IResearchCapability research = player != null ? player.getCapability(ConfigCapabilities.RESEARCH) : new ResearchAttachment();
			Optional<RecipeHolder<AlchemyRecipe>> recipe = CraftingUtils.findAlchemyRecipe((ServerLevel) getLevel(), aspects, stack, research);
			if (recipe.isPresent()) {
				RecipeHolder<AlchemyRecipe> r = recipe.get();
				ItemStack result = r.value().result().copy();
				this.aspects.remove(r.value().aspects());
				drain(50, FluidAction.EXECUTE);
				spitItem(result);
				stack.shrink(1);
				crafted = true;

				if (getLevel() instanceof ServerLevel serverLevel) {
					Vec3 effectPos = new Vec3(getBlockPos().getX() + 0.5, getBlockPos().getY() + 0.75, getBlockPos().getZ() + 0.5);
					PacketDistributor.sendToPlayersNear(
							serverLevel,
							null,
							effectPos.x(),
							effectPos.y(),
							effectPos.z(),
							64.0,
							new ClientboundBamfEffectPacket(effectPos, ALCHEMY_COLOR, false, true)
					);
					getLevel().playSound(null, getBlockPos(), ConfigSounds.POOF.value(), SoundSource.BLOCKS, 0.4F, 1.0F + getLevel().getRandom().nextFloat() * 0.05F);
					getLevel().playSound(null, getBlockPos(), ConfigSounds.SPILL.value(), SoundSource.BLOCKS, 0.2F, 1.0F + getLevel().getRandom().nextFloat() * 0.4F);
				}
			} else {
				AspectList list = ConfigDataRegistries.ASPECT_REGISTRY.getAspects(stack);
				if (!list.isEmpty()) {
					if (aspects.size() + list.size() > MAX_ESSENTIA) {
						aspects.merge(list.drain(MAX_ESSENTIA - aspects.size()));
						//TODO Vent list as flux.
						Thaumcraft.debug("Spilled %d as flux.", list.size());
					} else {
						aspects.merge(list);
					}

					if (player == null || !player.isCreative() || !wasClicked) {
						Thaumcraft.info(Integer.toString(stack.getCount()));
						stack.shrink(1);
						consumed = true;
						//break out before we end up consuming half the player's stack. (bad!)
						break;
					}
					consumed = true;
				}
			}
		}

		if (crafted) {
			Thaumcraft.debug("Alchemy successful.");
		} else if (consumed) {
			Thaumcraft.debug("Item melted down.");
			getLevel().playSound(null, getBlockPos(), ConfigSounds.BUBBLE.value(), SoundSource.BLOCKS, 0.2F, 1.0F + getLevel().getRandom().nextFloat() * 0.4F);
		} else {
			Thaumcraft.debug("Unable to process Item.");
		}

		sync();

		return crafted || consumed;
	}

	public float getFluidPercentage() {
		return FluidHelper.isTankEmpty(this) ? 0 : (float) waterTank.getFluidAmount() / waterTank.getCapacity();
	}

	public float getAspectPercentage() {
		return aspects.isEmpty() ? 0 : (float) aspects.size() / MAX_ESSENTIA;
	}

	public boolean isCooking() {
		return heat >= HEAT_THRESHOLD;
	}

	private void spitItem(ItemStack items) {
		boolean repeatDrop = false;
		while (!items.isEmpty()) {
			ItemStack copy = items.copy();
			if (copy.getCount() > copy.getMaxStackSize())
				copy.setCount(copy.getMaxStackSize());
			items.shrink(copy.getCount());
			double hVel = repeatDrop ? (getLevel().getRandom().nextFloat() - getLevel().getRandom().nextFloat()) * .01F : 0F;
			getLevel().addFreshEntity(new ItemEntity(getLevel(),
					getBlockPos().getX() + .5F,
					getBlockPos().getY() + .5F,
					getBlockPos().getZ() + .5F,
					copy,
					hVel, .075D, hVel));
			repeatDrop = true;
		}
	}

	/* -------------------------------------------------------------------------------------------------------------- */
	/*                                              Fluid Tank Methods                                                */
	/* -------------------------------------------------------------------------------------------------------------- */

	@Override
	public int getTanks() {
		return waterTank.getTanks();
	}

	@Override
	public @NotNull FluidStack getFluidInTank(int tank) {
		return waterTank.getFluid();
	}

	@Override
	public int getTankCapacity(int tank) {
		return waterTank.getCapacity();
	}

	@Override
	public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
		return true;
	}

	@Override
	public int fill(FluidStack resource, FluidAction action) {
		int filled = waterTank.fill(resource, action);
		if (action.execute() && filled > 0) {
			sync();
		}
		return filled;
	}

	@Override
	public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
		FluidStack drained = waterTank.drain(maxDrain, action);
		if (action.execute() && !drained.isEmpty()) {
			sync();
		}
		return drained;
	}

	@Override
	public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
		FluidStack drained = waterTank.drain(resource, action);
		if (action.execute() && !drained.isEmpty()) {
			sync();
		}
		return drained;
	}
}
