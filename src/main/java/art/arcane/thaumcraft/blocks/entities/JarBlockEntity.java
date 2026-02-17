package art.arcane.thaumcraft.blocks.entities;

import art.arcane.thaumcraft.api.capabilities.IGoggleRendererCapability;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.state.BlockState;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.api.aspects.Aspect;
import art.arcane.thaumcraft.api.capabilities.IEssentiaCapability;
import art.arcane.thaumcraft.blocks.alchemy.JarBlock;
import art.arcane.thaumcraft.registries.ConfigBlockEntities;
import art.arcane.thaumcraft.registries.ConfigCapabilities;
import art.arcane.thaumcraft.registries.ConfigSounds;
import art.arcane.thaumcraft.util.simple.SimpleBlockEntity;
import art.arcane.thaumcraft.util.simple.TickableBlockEntity;

import java.util.Objects;

public class JarBlockEntity extends SimpleBlockEntity implements IEssentiaCapability, IGoggleRendererCapability, TickableBlockEntity {

    private static final int MAX_ESSENTIA = 250;

    @Getter @Setter
    private ResourceKey<Aspect> currentAspect;
    @Getter
    private ResourceKey<Aspect> label;
    @Getter
    private Direction labelDirection;
    private int amount;
    private int checkTimer;

    public JarBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ConfigBlockEntities.JAR.entityType(), pPos, pBlockState);
        this.currentAspect = null;
        this.label = null;
        this.labelDirection = null;
        this.amount = 0;
        this.checkTimer = 0;
    }

    private ResourceKey<Aspect> parseAspectKey(String key) {
        if (key == null || key.isBlank()) {
            return null;
        }
        ResourceLocation location = ResourceLocation.tryParse(key);
        return location == null ? null : ResourceKey.create(ThaumcraftData.Registries.ASPECT, location);
    }

    @Override
    protected void readNbt(CompoundTag nbt, HolderLookup.Provider pRegistries) {
        if (nbt.contains("content")) {
            CompoundTag content = nbt.getCompound("content");
            this.currentAspect = parseAspectKey(content.getString("aspect"));
            this.amount = Math.max(0, content.getInt("amount"));
            if (this.currentAspect == null || this.amount <= 0) {
                this.currentAspect = null;
                this.amount = 0;
            }
        } else {
            this.currentAspect = null;
            this.amount = 0;
        }

        if (nbt.contains("label")) {
            this.label = parseAspectKey(nbt.getString("label"));
            this.labelDirection = Direction.byName(nbt.getString("label_dir"));
            if (this.label == null || this.labelDirection == null) {
                this.label = null;
                this.labelDirection = null;
            }
        } else {
            this.label = null;
            this.labelDirection = null;
        }

        if (nbt.contains("empty")) {
            this.currentAspect = null;
            this.amount = 0;
            this.label = null;
            this.labelDirection = null;
        }
    }

    @Override
    protected void writeNbt(CompoundTag nbt, HolderLookup.Provider pRegistries) {
        if (currentAspect != null && amount > 0) {
            CompoundTag content = new CompoundTag();
            content.putString("aspect", this.currentAspect.location().toString());
            content.putInt("amount", this.amount);
            nbt.put("content", content);
        }
        if (labelDirection != null && label != null) {
            nbt.putString("label", this.label.location().toString());
            nbt.putString("label_dir", this.labelDirection.getSerializedName());
        }
    }

    private boolean isVoid() {
        return ((JarBlock)getBlockState().getBlock()).isVoid();
    }

    public float getFillPercent() {
        return this.amount > 0 ? (float) Math.min(this.amount, MAX_ESSENTIA) / MAX_ESSENTIA : 0;
    }

    public boolean dump() {
        if(this.currentAspect == null)
            return false;
        this.amount = 0;
        this.currentAspect = null;
        if(this.level != null && !this.level.isClientSide()) {
            //TODO: Pollute Aura
            sync();
        }
        return true;
    }

    public boolean applyLabel(Direction dir, ResourceKey<Aspect> filterType) {
        if(this.labelDirection != null)
            return false;
        if(filterType == null && this.currentAspect != null) {
            this.labelDirection = dir;
            this.label = this.currentAspect;
            sync();
            return true;
        } else if(filterType != null && (this.currentAspect == null || Objects.equals(this.currentAspect, filterType))) {
            this.labelDirection = dir;
            this.label = filterType;
            sync();
            return true;
        }
        return false;
    }

    public boolean removeLabel(Direction dir) {
        if(this.labelDirection == null || this.labelDirection != dir)
            return false;
        this.labelDirection = null;
        this.label = null;
        if(this.level != null && !this.level.isClientSide()) {
            //TODO: Spawn Item Entity
            sync();
        }
        return true;
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
        return currentAspect;
    }

    @Override
    public int getMinimumSuction(Direction dir) {
        if(isVoid()) {
            return label == null ? 32 : 48;
        } else {
            return label == null ? 32 : 64;
        }
    }

    @Override
    public ResourceKey<Aspect> getSuctionType(Direction dir) {
        return label != null ? label : currentAspect;
    }

    @Override
    public int getSuction(Direction dir) {
        if(isVoid()) {
            if(label != null && amount < MAX_ESSENTIA) {
                return 48;
            }
            return 32;
        } else {
            if(amount >= MAX_ESSENTIA) {
                return 0;
            }
            return label == null ? 32 : 64;
        }
    }

    @Override
    public int drainAspect(ResourceKey<Aspect> aspect, int amount, Direction dir) {
        if(dir != Direction.UP)
            return 0;
        if(Objects.equals(aspect, currentAspect)) {
            int currentAmount = this.amount;
            if(currentAmount <= amount) {
                this.amount = 0;
                this.currentAspect = null;
                sync();
                return currentAmount;
            }
            this.amount -= amount;
            sync();
            return amount;
        }
        return 0;
    }

    @Override
    public int fillAspect(ResourceKey<Aspect> aspect, int amount, Direction dir) {
        if(dir != Direction.UP)
            return 0;
        if(aspect == null || amount <= 0)
            return 0;
        if (isVoid() && this.currentAspect != null && !Objects.equals(aspect, this.currentAspect)) {
            return 0;
        }
        if(this.currentAspect == null && this.amount == 0) {
            this.currentAspect = aspect;
        }
        if(aspect.equals(currentAspect)) {
            int previousAmount = this.amount;
            if (isVoid()) {
                this.amount = Math.min(MAX_ESSENTIA, this.amount + amount);
                if (this.amount != previousAmount) {
                    sync();
                }
                return amount;
            }
            int addAmount = Math.min(MAX_ESSENTIA - this.amount, amount);
            this.amount += addAmount;
            if (addAmount > 0) {
                sync();
            }
            return addAmount;
        }
        return 0;
    }

    @Override
    public boolean compliesToAspect(ResourceKey<Aspect> aspect, Direction dir) {
        if(dir != Direction.UP) {
            return false;
        }
        return (aspect == null || label == null || Objects.equals(aspect, label))
                && (aspect == null || currentAspect == null || Objects.equals(aspect, currentAspect));
    }

    @Override
    public boolean canFit(ResourceKey<Aspect> aspect, int amount, Direction dir) {
        return amount > 0 && compliesToAspect(aspect, dir) && (isVoid() || amount <= MAX_ESSENTIA - this.amount);
    }

    @Override
    public boolean contains(ResourceKey<Aspect> aspect, int amount, Direction dir) {
        return amount > 0 && (aspect == null || compliesToAspect(aspect, dir)) && this.amount >= amount;
    }

    @Override
    public boolean isContainer(Direction dir) {
        return dir == Direction.UP;
    }

    @Override
    public void onServerTick() {
        if (++this.checkTimer % 5 != 0) {
            return;
        }
        if (!isVoid() && this.amount >= MAX_ESSENTIA) {
            return;
        }
        fillFromAbove();
    }

    private void fillFromAbove() {
        if (this.level == null) {
            return;
        }
        IEssentiaCapability cap = this.level.getCapability(ConfigCapabilities.ESSENTIA, this.worldPosition.above(), Direction.DOWN);
        if (cap == null || !cap.getSideStatus(Direction.DOWN).isOutput()) {
            return;
        }

        ResourceKey<Aspect> targetAspect = null;
        if (this.label != null) {
            targetAspect = this.label;
        } else if (this.currentAspect != null && this.amount > 0) {
            targetAspect = this.currentAspect;
        } else if (cap.getEssentia(Direction.DOWN) > 0
                && cap.getSuction(Direction.DOWN) < getSuction(Direction.UP)
                && getSuction(Direction.UP) >= cap.getMinimumSuction(Direction.DOWN)) {
            targetAspect = cap.getEssentiaType(Direction.DOWN);
        }

        if (targetAspect == null || cap.getSuction(Direction.DOWN) >= getSuction(Direction.UP)) {
            return;
        }

        int drained = cap.drainAspect(targetAspect, 1, Direction.DOWN);
        if (drained <= 0) {
            return;
        }
        int accepted = fillAspect(targetAspect, drained, Direction.UP);
        if (accepted > 0) {
            this.level.playSound(null, this.worldPosition, ConfigSounds.BUBBLE.value(), SoundSource.BLOCKS, 0.08F, 1.2F + this.level.random.nextFloat() * 0.2F);
        }
        if (accepted < drained) {
            cap.fillAspect(targetAspect, drained - accepted, Direction.DOWN);
        }
    }

    @Override
    public TickSetting getTickSetting() {
        return TickSetting.SERVER;
    }
}
