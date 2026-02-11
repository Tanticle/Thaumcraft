package art.arcane.thaumcraft.blocks.entities;

import art.arcane.thaumcraft.api.aspects.Aspect;
import art.arcane.thaumcraft.api.capabilities.IEssentiaCapability;
import art.arcane.thaumcraft.api.helpers.EssentiaHelper;
import art.arcane.thaumcraft.blocks.alchemy.EssentiaInputBlock;
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

public class EssentiaInputBlockEntity extends SimpleBlockEntity implements IEssentiaCapability, TickableBlockEntity {

    private int tickCounter;

    public EssentiaInputBlockEntity(BlockPos pos, BlockState state) {
        super(ConfigBlockEntities.ESSENTIA_INPUT.entityType(), pos, state);
    }

    private Direction getFacing() {
        BlockState state = this.getBlockState();
        return state.hasProperty(EssentiaInputBlock.FACING) ? state.getValue(EssentiaInputBlock.FACING) : Direction.UP;
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
        transferToRemote();
    }

    private void transferToRemote() {
        if (this.level == null) {
            return;
        }
        Direction facing = getFacing();
        Direction connection = getConnectionSide();
        BlockPos sourcePos = this.worldPosition.relative(connection);

        IEssentiaCapability source = this.level.getCapability(ConfigCapabilities.ESSENTIA, sourcePos, facing);
        if (source == null && this.level.getBlockEntity(sourcePos) instanceof IEssentiaCapability fallback) {
            source = fallback;
        }
        if (source == null || !source.getSideStatus(facing).isOutput()) {
            return;
        }

        int ownSuction = getSuction(connection);
        if (source.getEssentia(facing) <= 0
                || source.getSuction(facing) >= ownSuction
                || ownSuction < source.getMinimumSuction(facing)) {
            return;
        }

        ResourceKey<Aspect> aspect = source.getEssentiaType(facing);
        if (aspect == null) {
            return;
        }

        if (EssentiaHelper.addEssentia(this.level, this.worldPosition, aspect, facing, 16, false, 5)) {
            source.drainAspect(aspect, 1, facing);
        }
    }

    @Override
    public TickSetting getTickSetting() {
        return TickSetting.SERVER;
    }

    @Override
    public SideStatus getSideStatus(Direction dir) {
        return SideStatus.INPUT;
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
        return isConnectable(dir) ? 128 : 0;
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
        return isConnectable(dir) && amount > 0 ? amount : 0;
    }

    @Override
    public boolean canFit(ResourceKey<Aspect> aspect, int amount, Direction dir) {
        if (!isConnectable(dir)) {
            return false;
        }
        return amount <= 0 || aspect != null;
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
