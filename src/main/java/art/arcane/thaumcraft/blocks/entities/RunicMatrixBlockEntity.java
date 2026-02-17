package art.arcane.thaumcraft.blocks.entities;

import art.arcane.thaumcraft.api.ThaumcraftUtils;
import art.arcane.thaumcraft.api.aspects.Aspect;
import art.arcane.thaumcraft.api.capabilities.IGoggleRendererCapability;
import art.arcane.thaumcraft.api.enums.InfusionStability;
import com.mojang.blaze3d.vertex.PoseStack;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.api.capabilities.IInfusionPedestalCapability;
import art.arcane.thaumcraft.api.capabilities.IResearchCapability;
import art.arcane.thaumcraft.data.aspects.AspectList;
import art.arcane.thaumcraft.data.recipes.InfusionRecipe;
import art.arcane.thaumcraft.registries.ConfigBlockEntities;
import art.arcane.thaumcraft.registries.ConfigCapabilities;
import art.arcane.thaumcraft.registries.ConfigDataMaps;
import art.arcane.thaumcraft.registries.ConfigSounds;
import art.arcane.thaumcraft.util.CraftingUtils;
import art.arcane.thaumcraft.util.simple.SimpleBlockEntity;
import art.arcane.thaumcraft.util.simple.TickableBlockEntity;

import java.text.DecimalFormat;
import java.util.*;

//TODO: Infusion - Speed and Cost modifiers
@Getter
public class RunicMatrixBlockEntity extends SimpleBlockEntity implements TickableBlockEntity, IGoggleRendererCapability {

    private static final int RADIUS_HORIZONTAL = 8;
    private static final int RADIUS_BELOW = -7;
    private static final int RADIUS_ABOVE = 3;

    private static final int CYCLE_TIME_DEFAULT = 20;
    private static final int CRAFT_TIME_DEFAULT = 5;
	private static final float STABILITY_CAP = 25F;

	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#######.##");

    private final AnimationHandler animationHandler;
    private MatrixState state;

    private RecipeHolder<InfusionRecipe> currentRecipe;
    private AspectList requiredEssentia;
	private List<Ingredient> requiredItems;
    private UUID craftingPlayer;
    private List<IInfusionPedestalCapability> itemProviders;

	private int craftTimer;
    private int cycleTimer;
    private float costModifier;
    private float cycleDelay;

	private float stability, stabilityModifier;
	private boolean shouldRecheckEnvironment;

    public RunicMatrixBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ConfigBlockEntities.RUNIC_MATRIX.entityType(), pPos, pBlockState);
        this.state = MatrixState.INACTIVE;
        this.animationHandler = new AnimationHandler(this);
        this.itemProviders = new ArrayList<>();
    }

	@Override
	public List<Component> textDisplay() {
		List<Component> list = new ArrayList<>();
		InfusionStability stabilityType = getInfusionStability();
		list.add(stabilityType.getMessage().withStyle(ChatFormatting.BOLD));
		list.add(Component.translatable("msg.thaumcraft.infusion_gain", DECIMAL_FORMAT.format(stabilityModifier)).withStyle(ChatFormatting.ITALIC, ChatFormatting.GOLD));
		if(this.state == MatrixState.CRAFTING || this.state == MatrixState.ABSORBING) {
			float loss = currentRecipe.value().instability() / stabilityType.getModifier();
			if(loss != 0.0F)
				list.add(Component.translatable("msg.thaumcraft.infusion_loss", DECIMAL_FORMAT.format(loss)).withStyle(ChatFormatting.ITALIC, ChatFormatting.RED));
		}
		return list;
	}

	@Override
	public void render(PoseStack stack, MultiBufferSource buffer, DeltaTracker deltaTracker) {
		//TODO - Aspect Container Rendering
	}

	@Override
    public TickSetting getTickSetting() {
        return TickSetting.SERVER_AND_CLIENT;
    }

    @Override
    protected void readNbt(CompoundTag nbt, HolderLookup.Provider pRegistries) {
        this.state = MatrixState.fromString(nbt.getString("state"));
    }

    @Override
    protected void writeNbt(CompoundTag nbt, HolderLookup.Provider pRegistries) {
        nbt.putString("state", this.state.getSerializedName());
    }

    @Override
    public void onServerTick() {
		if(this.state == MatrixState.INACTIVE)
			return;

		cycleTimer++;

		if(shouldRecheckEnvironment) {
			shouldRecheckEnvironment = false;
			scanEnvironment();
		}


		verifyStructure();

		switch(this.state) {
			case CRAFTING, ABSORBING -> {
				if(cycleTimer % 20 == 0 && !verifyStructure())
					return;
				if(cycleTimer % cycleDelay == 0)
					doCrafting();
			}
			case IDLE ->  {
				if(cycleTimer % 100 == 0 && !verifyStructure())
					return;
				if(this.stability < STABILITY_CAP && cycleTimer % Math.max(5, cycleDelay) == 0)
					stability = Math.min(STABILITY_CAP, stability + Math.max(.1F, stabilityModifier));
			}
		}
	}

    @Override
    public void onClientTick() {
        animationHandler.tick();
        if(animationHandler.hasFinishedStartup() && (animationHandler.rubikAnimation == null || animationHandler.isRubikDone())) {
            RandomSource r = getLevel().getRandom();
            animationHandler.createNewRubik(
                    Direction.values()[r.nextIntBetweenInclusive(0, 5)],
                    r.nextIntBetweenInclusive(1, 3),
                    10,
                    r.nextBoolean());
        }
    }

    public boolean activate(Level level) {
        if(CraftingUtils.verifyInfusionAltarStructure(level, this.getBlockPos(), false)) {
            this.state = MatrixState.STARTING;
            level.playSound(null, getBlockPos(), ConfigSounds.SPARKLE_HUM.value(), SoundSource.BLOCKS, 1, 1);
            sync();
            return true;
        }
        return false;
    }

    private boolean beginCrafting(Player player) {
		if(this.state != MatrixState.IDLE)
			return false;

		if(!verifyStructure())
			return false;

        Optional<RecipeHolder<InfusionRecipe>> recipeOptional = getCurrentRecipe(player.getCapability(ConfigCapabilities.RESEARCH));
        if(recipeOptional.isEmpty()) {
            this.state = MatrixState.IDLE;
            this.currentRecipe = null;
            return false;
        }

        this.currentRecipe = recipeOptional.get();
        this.requiredEssentia = currentRecipe.value().aspects().clone().modify(this.costModifier);
		this.requiredItems = NonNullList.copyOf(currentRecipe.value().components());
        this.craftingPlayer = player.getUUID();
        this.state = MatrixState.ABSORBING;
        getLevel().playSound(null, getBlockPos(), ConfigSounds.SPARKLE_HUM.value(), SoundSource.BLOCKS, .5F, 1);
        sync();
        return true;
    }

	private void doCrafting() {
		boolean interrupted = false;
		if(this.stability < STABILITY_CAP && this.stability > -100F) {
			float loss = (currentRecipe.value().instability() / getInfusionStability().getModifier()) * getLevel().random.nextFloat();
			stability = Math.clamp(-100F, STABILITY_CAP, stability + stabilityModifier - loss);
		}

		IInfusionPedestalCapability catalyst = level.getCapability(ConfigCapabilities.INFUSION_PEDESTAL, getBlockPos().below(2));
		if (catalyst == null || catalyst.getItem().isEmpty() || !currentRecipe.value().catalyst().test(catalyst.getItem())) {
			interrupted = true;
		}

		if(interrupted || (stability < 0F && level.random.nextInt(1500) <= Mth.abs(stability))) {
			//TODO: Instability Events
			stability += 5 + level.random.nextFloat() * 5F;
			//TODO: Grant instability research
			if(!interrupted)
				return;
		}

		if(interrupted) {
			this.state = MatrixState.IDLE;
			this.currentRecipe = null;
			this.requiredEssentia = null;
			this.requiredItems = null;
			this.craftingPlayer = null;
			sync();
			return;
		}

		if(this.state == MatrixState.ABSORBING ) {
			ResourceKey<Aspect> nextAspect = requiredEssentia.aspectsPresent().getFirst();
			if(ThaumcraftUtils.drainClosestEssentiaSource(getLevel(), getBlockPos(), 12, nextAspect, 1, Direction.UP)) {
				requiredEssentia.remove(nextAspect, 1);
				if(requiredEssentia.isEmpty())
					this.state = MatrixState.CRAFTING;
			} else {
				this.stability -= 0.25F;
			}
			this.shouldRecheckEnvironment = true;
			sync();
			return;
		}

		if(this.state == MatrixState.CRAFTING) {
			for(Ingredient item : requiredItems) {
				for (IInfusionPedestalCapability pedestal : itemProviders) {
					if(pedestal.getItem().isEmpty() || !item.test(pedestal.getItem()))
						continue;
					if(craftTimer == 0) {
						craftTimer = CRAFT_TIME_DEFAULT;
						//TODO: Item Infusion Particles
					} else if(craftTimer-- <= 1) {
						pedestal.consumeItem();
						requiredItems.remove(item);
						sync();
					}
					return;
				}

				if(getLevel().random.nextInt(1 + requiredItems.size()) == 0) {
					this.state = MatrixState.ABSORBING;
					//TODO: Some visual or sound indicator of this
					ResourceKey<Aspect> randomAspect = currentRecipe.value().aspects().aspectsPresent().get(getLevel().random.nextInt(currentRecipe.value().aspects().aspectCount()));
					requiredEssentia.remove(randomAspect, 1);
					stability -= 0.25F;
					sync();
				}

			}
			if(requiredItems.isEmpty()) {
				finishCrafting();
				sync();
			}
		}
	}

	private void finishCrafting() {

	}

	private boolean verifyStructure() {
		if(!CraftingUtils.verifyInfusionAltarStructure(level, this.getBlockPos(), false)) {
			if(this.state != MatrixState.INACTIVE && this.state != MatrixState.STOPPING) {
				this.state = MatrixState.STOPPING;
				this.currentRecipe = null;
				this.requiredEssentia = null;
				this.requiredItems = null;
				this.craftingPlayer = null;
				this.itemProviders.clear();
				sync();
			}
			return false;
		}
		return true;
	}

	private boolean scanEnvironment() {
		List<BlockPos> stabilityModifiers = new ArrayList<>();
		itemProviders.clear();
		cycleTimer = CYCLE_TIME_DEFAULT;
		costModifier = 1.0F;

		if(!verifyStructure())
			return false;

		for(int x = -RADIUS_HORIZONTAL; x <= RADIUS_HORIZONTAL; x++) {
			for(int z = -RADIUS_HORIZONTAL; z <= RADIUS_HORIZONTAL; z++) {
				for(int y = RADIUS_BELOW; y <= RADIUS_ABOVE; y++) {
					BlockPos pos = getBlockPos().offset(x, y, z);
					if(pos.equals(getBlockPos().below(2))) //Skip the middle pedestal, it is treated separately.
						continue;
					IInfusionPedestalCapability pedestal = level.getCapability(ConfigCapabilities.INFUSION_PEDESTAL, pos);
					if (pedestal != null)
						itemProviders.add(pedestal);
					if (level.getCapability(ConfigCapabilities.INFUSION_STABILIZER, pos) != null || level.getBlockState(pos).getBlockHolder().getData(ConfigDataMaps.INFUSION_STABILIZER) != null)
						stabilityModifiers.add(pos);
				}
			}
		}
		//TODO: Infusion - Instability modifiers, Symmetry
		return true;
	}

	private InfusionStability getInfusionStability() {
		if(stability > 0F) {
			return stability > STABILITY_CAP / 2 ? InfusionStability.VERY_STABLE : InfusionStability.STABLE;
		} else {
			return stability > -STABILITY_CAP ? InfusionStability.VERY_UNSTABLE : InfusionStability.UNSTABLE;
		}
	}
	private Optional<RecipeHolder<InfusionRecipe>> getCurrentRecipe(IResearchCapability research) {
		IInfusionPedestalCapability catalystPedestal = level.getCapability(ConfigCapabilities.INFUSION_PEDESTAL, getBlockPos().below(2));
		if(catalystPedestal == null) {
			Thaumcraft.error("Infusion crafting failed: Structure is valid but no central pedestal has been found. Please report this. Found %s", level.getBlockState(getBlockPos().below(2)).getBlock().getName());
			return Optional.empty();
		}
		ItemStack catalyst = catalystPedestal.getItem();
		List<ItemStack> components = itemProviders.stream().map(IInfusionPedestalCapability::getItem).filter(item -> item != ItemStack.EMPTY).toList();
		return CraftingUtils.findInfusionRecipe((ServerLevel)level, new InfusionRecipe.Input(catalyst, NonNullList.copyOf(components), research));
	}

	public static class AnimationHandler {

        private final RunicMatrixBlockEntity be;

        private float activate, activateProgress;

        private float idleRot;

        @Getter
        private RubikAnimation rubikAnimation;
        @Getter
        private boolean rubikDone;
        private float rubik, rubikTarget;

        public AnimationHandler(RunicMatrixBlockEntity blockEntity) {
            this.be = blockEntity;
        }

        public void createNewRubik(Direction dir, int amount, float speed, boolean inverted) {
            this.rubik = this.rubikTarget = 0;
            this.rubikDone = false;
            this.rubikAnimation = new RubikAnimation(dir, amount * 90, speed, inverted);
        }

        public boolean hasFinishedStartup() {
            return activate >= 1.0F;
        }

        public float getActivateAnimation(float delta) {
            activate = Mth.lerp(delta, activate, activateProgress);
            return activate;
        }

        public float getIdleRotation(float delta) {
            idleRot = Mth.rotLerp(delta, idleRot, (be.getLevel().getGameTime() % 360) * activate);
            return idleRot;
        }

        public float getRubikAngle(float delta) {
            if(rubikDone) {
                return 0;
            }
            rubik = Mth.lerp(delta, rubik, rubikTarget);
            if(rubik >= rubikAnimation.target()) {
                rubikDone = true;
                return 0;
            }
            return rubik * (rubikAnimation.inverted() ? -1 : 1);
        }

        public void tick() {

        }
    }

    public record RubikAnimation(Direction axis, int target, float speed, boolean inverted) { }

    @AllArgsConstructor
    public enum MatrixState implements StringRepresentable {
        INACTIVE,
        STARTING,
        IDLE,
		STOPPING,
        ABSORBING,
        CRAFTING;

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase();
        }

        public static MatrixState fromString(String name) {
            return Arrays.stream(MatrixState.values()).filter(s -> s.getSerializedName().equals(name)).findFirst().orElse(MatrixState.INACTIVE);
        }
    }

    @Getter
    @AllArgsConstructor
    public enum AltarTier {
        ARCANE(Thaumcraft.id("textures/block/runic_matrix.png"), 0F, 0F, 0),
        ANCIENT(Thaumcraft.id("textures/block/runic_matrix_ancient.png"), -.1F, -.1F, -1),
        ELDRITCH(Thaumcraft.id("textures/block/runic_matrix_eldritch.png"), .05F,.2F, -3);

        private final ResourceLocation texture;
        private float costModifier, stabilityRegen;
        private int cycleModifier;
    }
}
