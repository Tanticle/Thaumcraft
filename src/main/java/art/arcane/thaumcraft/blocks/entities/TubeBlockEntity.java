package art.arcane.thaumcraft.blocks.entities;

import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.api.aspects.Aspect;
import art.arcane.thaumcraft.api.capabilities.IEssentiaCapability;
import art.arcane.thaumcraft.blocks.alchemy.TubeBlock;
import art.arcane.thaumcraft.client.fx.ThaumcraftFX;
import art.arcane.thaumcraft.registries.ConfigBlockEntities;
import art.arcane.thaumcraft.registries.ConfigCapabilities;
import art.arcane.thaumcraft.registries.ConfigDataRegistries;
import art.arcane.thaumcraft.registries.ConfigSounds;
import art.arcane.thaumcraft.util.BitPacker;
import art.arcane.thaumcraft.util.simple.SimpleBlockEntity;
import art.arcane.thaumcraft.util.simple.TickableBlockEntity;
import com.google.common.collect.Sets;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;
import java.util.Set;

public class TubeBlockEntity extends SimpleBlockEntity implements IEssentiaCapability, TickableBlockEntity {

    private static final int DEFAULT_VENT_COLOR = 0xAAAAAA;

    @Getter
    private Set<Direction> disabledDirections;

    protected int venting;
    protected int ventColor;
    private int checkTimer;
    private int transferSoundCooldown;
    private int ventSoundCooldown;

    protected ResourceKey<Aspect> suctionType;
    protected int suction;
    protected ResourceKey<Aspect> aspect;
    protected int amount;
    protected Direction facing;

    public TubeBlockEntity(BlockPos pPos, BlockState pBlockState) {
        this(ConfigBlockEntities.TUBE.entityType(), pPos, pBlockState);
    }

    protected TubeBlockEntity(BlockEntityType<?> type, BlockPos pPos, BlockState pBlockState) {
        super(type, pPos, pBlockState);
        this.disabledDirections = Sets.newHashSet();
        this.checkTimer = 0;
        this.facing = Direction.NORTH;
        this.transferSoundCooldown = 0;
        this.ventSoundCooldown = 0;
        this.ventColor = DEFAULT_VENT_COLOR;
    }

    @Override
    protected void readNbt(CompoundTag nbt, HolderLookup.Provider pRegistries) {
        this.disabledDirections = BitPacker.readFlags(nbt.getByte("directions"), Direction.class, BitPacker.Length.BYTE);
        this.suctionType = parseAspectKey(nbt.getString("suction_type"));
        this.suction = nbt.getInt("suction");
        this.aspect = parseAspectKey(nbt.getString("content_type"));
        this.amount = nbt.getInt("content_amount");
        this.facing = nbt.contains("facing") ? Direction.byName(nbt.getString("facing")) : Direction.NORTH;
        this.venting = Math.max(0, nbt.getInt("venting"));
        this.ventColor = nbt.contains("vent_color") ? nbt.getInt("vent_color") : DEFAULT_VENT_COLOR;
        if (this.facing == null) {
            this.facing = Direction.NORTH;
        }
    }

    @Override
    protected void writeNbt(CompoundTag nbt, HolderLookup.Provider pRegistries) {
        nbt.putByte("directions", (byte) BitPacker.encodeFlags(disabledDirections, BitPacker.Length.BYTE));
        if (this.suctionType != null) {
            nbt.putString("suction_type", this.suctionType.location().toString());
        }
        nbt.putInt("suction", this.suction);
        if (this.aspect != null) {
            nbt.putString("content_type", this.aspect.location().toString());
        }
        nbt.putInt("content_amount", this.amount);
        nbt.putString("facing", this.facing.getSerializedName());
        nbt.putInt("venting", this.venting);
        nbt.putInt("vent_color", this.ventColor);
    }

    protected ResourceKey<Aspect> parseAspectKey(String key) {
        if (key == null || key.isBlank()) {
            return null;
        }
        ResourceLocation location = ResourceLocation.tryParse(key);
        return location == null ? null : ResourceKey.create(ThaumcraftData.Registries.ASPECT, location);
    }

    public boolean isDirectionDisabled(Direction direction) {
        return direction != null && disabledDirections.contains(direction);
    }

    public boolean isConnectable(Direction direction) {
        return direction == null || !isDirectionDisabled(direction);
    }

    public Direction getFacing() {
        return this.facing;
    }

    public boolean supportsFacingControl() {
        return false;
    }

    public boolean toggleSide(Direction direction) {
        if (direction == null) {
            return false;
        }
        if (disabledDirections.contains(direction)) {
            disabledDirections.remove(direction);
        } else {
            disabledDirections.add(direction);
        }
        syncAdjacentTubeSide(direction, isConnectable(direction));
        onConnectivityChanged();
        return true;
    }

    private void syncAdjacentTubeSide(Direction direction, boolean open) {
        if (this.level == null || direction == null) {
            return;
        }
        BlockPos neighborPos = this.worldPosition.offset(direction.getUnitVec3i());
        if (!(this.level.getBlockEntity(neighborPos) instanceof TubeBlockEntity neighborTube)) {
            return;
        }
        Direction opposite = direction.getOpposite();
        boolean neighborOpen = neighborTube.isConnectable(opposite);
        if (neighborOpen == open) {
            return;
        }
        if (open) {
            neighborTube.disabledDirections.remove(opposite);
        } else {
            neighborTube.disabledDirections.add(opposite);
        }
        neighborTube.onConnectivityChanged();
    }

    public boolean cycleFacing() {
        if (!supportsFacingControl()) {
            return false;
        }
        this.facing = Direction.from3DDataValue((this.facing.get3DDataValue() + 1) % Direction.values().length);
        onConnectivityChanged();
        return true;
    }

    public void setFacing(Direction direction) {
        if (!supportsFacingControl() || direction == null) {
            return;
        }
        this.facing = direction;
        setChanged();
    }

    protected void onConnectivityChanged() {
        setChanged();
        if (this.level == null) {
            return;
        }
        BlockState state = this.level.getBlockState(this.worldPosition);
        if (state.getBlock() instanceof TubeBlock tubeBlock) {
            this.level.setBlock(this.worldPosition, tubeBlock.determineConnections(this.level, state, this.worldPosition), 1 | 2);
        }
        for (Direction direction : Direction.values()) {
            this.level.updateNeighborsAt(this.worldPosition.relative(direction), this.getBlockState().getBlock());
        }
        sync();
    }

    protected boolean canUpdateFlow() {
        return true;
    }

    protected boolean isDirectionalSuction() {
        return false;
    }

    protected boolean isDirectionalFlow() {
        return false;
    }

    protected int getReducedSuction(int suction) {
        return suction - 1;
    }

    protected ResourceKey<Aspect> getAspectFilter() {
        return null;
    }

    protected void playTransferSound() {
        if (this.level == null || this.level.isClientSide() || this.transferSoundCooldown > 0) {
            return;
        }
        this.level.playSound(null, this.worldPosition, ConfigSounds.BUBBLE.value(), SoundSource.BLOCKS, 0.1F, 1.15F + this.level.random.nextFloat() * 0.25F);
        if (this.level.random.nextInt(80) == 0) {
            this.level.playSound(null, this.worldPosition, ConfigSounds.CREAK.value(), SoundSource.BLOCKS, 0.55F, 1.0F + this.level.random.nextFloat() * 0.2F);
        }
        this.transferSoundCooldown = 2;
    }

    protected void playConflictSound() {
        if (this.level == null || this.level.isClientSide() || this.ventSoundCooldown > 0) {
            return;
        }
        this.level.playSound(null, this.worldPosition, ConfigSounds.CREAK.value(), SoundSource.BLOCKS, 0.6F, 0.85F + this.level.random.nextFloat() * 0.25F);
        this.ventSoundCooldown = 10;
    }

    protected void playVentingSound() {
        if (this.level == null || this.level.isClientSide() || this.venting <= 0 || this.ventSoundCooldown > 0) {
            return;
        }
        this.level.playSound(null, this.worldPosition, ConfigSounds.CREAK.value(), SoundSource.BLOCKS, 0.45F, 0.95F + this.level.random.nextFloat() * 0.15F);
        this.ventSoundCooldown = 8;
    }

    private int resolveVentColor(ResourceKey<Aspect> key) {
        if (this.level == null || key == null) {
            return DEFAULT_VENT_COLOR;
        }
        try {
            return ConfigDataRegistries.ASPECTS.get(this.level.registryAccess(), key).colour().argb32(false) & 0xFFFFFF;
        } catch (Exception ignored) {
            return DEFAULT_VENT_COLOR;
        }
    }

    private void spawnVentingParticles() {
        if (this.level == null || !this.level.isClientSide()) {
            return;
        }
        double x = this.worldPosition.getX() + 0.5;
        double y = this.worldPosition.getY() + 0.5;
        double z = this.worldPosition.getZ() + 0.5;
        // TC6 uses a stable randomized vent direction per tube (seeded from tile hash).
        RandomSource dirRandom = RandomSource.create((long) this.hashCode() * 4L);
        float rp = dirRandom.nextFloat() * 360.0F;
        float ry = dirRandom.nextFloat() * 360.0F;
        double fx = -Mth.sin(ry / 180.0F * (float) Math.PI) * Mth.cos(rp / 180.0F * (float) Math.PI);
        double fz = Mth.cos(ry / 180.0F * (float) Math.PI) * Mth.cos(rp / 180.0F * (float) Math.PI);
        double fy = -Mth.sin(rp / 180.0F * (float) Math.PI);
        ThaumcraftFX.drawVentParticles(x, y, z, fx / 5.0, fy / 5.0, fz / 5.0, this.ventColor);
    }

    protected void determineSuction() {
        this.suction = 0;
        this.suctionType = null;
        if (!canUpdateFlow()) {
            return;
        }
        ResourceKey<Aspect> filter = getAspectFilter();
        for (Direction dir : Direction.values()) {
            if (!isConnectable(dir)) {
                continue;
            }
            if (isDirectionalSuction() && this.facing != dir.getOpposite()) {
                continue;
            }
            IEssentiaCapability cap = getLevel().getCapability(ConfigCapabilities.ESSENTIA, getBlockPos().offset(dir.getUnitVec3i()), dir.getOpposite());
            if (cap == null) {
                continue;
            }
            ResourceKey<Aspect> neighborSuctionType = cap.getSuctionType(dir.getOpposite());
            if (filter != null && neighborSuctionType != null && !Objects.equals(filter, neighborSuctionType)) {
                continue;
            }
            if (filter == null && this.amount > 0 && neighborSuctionType != null && !Objects.equals(this.aspect, neighborSuctionType)) {
                continue;
            }

            int neighborSuction = cap.getSuction(dir.getOpposite());
            if (neighborSuction > 0 && neighborSuction > this.suction + 1) {
                this.suctionType = neighborSuctionType == null ? filter : neighborSuctionType;
                this.suction = Math.max(0, getReducedSuction(neighborSuction));
            }
        }
    }

    protected void checkConflicts() {
        for (Direction dir : Direction.values()) {
            if (!isConnectable(dir)) {
                continue;
            }
            IEssentiaCapability cap = getLevel().getCapability(ConfigCapabilities.ESSENTIA, getBlockPos().offset(dir.getUnitVec3i()), dir.getOpposite());
            if (cap == null) {
                continue;
            }
            int suck = cap.getSuction(dir.getOpposite());
            BlockEntity be = getLevel().getBlockEntity(getBlockPos().offset(dir.getUnitVec3i()));
            if (this.suction > 0
                    && (suck == this.suction || suck == this.suction - 1)
                    && !Objects.equals(this.suctionType, cap.getSuctionType(dir.getOpposite()))
                    && !(be instanceof TubeFilterBlockEntity)) {
                this.ventColor = resolveVentColor(this.suctionType);
                this.venting = 50;
                playConflictSound();
                sync();
                return;
            }
        }
    }

    protected void flow() {
        if (!canUpdateFlow() || this.amount > 0) {
            return;
        }
        for (Direction dir : Direction.values()) {
            if (!isConnectable(dir)) {
                continue;
            }
            if (isDirectionalFlow() && this.facing == dir.getOpposite()) {
                continue;
            }
            IEssentiaCapability cap = getLevel().getCapability(ConfigCapabilities.ESSENTIA, getBlockPos().offset(dir.getUnitVec3i()), dir.getOpposite());
            if (cap == null || !cap.getSideStatus(dir.getOpposite()).isOutput()) {
                continue;
            }
            ResourceKey<Aspect> targetAspect = cap.getEssentiaType(dir.getOpposite());
            if ((suctionType == null || Objects.equals(suctionType, targetAspect) || targetAspect == null)
                    && suction > cap.getSuction(dir.getOpposite())
                    && suction >= cap.getMinimumSuction(dir.getOpposite())) {
                ResourceKey<Aspect> pullType = suctionType;
                if (pullType == null) {
                    pullType = targetAspect;
                    if (pullType == null) {
                        pullType = cap.getEssentiaType(null);
                    }
                }
                if (pullType == null) {
                    continue;
                }
                int drained = cap.drainAspect(pullType, 1, dir.getOpposite());
                if (drained <= 0) {
                    continue;
                }
                int amountFilled = fillAspect(pullType, drained, dir);
                if (amountFilled > 0) {
                    playTransferSound();
                    return;
                }
                if (drained > amountFilled) {
                    cap.fillAspect(pullType, drained - amountFilled, dir.getOpposite());
                }
            }
        }
    }

    /* -------------------------------------------------------------------------------------------------------------- */
    /*                                         Tick-able BlockEntity Methods                                          */
    /* -------------------------------------------------------------------------------------------------------------- */

    @Override
    public void onServerTick() {
        if (transferSoundCooldown > 0) {
            transferSoundCooldown--;
        }
        if (ventSoundCooldown > 0) {
            ventSoundCooldown--;
        }
        if (venting > 0) {
            venting--;
            playVentingSound();
        }
        if (checkTimer == 0) {
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
        if (venting > 0) {
            venting--;
            spawnVentingParticles();
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
        if (!isConnectable(dir)) {
            return 0;
        }
        return this.amount;
    }

    @Override
    public ResourceKey<Aspect> getEssentiaType(Direction dir) {
        if (!isConnectable(dir)) {
            return null;
        }
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
        if (!isConnectable(dir)) {
            return 0;
        }
        return suction;
    }

    @Override
    public ResourceKey<Aspect> getSuctionType(Direction dir) {
        if (!isConnectable(dir)) {
            return null;
        }
        return suctionType;
    }

    @Override
    public int drainAspect(ResourceKey<Aspect> aspect, int amount, Direction dir) {
        if (!isConnectable(dir) || aspect == null || amount <= 0 || this.aspect == null || this.amount <= 0 || !this.aspect.equals(aspect)) {
            return 0;
        }
        int drained = Math.min(amount, this.amount);
        this.amount -= drained;
        if (this.amount <= 0) {
            this.amount = 0;
            this.aspect = null;
        }
        setChanged();
        return drained;
    }

    @Override
    public int fillAspect(ResourceKey<Aspect> aspect, int amount, Direction dir) {
        if (!isConnectable(dir) || aspect == null || amount <= 0 || !compliesToAspect(aspect, dir)) {
            return 0;
        }
        if (this.amount == 0) {
            int filled = Math.min(1, amount);
            this.aspect = aspect;
            this.amount = filled;
            setChanged();
            return filled;
        }
        if (!aspect.equals(this.aspect) || this.amount >= 1) {
            return 0;
        }
        int filled = Math.min(1 - this.amount, amount);
        if (filled > 0) {
            this.amount += filled;
            setChanged();
        }
        return filled;
    }

    @Override
    public boolean canFit(ResourceKey<Aspect> aspect, int amount, Direction dir) {
        if (!isConnectable(dir)) {
            return false;
        }
        if (amount <= 0) {
            return true;
        }
        if (this.amount == 0) {
            return true;
        }
        return aspect != null && aspect.equals(this.aspect) && this.amount + amount <= 1;
    }

    @Override
    public boolean contains(ResourceKey<Aspect> aspect, int amount, Direction dir) {
        if (!isConnectable(dir)) {
            return false;
        }
        if (amount <= 0) {
            return true;
        }
        if (this.amount < amount) {
            return false;
        }
        return aspect == null || Objects.equals(aspect, this.aspect);
    }

    @Override
    public boolean compliesToAspect(ResourceKey<Aspect> aspect, Direction dir) {
        if (!isConnectable(dir)) {
            return false;
        }
        return aspect == null || this.aspect == null || Objects.equals(aspect, this.aspect);
    }

    @Override
    public boolean isContainer(Direction dir) {
        return false;
    }
}
