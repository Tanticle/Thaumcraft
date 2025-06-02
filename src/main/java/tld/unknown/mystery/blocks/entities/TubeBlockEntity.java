package tld.unknown.mystery.blocks.entities;

import com.google.common.collect.Sets;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.state.BlockState;
import tld.unknown.mystery.api.aspects.Aspect;
import tld.unknown.mystery.api.capabilities.IEssentiaCapability;
import tld.unknown.mystery.registries.ConfigBlockEntities;
import tld.unknown.mystery.registries.ConfigCapabilities;
import tld.unknown.mystery.registries.ConfigDataRegistries;
import tld.unknown.mystery.util.BitPacker;
import tld.unknown.mystery.util.simple.SimpleBlockEntity;
import tld.unknown.mystery.util.simple.TickableBlockEntity;

import java.util.Set;

public class TubeBlockEntity extends SimpleBlockEntity implements IEssentiaCapability, TickableBlockEntity {

    @Getter
    private Set<Direction> disabledDirections;

    private int venting;
    private int checkTimer;

    private Holder<Aspect> suctionType;
    private int suction;
    private Holder<Aspect> aspect;
    private int amount;

    public TubeBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ConfigBlockEntities.TUBE.entityType(), pPos, pBlockState);
        this.disabledDirections = Sets.newHashSet();
        this.checkTimer = 0;
    }

    @Override
    protected void readNbt(CompoundTag nbt, HolderLookup.Provider pRegistries) {
        this.disabledDirections = BitPacker.readFlags(nbt.getByte("directions"), Direction.class, BitPacker.Length.BYTE);
    }

    @Override
    protected void writeNbt(CompoundTag nbt, HolderLookup.Provider pRegistries) {
        nbt.putByte("directions", (byte)BitPacker.encodeFlags(disabledDirections, BitPacker.Length.BYTE));
    }

    private void determineSuction() {
        for(Direction dir : Direction.values()) {
            if(!disabledDirections.contains(dir)) {
                IEssentiaCapability cap = getLevel().getCapability(ConfigCapabilities.ESSENTIA, getBlockPos().offset(dir.getUnitVec3i()), dir.getOpposite());
                if(cap != null) {
                    if (cap.compliesToAspect(null, dir.getOpposite())) {
                        if (amount <= 0 || cap.compliesToAspect(null, dir.getOpposite()) || cap.compliesToAspect(aspect.unwrapKey().get(), dir.getOpposite())) {
                            if (amount <= 0 || aspect == null || cap.compliesToAspect(null, dir.getOpposite()) || cap.compliesToAspect(aspect.unwrapKey().get(), dir.getOpposite())) {
                                int suck = cap.getSuction(dir.getOpposite());
                                if (suck > 0 && suck > this.suction + 1) {
                                    this.suction = suck - 1;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void checkConflicts() {
        for(Direction dir : Direction.values()) {
            if(!disabledDirections.contains(dir)) {
                IEssentiaCapability cap = getLevel().getCapability(ConfigCapabilities.ESSENTIA, getBlockPos().offset(dir.getUnitVec3i()), dir.getOpposite());
                if(cap != null) {
                    int suck = cap.getSuction(dir.getOpposite());
                    if (this.suction > 0 && (suck == this.suction || suck == this.suction - 1) && cap.compliesToAspect(this.suctionType.unwrapKey().get(), dir.getOpposite())) {
                        // Dispatch Venting Particles
                        this.venting = 40;
                    }
                }
            }
        }
    }

    private  void flow() {
        if(this.amount == 0) {
            return;
        }
        for(Direction dir : Direction.values()) {
            if(!disabledDirections.contains(dir)) {
                IEssentiaCapability cap = getLevel().getCapability(ConfigCapabilities.ESSENTIA, getBlockPos().offset(dir.getUnitVec3i()), dir.getOpposite());
                if(cap != null) {
                    if (cap.getSideStatus(dir.getOpposite()).isOutput()) {
                        if ((suctionType == null || suctionType == cap.getEssentiaType(dir.getOpposite()) || cap.getEssentiaType(dir.getOpposite()) == null) &&
                                suction > cap.getSuction(dir.getOpposite()) &&
                                suction >= cap.getMinimumSuction(dir.getOpposite())) {

                            if(suctionType == null) {
                                suctionType = ConfigDataRegistries.ASPECTS.getHolder(level.registryAccess(), cap.getEssentiaType(dir.getOpposite()));
                            }

                            int amountFilled = fillAspect(suctionType.unwrapKey().get(), cap.drainAspect(suctionType.unwrapKey().get(), 1, dir.getOpposite()), dir.getOpposite());
                            if (amountFilled > 0) {
                                //TODO: ??? Fill effect?
                                /*if (this.world.rand.nextInt(100) == 0)
                                    this.world.addBlockEvent(this.pos, BlocksTC.tube, 0, 0);*/
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    /* -------------------------------------------------------------------------------------------------------------- */
    /*                                         Tick-able BlockEntity Methods                                          */
    /* -------------------------------------------------------------------------------------------------------------- */

    @Override
    public void onServerTick() {
        if(venting >= 0) {
            venting--;
        }
        if(checkTimer == 0) {
            this.checkTimer = level.getRandom().nextInt(10);
        }
        if (this.venting <= 0) {
            if (++this.checkTimer % 2 == 0) {
                determineSuction();
                checkConflicts();
                if (this.aspect != null && this.amount == 0) {
                    this.aspect = null;
                }
            }
            if (this.checkTimer % 5 == 0 && this.suction > 0) {
                flow();
            }
        }
    }

    @Override
    public void onClientTick() {
        if(venting >= 0) {
            venting--;
            //TODO Venting Particles
        }
    }

    @Override
    public TickSetting getTickSetting() {
        return TickSetting.SERVER_AND_CLIENT;
    }

    /* -------------------------------------------------------------------------------------------------------------- */
    /*                                         Essentia Capability Methods                                            */
    /* -------------------------------------------------------------------------------------------------------------- */

    @Override
    public int getEssentia(Direction dir) {
        return this.amount;
    }

    @Override
    public ResourceKey<Aspect> getEssentiaType(Direction dir) {
        return this.aspect.unwrapKey().get();
    }

    @Override
    public int getMinimumSuction(Direction dir) {
        return 0;
    }

    @Override
    public SideStatus getSideStatus(Direction dir) {
        return SideStatus.INPUT_OUTPUT;
    }

    @Override
    public int getSuction(Direction dir) {
        return suction;
    }

    @Override
    public ResourceKey<Aspect> getSuctionType(Direction dir) {
        return suctionType == null ? null : suctionType.unwrapKey().get();
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
        return false;
    }

    @Override
    public boolean compliesToAspect(ResourceKey<Aspect> aspect, Direction dir) {
        return false;
    }
}
