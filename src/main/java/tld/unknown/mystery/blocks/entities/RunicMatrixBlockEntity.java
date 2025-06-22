package tld.unknown.mystery.blocks.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import tld.unknown.mystery.Thaumcraft;
import tld.unknown.mystery.api.capabilities.IInfusionPedestalCapability;
import tld.unknown.mystery.api.capabilities.IResearchCapability;
import tld.unknown.mystery.data.aspects.AspectList;
import tld.unknown.mystery.data.recipes.InfusionRecipe;
import tld.unknown.mystery.registries.ConfigBlockEntities;
import tld.unknown.mystery.registries.ConfigCapabilities;
import tld.unknown.mystery.registries.ConfigDataMaps;
import tld.unknown.mystery.registries.ConfigSounds;
import tld.unknown.mystery.util.CraftingUtils;
import tld.unknown.mystery.util.simple.SimpleBlockEntity;
import tld.unknown.mystery.util.simple.TickableBlockEntity;

import java.util.*;

//TODO: Infusion - Speed and Cost modifiers
@Getter
public class RunicMatrixBlockEntity extends SimpleBlockEntity implements TickableBlockEntity {

    private static final int RADIUS_HORIZONTAL = 8;
    private static final int RADIUS_BELOW = -7;
    private static final int RADIUS_ABOVE = 3;

    private static final int CYCLE_TIME_DEFAULT = 10;

    private final AnimationHandler animationHandler;
    private MatrixState state;
    private AltarTier tier;

    private RecipeHolder<InfusionRecipe> currentRecipe;
    private AspectList requiredEssentia;
    private UUID craftingPlayer;
    private List<BlockEntity> itemProviders;
    private int cycleSpeed;
    private float costModifier;

    public RunicMatrixBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ConfigBlockEntities.RUNIC_MATRIX.entityType(), pPos, pBlockState);
        this.state = MatrixState.INACTIVE;
        this.tier = AltarTier.ARCANE;
        this.animationHandler = new AnimationHandler(this);
        this.itemProviders = new ArrayList<>();
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
    public void onServerTick() { }

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

    private boolean scanEnvironment() {
        List<BlockPos> stabilityModifiers = new ArrayList<>();
        itemProviders.clear();
        cycleSpeed = CYCLE_TIME_DEFAULT;
        costModifier = 1.0F;

        if(!CraftingUtils.verifyInfusionAltarStructure(level, this.getBlockPos(), false))
            return false;
        for(int x = -RADIUS_HORIZONTAL; x <= RADIUS_HORIZONTAL; x++) {
            for(int z = -RADIUS_HORIZONTAL; z <= RADIUS_HORIZONTAL; z++) {
                for(int y = RADIUS_BELOW; y <= RADIUS_ABOVE; y++) {
                    BlockPos pos = getBlockPos().offset(x, y, z);
                    if(pos.equals(getBlockPos().below(2))) //Skip the middle pedestal, it is treated separately.
                        continue;
                    if (level.getCapability(ConfigCapabilities.INFUSION_PEDESTAL, pos) != null)
                        itemProviders.add(level.getBlockEntity(pos));
                    if (level.getCapability(ConfigCapabilities.INFUSION_STABILIZER, pos) != null || level.getBlockState(pos).getBlockHolder().getData(ConfigDataMaps.INFUSION_STABILIZER) != null)
                        stabilityModifiers.add(pos);
                }
            }
        }
        //TODO: Infusion - Instability
        return true;
    }

    private boolean beginCrafting(Player player) {
        if(!scanEnvironment()) {
            this.state = MatrixState.IDLE;
            this.currentRecipe = null;
            return false;
        }
        Optional<RecipeHolder<InfusionRecipe>> recipeOptional = getCurrentRecipe(player.getCapability(ConfigCapabilities.RESEARCH));
        if(recipeOptional.isEmpty()) {
            this.state = MatrixState.IDLE;
            this.currentRecipe = null;
            return false;
        }

        this.currentRecipe = recipeOptional.get();
        this.requiredEssentia = currentRecipe.value().aspects().modify(this.costModifier);
        this.craftingPlayer = player.getUUID();
        this.state = MatrixState.CRAFTING;
        getLevel().playSound(null, getBlockPos(), ConfigSounds.SPARKLE_HUM.value(), SoundSource.BLOCKS, .5F, 1);
        sync();
        return true;
    }

    private Optional<RecipeHolder<InfusionRecipe>> getCurrentRecipe(IResearchCapability research) {
        IInfusionPedestalCapability catalystPedestal = level.getCapability(ConfigCapabilities.INFUSION_PEDESTAL, getBlockPos().below(2));
        if(catalystPedestal == null) {
            Thaumcraft.error("Infusion crafting failed: Structure is valid but no central pedestal has been found. Please report this. Found %s", level.getBlockState(getBlockPos().below(2)).getBlock().getName());
            return Optional.empty();
        }
        ItemStack catalyst = catalystPedestal.getItem();
        List<ItemStack> components = itemProviders.stream().map(be -> level.getCapability(ConfigCapabilities.INFUSION_PEDESTAL, be.getBlockPos()).getItem()).filter(item -> item != ItemStack.EMPTY).toList();
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
        ABSORBING,
        CRAFTING,
        EXPLODING;

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
