package art.arcane.thaumcraft.blocks.entities;

import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import art.arcane.thaumcraft.data.aura.AuraHelper;
import art.arcane.thaumcraft.integrations.botania.BotaniaCompat;
import art.arcane.thaumcraft.registries.ConfigBlockEntities;
import art.arcane.thaumcraft.util.simple.SimpleBlockEntity;
import art.arcane.thaumcraft.util.simple.TickableBlockEntity;

@Getter
public class EverfullUrnBlockEntity extends SimpleBlockEntity implements TickableBlockEntity {

    public static final int MAX_WATER = 1000;
    public static final int BUCKET_COST = 1000;
    public static final int BOTTLE_COST = 333;
    public static final int CAULDRON_COST = 333;
    public static final int FLUID_TRANSFER_AMOUNT = 25;
    public static final int APOTHECARY_COST = 1000;
    private static final int SCAN_RADIUS_XZ = 2;
    private static final int SCAN_RADIUS_Y_DOWN = 1;
    private static final int SCAN_RADIUS_Y_UP = 1;
    private static final int SCAN_SIZE_XZ = SCAN_RADIUS_XZ * 2 + 1;
    private static final int SCAN_SIZE_Y = SCAN_RADIUS_Y_DOWN + SCAN_RADIUS_Y_UP + 1;
    private static final int MAX_SCAN_INDEX = SCAN_SIZE_XZ * SCAN_SIZE_Y * SCAN_SIZE_XZ;

    private int waterAmount;
    private int scanIndex;
    private final IntSet knownHandlers = new IntArraySet();

    public EverfullUrnBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ConfigBlockEntities.EVERFULL_URN.entityType(), pPos, pBlockState);
        this.waterAmount = 0;
        this.scanIndex = 0;
    }

    @Override
    public void onServerTick() {
        if (waterAmount < MAX_WATER) {
            float neededVis = (MAX_WATER - waterAmount) / 1000.0F;
            if (neededVis > 0.1F) {
                neededVis = 0.1F;
            }
            float drainedVis = AuraHelper.drainVis(getLevel(), getBlockPos(), neededVis, false);
            int waterGain = (int) (1000.0F * drainedVis);
            if (waterGain > 0) {
                waterAmount = Math.min(MAX_WATER, waterAmount + waterGain);
                setChanged();
                if (waterAmount >= MAX_WATER) {
                    sync();
                }
            }
        }

        tickContainerFilling();
    }

    private void tickContainerFilling() {
        scanIndex = (scanIndex + 1) % MAX_SCAN_INDEX;
        BlockPos scanPos = indexToPos(scanIndex);

        if (isValidHandler(scanPos)) {
            knownHandlers.add(scanIndex);
        }

        for (int idx : knownHandlers.toIntArray()) {
            if (waterAmount < FLUID_TRANSFER_AMOUNT) {
                break;
            }

            BlockPos pos = indexToPos(idx);

            if (tryFillFluidHandler(pos)) {
                break;
            }

            if (tryFillCauldron(pos)) {
                break;
            }

            if (tryFillBotaniaApothecary(pos)) {
                break;
            }

            if (!isValidHandler(pos)) {
                knownHandlers.remove(idx);
            }
        }
    }

    private boolean isValidHandler(BlockPos pos) {
        BlockState state = getLevel().getBlockState(pos);
        if (state.is(Blocks.CAULDRON) || state.is(Blocks.WATER_CAULDRON)) {
            return true;
        }
        BlockEntity blockEntity = getLevel().getBlockEntity(pos);
        if (blockEntity != null) {
            IFluidHandler handler = getLevel().getCapability(Capabilities.FluidHandler.BLOCK, pos, state, blockEntity, Direction.UP);
            if (handler != null) {
                return true;
            }
            if (BotaniaCompat.isPetalApothecary(blockEntity)) {
                return true;
            }
        }
        return false;
    }

    private boolean tryFillFluidHandler(BlockPos pos) {
        BlockEntity blockEntity = getLevel().getBlockEntity(pos);
        if (blockEntity == null) {
            return false;
        }
        IFluidHandler handler = getLevel().getCapability(Capabilities.FluidHandler.BLOCK, pos, blockEntity.getBlockState(), blockEntity, Direction.UP);
        if (handler == null) {
            return false;
        }
        FluidStack water = new FluidStack(net.minecraft.world.level.material.Fluids.WATER, Math.min(waterAmount, FLUID_TRANSFER_AMOUNT));
        int filled = handler.fill(water, IFluidHandler.FluidAction.SIMULATE);
        if (filled > 0) {
            handler.fill(new FluidStack(net.minecraft.world.level.material.Fluids.WATER, filled), IFluidHandler.FluidAction.EXECUTE);
            consumeWater(filled);
            return true;
        }
        return false;
    }

    private boolean tryFillCauldron(BlockPos pos) {
        BlockState state = getLevel().getBlockState(pos);
        if (state.is(Blocks.CAULDRON) && waterAmount >= CAULDRON_COST) {
            getLevel().setBlockAndUpdate(pos, Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 1));
            consumeWater(CAULDRON_COST);
            return true;
        }
        if (state.is(Blocks.WATER_CAULDRON)) {
            int level = state.getValue(LayeredCauldronBlock.LEVEL);
            if (level < 3 && waterAmount >= CAULDRON_COST) {
                getLevel().setBlockAndUpdate(pos, state.setValue(LayeredCauldronBlock.LEVEL, level + 1));
                consumeWater(CAULDRON_COST);
                return true;
            }
        }
        return false;
    }

    private boolean tryFillBotaniaApothecary(BlockPos pos) {
        if (!BotaniaCompat.isLoaded() || waterAmount < APOTHECARY_COST) {
            return false;
        }
        if (BotaniaCompat.tryFillApothecary(getLevel(), pos)) {
            consumeWater(APOTHECARY_COST);
            return true;
        }
        return false;
    }

    private void consumeWater(int amount) {
        boolean wasFull = isFull();
        waterAmount -= amount;
        setChanged();
        if (wasFull) {
            sync();
        }
    }

    private BlockPos indexToPos(int index) {
        int x = index % SCAN_SIZE_XZ - SCAN_RADIUS_XZ;
        int y = (index / SCAN_SIZE_XZ) % SCAN_SIZE_Y - SCAN_RADIUS_Y_DOWN;
        int z = (index / (SCAN_SIZE_XZ * SCAN_SIZE_Y)) - SCAN_RADIUS_XZ;
        return getBlockPos().offset(x, y, z);
    }

    @Override
    public TickSetting getTickSetting() {
        return TickSetting.SERVER;
    }

    public boolean isFull() {
        return waterAmount >= MAX_WATER;
    }

    public boolean drainWater(int amount) {
        if (waterAmount >= amount) {
            boolean wasFull = isFull();
            waterAmount -= amount;
            setChanged();
            if (wasFull) {
                sync();
            }
            return true;
        }
        return false;
    }

    @Override
    protected void readNbt(CompoundTag nbt, HolderLookup.Provider pRegistries) {
        this.waterAmount = nbt.getInt("water");
    }

    @Override
    protected void writeNbt(CompoundTag nbt, HolderLookup.Provider pRegistries) {
        nbt.putInt("water", this.waterAmount);
    }
}
