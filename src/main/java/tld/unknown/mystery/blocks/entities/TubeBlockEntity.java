package tld.unknown.mystery.blocks.entities;

import com.google.common.collect.Sets;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import tld.unknown.mystery.api.capabilities.IEssentiaCapability;
import tld.unknown.mystery.registries.ConfigBlockEntities;
import tld.unknown.mystery.registries.ConfigCapabilities;
import tld.unknown.mystery.util.BitPacker;
import tld.unknown.mystery.util.MathUtils;
import tld.unknown.mystery.util.simple.SimpleBlockEntity;
import tld.unknown.mystery.util.simple.TickableBlockEntity;

import java.util.Set;

public class TubeBlockEntity extends SimpleBlockEntity implements IEssentiaCapability, TickableBlockEntity {

    @Getter
    private Set<Direction> disabledDirections;

    private int venting;
    private int checkTimer;

    private ResourceLocation suctionType;
    private int suction;
    private ResourceLocation aspect;
    private int amount;

    public TubeBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ConfigBlockEntities.TUBE.entityType(), pPos, pBlockState);
        this.disabledDirections = Sets.newHashSet();
        this.checkTimer = 0;
    }

    @Override
    protected void readNbt(CompoundTag nbt) {
        this.disabledDirections = BitPacker.readFlags(nbt.getByte("directions"), Direction.class, BitPacker.Length.BYTE);
    }

    @Override
    protected void writeNbt(CompoundTag nbt) {
        nbt.putByte("directions", (byte)BitPacker.encodeFlags(disabledDirections, BitPacker.Length.BYTE));
    }

    private void determineSuction() {
        for(Direction dir : Direction.values()) {
            if(!disabledDirections.contains(dir)) {
                IEssentiaCapability cap = getLevel().getCapability(ConfigCapabilities.ESSENTIA, getBlockPos().offset(dir.getNormal()), dir.getOpposite());
                if(cap != null) {
                    if (cap.compliesToAspect(null, dir.getOpposite())) {
                        if (amount <= 0 || cap.compliesToAspect(null, dir.getOpposite()) || cap.compliesToAspect(aspect, dir.getOpposite())) {
                            if (amount <= 0 || aspect == null || cap.compliesToAspect(null, dir.getOpposite()) || cap.compliesToAspect(aspect, dir.getOpposite())) {
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
                IEssentiaCapability cap = getLevel().getCapability(ConfigCapabilities.ESSENTIA, getBlockPos().offset(dir.getNormal()), dir.getOpposite());
                if(cap != null) {
                    int suck = cap.getSuction(dir.getOpposite());
                    if (this.suction > 0 && (suck == this.suction || suck == this.suction - 1) && cap.compliesToAspect(this.suctionType, dir.getOpposite())) {
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
                IEssentiaCapability cap = getLevel().getCapability(ConfigCapabilities.ESSENTIA, getBlockPos().offset(dir.getNormal()), dir.getOpposite());
                if(cap != null) {
                    if (cap.getSideStatus(dir.getOpposite()).isOutput()) {
                        if ((suctionType == null || suctionType == cap.getEssentiaType(dir.getOpposite()) || cap.getEssentiaType(dir.getOpposite()) == null) &&
                                suction > cap.getSuction(dir.getOpposite()) &&
                                suction >= cap.getMinimumSuction(dir.getOpposite())) {

                            if(suctionType == null) {
                                suctionType = cap.getEssentiaType(dir.getOpposite());
                            }

                            int amountFilled = fillAspect(suctionType, cap.drainAspect(suctionType, 1, dir.getOpposite()), dir.getOpposite());
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
    public ResourceLocation getEssentiaType(Direction dir) {
        return this.aspect;
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
    public ResourceLocation getSuctionType(Direction dir) {
        return suctionType;
    }

    @Override
    public int drainAspect(ResourceLocation aspect, int amount, Direction dir) {
        return 0;
    }

    @Override
    public int fillAspect(ResourceLocation aspect, int amount, Direction dir) {
        return 0;
    }

    @Override
    public boolean canFit(ResourceLocation aspect, int amount, Direction dir) {
        return false;
    }

    @Override
    public boolean contains(ResourceLocation aspect, int amount, Direction dir) {
        return false;
    }

    @Override
    public boolean compliesToAspect(ResourceLocation aspect, Direction dir) {
        return false;
    }
}
