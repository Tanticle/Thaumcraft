package tld.unknown.mystery.blocks.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.BlockState;
import tld.unknown.mystery.registries.ConfigBlockEntities;
import tld.unknown.mystery.util.simple.SimpleBlockEntity;
import tld.unknown.mystery.util.simple.TickableBlockEntity;

import java.util.Arrays;

@Getter
public class RunicMatrixBlockEntity extends SimpleBlockEntity implements TickableBlockEntity {

    private final AnimationHandler animationHandler;

    private boolean activated;
    private MatrixState matrixState;

    public RunicMatrixBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ConfigBlockEntities.RUNIC_MATRIX.entityType(), pPos, pBlockState);
        this.activated = false;
        this.matrixState = MatrixState.IDLE;
        this.animationHandler = new AnimationHandler(this);
    }

    @Override
    public TickSetting getTickSetting() {
        return TickSetting.SERVER_AND_CLIENT;
    }

    @Override
    protected void readNbt(CompoundTag nbt, HolderLookup.Provider pRegistries) {
        this.matrixState = MatrixState.fromString(nbt.getString("state"));
    }

    @Override
    protected void writeNbt(CompoundTag nbt, HolderLookup.Provider pRegistries) {
        nbt.putString("state", matrixState.getSerializedName());
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

    public void activate() {
        this.activated = !activated;
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
            if (be.isActivated() && this.activateProgress != 1.0F) {
                if (this.activateProgress < 1.0F) {
                    this.activateProgress += Math.max(this.activateProgress / 10.0F, 0.001F);
                }
                if (this.activateProgress > 0.999D) {
                    this.activateProgress = 1.0F;
                }
            }
            if (!be.isActivated() && this.activateProgress > 0.0F) {
                if (this.activateProgress > 0.0F) {
                    this.activateProgress -= this.activateProgress / 10.0F;
                }
                if (this.activateProgress < 0.001D) {
                    this.activateProgress = 0.0F;
                }
            }

            if(rubikAnimation != null && !rubikDone) {
                rubikTarget = Math.min(rubikTarget + rubikAnimation.speed(), rubikAnimation.target());
            }
        }
    }

    public record RubikAnimation(Direction axis, int target, float speed, boolean inverted) { }

    @AllArgsConstructor
    public enum MatrixState implements StringRepresentable {
        IDLE,
        CRAFTING,
        SECRET;

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase();
        }

        public static MatrixState fromString(String name) {
            return Arrays.stream(MatrixState.values()).filter(s -> s.getSerializedName().equals(name)).findFirst().orElse(MatrixState.IDLE);
        }
    }
}
