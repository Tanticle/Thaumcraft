package art.arcane.thaumcraft.blocks.entities;

import art.arcane.thaumcraft.api.aspects.Aspect;
import art.arcane.thaumcraft.api.capabilities.IEssentiaCapability;
import art.arcane.thaumcraft.api.helpers.EssentiaHelper;
import art.arcane.thaumcraft.blocks.alchemy.EssentiaOutputBlock;
import art.arcane.thaumcraft.registries.ConfigBlockEntities;
import art.arcane.thaumcraft.registries.ConfigCapabilities;
import art.arcane.thaumcraft.util.simple.SimpleBlockEntity;
import art.arcane.thaumcraft.util.simple.TickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.state.BlockState;

public class EssentiaOutputBlockEntity extends SimpleBlockEntity implements IEssentiaCapability, TickableBlockEntity {

    private int tickCounter;

    public EssentiaOutputBlockEntity(BlockPos pos, BlockState state) {
        super(ConfigBlockEntities.ESSENTIA_OUTPUT.entityType(), pos, state);
    }

    private Direction getFacing() {
        BlockState state = this.getBlockState();
        return state.hasProperty(EssentiaOutputBlock.FACING) ? state.getValue(EssentiaOutputBlock.FACING) : Direction.UP;
    }

    public Direction getConnectionSide() {
        return getFacing().getOpposite();
    }

    private boolean isConnectable(Direction side) {
        return side != null && side == getConnectionSide();
    }

    @Override
    protected void readNbt(CompoundTag nbt, HolderLookup.Provider registries) {
    }

    @Override
    protected void writeNbt(CompoundTag nbt, HolderLookup.Provider registries) {
    }

    @Override
    public void onServerTick() {
        if (this.level == null || this.level.isClientSide()) {
            return;
        }
        if (++this.tickCounter % 5 != 0) {
            return;
        }
        transferFromRemote();
    }

    private void transferFromRemote() {
        if (this.level == null) {
            return;
        }
        Direction facing = getFacing();
        Direction connection = getConnectionSide();
        BlockPos targetPos = this.worldPosition.relative(connection);

        IEssentiaCapability target = this.level.getCapability(ConfigCapabilities.ESSENTIA, targetPos, facing);
        if (target == null && this.level.getBlockEntity(targetPos) instanceof IEssentiaCapability fallback) {
            target = fallback;
        }
        if (target == null || !target.getSideStatus(facing).isInput()) {
            return;
        }

        int targetSuction = target.getSuction(facing);
        ResourceKey<Aspect> desired = target.getSuctionType(facing);
        if (targetSuction <= 0 || desired == null) {
            return;
        }

        if (EssentiaHelper.drainEssentiaWithConfirmation(this.level, this.worldPosition, desired, facing, 16, false, 5)
                && target.fillAspect(desired, 1, facing) > 0) {
            EssentiaHelper.confirmDrain();
        }
    }

    @Override
    public TickSetting getTickSetting() {
        return TickSetting.SERVER;
    }

    @Override
    public SideStatus getSideStatus(Direction dir) {
        return SideStatus.OUTPUT;
    }

    @Override
    public int getEssentia(Direction dir) {
        return 0;
    }

    @Override
    public ResourceKey<Aspect> getEssentiaType(Direction dir) {
        return null;
    }

    @Override
    public int getMinimumSuction(Direction dir) {
        return 0;
    }

    @Override
    public int getSuction(Direction dir) {
        return 0;
    }

    @Override
    public ResourceKey<Aspect> getSuctionType(Direction dir) {
        return null;
    }

    @Override
    public int drainAspect(ResourceKey<Aspect> aspect, int amount, Direction dir) {
        return 0;
    }

    @Override
    public int fillAspect(ResourceKey<Aspect> aspect, int amount, Direction dir) {
        return 0;
    }

    @Override
    public boolean canFit(ResourceKey<Aspect> aspect, int amount, Direction dir) {
        return false;
    }

    @Override
    public boolean contains(ResourceKey<Aspect> aspect, int amount, Direction dir) {
        return amount <= 0;
    }

    @Override
    public boolean compliesToAspect(ResourceKey<Aspect> aspect, Direction dir) {
        return isConnectable(dir);
    }

    @Override
    public boolean isContainer(Direction dir) {
        return false;
    }
}
