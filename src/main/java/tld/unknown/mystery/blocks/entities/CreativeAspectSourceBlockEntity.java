package tld.unknown.mystery.blocks.entities;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import tld.unknown.mystery.api.ThaumcraftData;
import tld.unknown.mystery.api.aspects.Aspect;
import tld.unknown.mystery.api.capabilities.IEssentiaCapability;
import tld.unknown.mystery.registries.ConfigBlockEntities;
import tld.unknown.mystery.util.simple.SimpleBlockEntity;

public class CreativeAspectSourceBlockEntity extends SimpleBlockEntity implements IEssentiaCapability {

    private static final int FILLED_AMOUNT = 250;

    @Getter
    @Setter
    private ResourceKey<Aspect> aspect;

    public CreativeAspectSourceBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ConfigBlockEntities.CREATIVE_ASPECT_SOURCE.entityType(), pPos, pBlockState);
    }

    @Override
    protected void readNbt(CompoundTag nbt, HolderLookup.Provider pRegistries) {
        if(nbt.contains("aspect")) {
            this.aspect = ResourceKey.create(ThaumcraftData.Registries.ASPECT, ResourceLocation.tryParse(nbt.getString("aspect")));
        }
    }

    @Override
    protected void writeNbt(CompoundTag nbt, HolderLookup.Provider pRegistries) {
        if(aspect != null) {
            nbt.putString("aspect", aspect.location().toString());
        }
    }

    /* -------------------------------------------------------------------------------------------------------------- */
    /*                                         Essentia Capability Methods                                            */
    /* -------------------------------------------------------------------------------------------------------------- */

    @Override
    public SideStatus getSideStatus(Direction dir) {
        return SideStatus.OUTPUT;
    }

    @Override
    public int getEssentia(Direction dir) {
        return aspect == null ? 0 : FILLED_AMOUNT;
    }

    @Override
    public ResourceKey<Aspect> getEssentiaType(Direction dir) {
        return aspect;
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
        return compliesToAspect(aspect, dir) ? Math.min(amount, FILLED_AMOUNT) : 0;
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
        return compliesToAspect(aspect, dir) && amount <= FILLED_AMOUNT;
    }

    @Override
    public boolean compliesToAspect(ResourceKey<Aspect> aspect, Direction dir) {
        return aspect.equals(this.aspect);
    }

    @Override
    public boolean isContainer(Direction dir) {
        return true;
    }
}
