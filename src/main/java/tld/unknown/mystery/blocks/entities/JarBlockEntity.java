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
import tld.unknown.mystery.api.aspects.AspectContainer;
import tld.unknown.mystery.api.capabilities.IEssentiaCapability;
import tld.unknown.mystery.blocks.JarBlock;
import tld.unknown.mystery.data.aspects.AspectList;
import tld.unknown.mystery.registries.ConfigBlockEntities;
import tld.unknown.mystery.util.simple.SimpleBlockEntity;

public class JarBlockEntity extends SimpleBlockEntity implements AspectContainer, IEssentiaCapability {

    private static final int MAX_ESSENTIA = 250;

    @Getter @Setter
    private ResourceKey<Aspect> currentAspect;
    @Getter
    private ResourceKey<Aspect> label;
    @Getter
    private Direction labelDirection;
    private int amount;

    public JarBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ConfigBlockEntities.JAR.entityType(), pPos, pBlockState);
        this.currentAspect = null;
        this.label = null;
        this.labelDirection = null;
        this.amount = 0;
    }

    @Override
    protected void readNbt(CompoundTag nbt, HolderLookup.Provider pRegistries) {
        if(nbt.contains("content")) {
            CompoundTag content = nbt.getCompound("content");
            this.currentAspect = ResourceKey.create(ThaumcraftData.Registries.ASPECT, ResourceLocation.tryParse(content.getString("aspect")));
            this.amount = content.getInt("amount");
        }
        if(nbt.contains("label")) {
            this.label = ResourceKey.create(ThaumcraftData.Registries.ASPECT, ResourceLocation.tryParse(nbt.getString("label")));
            this.labelDirection = Direction.byName(nbt.getString("label_dir"));
        }
    }

    @Override
    protected void writeNbt(CompoundTag nbt, HolderLookup.Provider pRegistries) {
        if(amount > 0) {
            CompoundTag content = new CompoundTag();
            content.putString("aspect", this.currentAspect.location().toString());
            content.putInt("amount", this.amount);
            nbt.put("content", content);
        }
        if(labelDirection != null) {
            nbt.putString("label", this.label.location().toString());
            nbt.putString("label_dir", this.labelDirection.getSerializedName());
        }
    }

    private boolean isVoid() {
        return ((JarBlock)getBlockState().getBlock()).isVoid();
    }

    public float getFillPercent() {
        return (float)this.amount / MAX_ESSENTIA;
    }

    public void dump() {
        this.amount = 0;
        this.currentAspect = null;
        //TODO: Pollute Aura
        sync();
    }

    public boolean applyLabel(Direction dir) {
        if(this.labelDirection != null || amount <= 0)
            return false;
        this.labelDirection = dir;
        this.label = this.currentAspect;
        sync();
        return true;
    }

    public boolean removeLabel(Direction dir) {
        if(this.labelDirection == null || this.labelDirection != dir)
            return false;
        this.labelDirection = null;
        this.label = null;
        sync();
        return true;
    }

    /* -------------------------------------------------------------------------------------------------------------- */
    /*                                            Aspect Container Methods                                            */
    /* -------------------------------------------------------------------------------------------------------------- */

    @Override
    public int getAspectCount(ResourceKey<Aspect> aspect) {
        return aspect.equals(currentAspect) ? amount : 0;
    }

    @Override
    public AspectList getAspectList() {
        return new AspectList().add(currentAspect, amount);
    }

    @Override
    public int drainAspect(ResourceKey<Aspect> aspect) {
        int essentia = this.amount;
        this.amount = 0;
        return essentia;
    }

    @Override
    public int drainAspect(ResourceKey<Aspect> aspect, int amount) {
        if(aspect.equals(currentAspect)) {
            int currentAmount = this.amount;
            if(currentAmount <= amount) {
                this.amount = 0;
                this.currentAspect = null;
                return currentAmount;
            }
            this.amount -= amount;
            return amount;
        }
        return 0;
    }

    //TODO: Void Effect & Flux
    @Override
    public int addAspect(ResourceKey<Aspect> aspect, int amount) {
        if(this.currentAspect == null && this.amount == 0) {
            this.currentAspect = aspect;
            this.amount = Math.min(MAX_ESSENTIA, amount);
            return this.amount;
        } else if(aspect.equals(currentAspect)) {
            int addAmount = Math.min(MAX_ESSENTIA - this.amount, amount);
            this.amount += addAmount;
            return addAmount;
        }
        return 0;
    }

    /* -------------------------------------------------------------------------------------------------------------- */
    /*                                         Essentia Capability Methods                                            */
    /* -------------------------------------------------------------------------------------------------------------- */

    @Override
    public SideStatus getSideStatus(Direction dir) {
        return SideStatus.INPUT_OUTPUT;
    }

    @Override
    public int getEssentia(Direction dir) {
        return amount;
    }

    @Override
    public ResourceKey<Aspect> getEssentiaType(Direction dir) {
        return label != null ? label : currentAspect;
    }

    @Override
    public int getMinimumSuction(Direction dir) {
        if(isVoid()) {
            return label == null ? 48 : 32;
        } else {
            return label == null ? 64 : 32;
        }
    }

    @Override
    public ResourceKey<Aspect> getSuctionType(Direction dir) {
        return getEssentiaType(dir);
    }

    @Override
    public int getSuction(Direction dir) {
        if(isVoid()) {
            return label == null ? 48 : 32;
        } else {
            if(amount >= MAX_ESSENTIA) {
                return 0;
            }
            return label == null ? 64 : 32;
        }
    }

    @Override
    public int drainAspect(ResourceKey<Aspect> aspect, int amount, Direction dir) {
        return dir == Direction.UP ? drainAspect(aspect, amount) : 0;
    }

    @Override
    public int fillAspect(ResourceKey<Aspect> aspect, int amount, Direction dir) {
        return dir == Direction.UP ? addAspect(aspect, amount) : 0;
    }

    @Override
    public boolean compliesToAspect(ResourceKey<Aspect> aspect, Direction dir) {
        return dir == Direction.UP && (label == null || aspect.equals(label)) && (currentAspect == null || aspect.equals(currentAspect));
    }

    @Override
    public boolean canFit(ResourceKey<Aspect> aspect, int amount, Direction dir) {
        return compliesToAspect(aspect, dir) && (isVoid() || amount <= MAX_ESSENTIA - this.amount);
    }

    @Override
    public boolean contains(ResourceKey<Aspect> aspect, int amount, Direction dir) {
        return (aspect == null || compliesToAspect(aspect, dir)) && this.amount >= amount;
    }
}
