package art.arcane.thaumcraft.blocks.entities;

import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import art.arcane.thaumcraft.blocks.LevitatorBlock;
import art.arcane.thaumcraft.client.fx.ThaumcraftFX;
import art.arcane.thaumcraft.config.ThaumcraftConfig;
import art.arcane.thaumcraft.data.aura.AuraHelper;
import art.arcane.thaumcraft.registries.ConfigBlockEntities;
import art.arcane.thaumcraft.util.simple.SimpleBlockEntity;
import art.arcane.thaumcraft.util.simple.TickableBlockEntity;

import java.util.List;

public class LevitatorBlockEntity extends SimpleBlockEntity implements TickableBlockEntity {

    private static final float MOVEMENT_SPEED = 0.1F;
    private static final float MAX_VELOCITY = 0.35F;
    private static final float DAMPEN_FACTOR = 0.9F;
    private static final float UPWARD_BOOST = 0.08F;

    private static int[] getRanges() {
        List<? extends Integer> list = ThaumcraftConfig.LEVITATOR_RANGES.get();
        return list.stream().mapToInt(Integer::intValue).toArray();
    }

    private static float[] getCosts() {
        List<? extends Double> list = ThaumcraftConfig.LEVITATOR_COSTS.get();
        float[] costs = new float[list.size()];
        for (int i = 0; i < list.size(); i++) {
            costs[i] = list.get(i).floatValue();
        }
        return costs;
    }

    private static float getMaxVis() {
        return ThaumcraftConfig.LEVITATOR_MAX_VIS.get().floatValue();
    }

    private static float getDrainRate() {
        return ThaumcraftConfig.LEVITATOR_DRAIN_RATE.get().floatValue();
    }

    private int rangeIndex = 0;
    private int rangeActual = 0;
    private int counter = 0;
    @Getter
    private float vis = 0;

    public LevitatorBlockEntity(BlockPos pos, BlockState state) {
        super(ConfigBlockEntities.LEVITATOR.entityType(), pos, state);
    }

    @Override
    public TickSetting getTickSetting() {
        return TickSetting.SERVER_AND_CLIENT;
    }

    @Override
    public void onServerTick() {
        if (level == null) return;

        Direction facing = getBlockState().getValue(LevitatorBlock.FACING);
        boolean enabled = getBlockState().getValue(LevitatorBlock.ENABLED);

        updateRangeActual(facing);

        if (rangeActual > 0 && enabled) {
            processEntities(facing);
        }
    }

    @Override
    public void onClientTick() {
        if (level == null) return;

        boolean enabled = getBlockState().getValue(LevitatorBlock.ENABLED);
        if (!enabled || rangeActual <= 0 || vis <= 0) return;

        Direction facing = getBlockState().getValue(LevitatorBlock.FACING);

        if (level.random.nextFloat() < 0.1F) {
            spawnBlockParticle(facing);
        }
    }

    private void spawnBlockParticle(Direction facing) {
        double x = getBlockPos().getX() + 0.25 + level.random.nextFloat() * 0.5;
        double y = getBlockPos().getY() + 0.25 + level.random.nextFloat() * 0.5;
        double z = getBlockPos().getZ() + 0.25 + level.random.nextFloat() * 0.5;
        double vx = facing.getStepX() / 50.0;
        double vy = facing.getStepY() / 50.0;
        double vz = facing.getStepZ() / 50.0;
        ThaumcraftFX.drawLevitatorParticle(x, y, z, vx, vy, vz);
    }

    private void updateRangeActual(Direction facing) {
        int[] ranges = getRanges();
        int maxRange = ranges[rangeIndex];
        if (rangeActual > maxRange) {
            rangeActual = 0;
        }

        int checkPos = counter % maxRange;
        BlockPos targetPos = getBlockPos().relative(facing, 1 + checkPos);

        if (level.getBlockState(targetPos).isSolidRender()) {
            if (1 + checkPos < rangeActual) {
                rangeActual = 1 + checkPos;
            }
            counter = -1;
        } else if (1 + checkPos > rangeActual) {
            rangeActual = 1 + checkPos;
        }
        counter++;
    }

    private void drainVisFromAura() {
        float maxVis = getMaxVis();
        if (vis < maxVis) {
            float drained = AuraHelper.drainVis(level, getBlockPos(), getDrainRate(), false);
            if (drained > 0) {
                vis = Math.min(maxVis, vis + drained);
                setChanged();
            }
        }
    }

    private void processEntities(Direction facing) {
        AABB searchArea = createSearchArea(facing);
        List<Entity> entities = level.getEntitiesOfClass(Entity.class, searchArea, this::canAffect);

        if (entities.isEmpty()) return;

        float cost = getVisCost();
        boolean affected = false;

        for (Entity entity : entities) {
            if (vis < cost) {
                drainVisFromAura();
            }
            if (vis < cost) break;

            applyMovement(entity, facing);
            vis -= cost;
            affected = true;
        }

        if (affected && counter % 20 == 0) {
            setChanged();
            sync();
        }
    }

    private AABB createSearchArea(Direction facing) {
        BlockPos pos = getBlockPos();
        return new AABB(
                pos.getX() - (facing.getStepX() < 0 ? rangeActual : 0),
                pos.getY() - (facing.getStepY() < 0 ? rangeActual : 0),
                pos.getZ() - (facing.getStepZ() < 0 ? rangeActual : 0),
                pos.getX() + 1 + (facing.getStepX() > 0 ? rangeActual : 0),
                pos.getY() + 1 + (facing.getStepY() > 0 ? rangeActual : 0),
                pos.getZ() + 1 + (facing.getStepZ() > 0 ? rangeActual : 0)
        );
    }

    private boolean canAffect(Entity entity) {
        return entity instanceof ItemEntity || entity instanceof AbstractHorse || entity.isPushable();
    }

    private void applyMovement(Entity entity, Direction facing) {
        Vec3 delta = entity.getDeltaMovement();
        double dx = delta.x;
        double dy = delta.y;
        double dz = delta.z;

        if (entity.isShiftKeyDown() && facing == Direction.UP) {
            if (dy < 0) {
                dy *= DAMPEN_FACTOR;
            }
        } else {
            dx += MOVEMENT_SPEED * facing.getStepX();
            dy += MOVEMENT_SPEED * facing.getStepY();
            dz += MOVEMENT_SPEED * facing.getStepZ();

            if (facing.getAxis() != Direction.Axis.Y && !entity.onGround()) {
                if (dy < 0) {
                    dy *= DAMPEN_FACTOR;
                }
                dy += UPWARD_BOOST;
            }

            dx = Mth.clamp(dx, -MAX_VELOCITY, MAX_VELOCITY);
            dy = Mth.clamp(dy, -MAX_VELOCITY, MAX_VELOCITY);
            dz = Mth.clamp(dz, -MAX_VELOCITY, MAX_VELOCITY);
        }

        entity.setDeltaMovement(dx, dy, dz);
        entity.fallDistance = 0;
        entity.hurtMarked = true;
    }

    public void cycleRange() {
        rangeIndex = (rangeIndex + 1) % getRanges().length;
        rangeActual = 0;
        setChanged();
        sync();
    }

    public int getCurrentRange() {
        return getRanges()[rangeIndex];
    }

    public float getVisCost() {
        float[] costs = getCosts();
        if (rangeIndex < costs.length) {
            return costs[rangeIndex];
        }
        return costs.length > 0 ? costs[0] : 0.01F;
    }

    @Override
    protected void readNbt(CompoundTag nbt, HolderLookup.Provider registries) {
        rangeIndex = nbt.getByte("rangeIndex");
        if (rangeIndex < 0 || rangeIndex >= getRanges().length) {
            rangeIndex = 0;
        }
        vis = nbt.getFloat("vis");
        rangeActual = nbt.getInt("rangeActual");
    }

    @Override
    protected void writeNbt(CompoundTag nbt, HolderLookup.Provider registries) {
        nbt.putByte("rangeIndex", (byte) rangeIndex);
        nbt.putFloat("vis", vis);
        nbt.putInt("rangeActual", rangeActual);
    }
}
