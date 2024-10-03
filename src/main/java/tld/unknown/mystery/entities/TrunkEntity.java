package tld.unknown.mystery.entities;

import lombok.Getter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ChestLidController;
import tld.unknown.mystery.menus.TrunkMenu;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class TrunkEntity extends Mob implements HasCustomInventoryScreen, OwnableEntity {

    private static final int SIZE_STANDARD = 27;
    public static final int SIZE_UPGRADED = 36;

    private static final EntityDataAccessor<Byte> UPGRADES = SynchedEntityData.defineId(TrunkEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> STAY = SynchedEntityData.defineId(TrunkEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(TrunkEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Boolean> OPENED = SynchedEntityData.defineId(TrunkEntity.class, EntityDataSerializers.BOOLEAN);

    private final ChestLidController lidController;

    @Getter
    private SimpleContainer inventory;
    private short openStatus = 0;

    public TrunkEntity(EntityType<? extends Mob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.inventory = new SimpleContainer(SIZE_STANDARD);
        this.lidController = new ChestLidController();
    }

    @Override
    protected void registerGoals() {

    }

    @Override
    public void tick() {
        super.tick();
        lidController.shouldBeOpen(isOpen());
        lidController.tickLid();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putByte("Upgrades", getUpgradeByte());
        pCompound.putBoolean("Stay", shouldSit());
        pCompound.put("Contents", inventory.createTag(level().registryAccess()));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        setUpgradeByte(pCompound.getByte("Upgrades"));
        setSit(pCompound.getBoolean("Stay"));
        inventory = new SimpleContainer(isSizeUpgraded() ? SIZE_UPGRADED : SIZE_STANDARD);
        inventory.fromTag(pCompound.getList("Contents", ListTag.TAG_COMPOUND), level().registryAccess());
    }

    @Override
    protected InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        this.openCustomInventoryScreen(pPlayer);
        return InteractionResult.sidedSuccess(level().isClientSide);
    }

    @Override
    public void openCustomInventoryScreen(Player pPlayer) {
        if(!this.level().isClientSide()) {
            updateOpenStatus(false);
            MenuConstructor menu = (id, inv, p) -> TrunkMenu.create(id, inv, this);
            pPlayer.openMenu(new SimpleMenuProvider(menu, getTypeName()));
        }
    }

    @Override
    public LivingEntity getOwner() {
        UUID uuid = this.getOwnerUUID();
        return uuid == null ? null : this.level().getPlayerByUUID(uuid);
    }

    public boolean isSizeUpgraded() {
        return false; //ChaumtraftItems.UPGRADE_CAPACITY.get().isBitSet(getUpgradeByte());
    }

    public float getLidProgress(float pPartialTicks) {
        return lidController.getOpenness(pPartialTicks);
    }

    /* -------------------------------------------------------------------------------------------------------------- */
    /*                                             Synced Data Methods                                                */
    /* -------------------------------------------------------------------------------------------------------------- */

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(UPGRADES, (byte)0)
                .define(STAY, false)
                .define(OWNER_UUID, Optional.empty())
                .define(OPENED, false);
    }

    public byte getUpgradeByte() {
        return getEntityData().get(UPGRADES);
    }

    public void setUpgradeByte(byte b) {
        getEntityData().set(UPGRADES, b);
    }

    @Override
    public UUID getOwnerUUID() {
        return getEntityData().get(OWNER_UUID).orElse(null);
    }

    public void setSit(boolean stay) {
        getEntityData().set(STAY, stay);
    }

    public boolean shouldSit() {
        return getEntityData().get(STAY);
    }

    public boolean isOpen() {
        return getEntityData().get(OPENED);
    }

    public void updateOpenStatus(boolean close) {
        if(!level().isClientSide()) {
            short newStatus;
            if(close) {
                newStatus = (short) Math.max(openStatus - 1, 0);
                if(newStatus == 0 && openStatus > 0) {
                    getEntityData().set(OPENED, false);
                    level().playSound(null, this, SoundEvents.CHEST_CLOSE, SoundSource.NEUTRAL, 1, 1);
                }
            } else {
                newStatus = (short)(openStatus + 1);
                if(newStatus > 0 && openStatus == 0) {
                    getEntityData().set(OPENED, true);
                    level().playSound(null, this, SoundEvents.CHEST_OPEN, SoundSource.NEUTRAL, 1, 1);
                }
            }
            openStatus = newStatus;
        }
    }

    /* -------------------------------------------------------------------------------------------------------------- */
    /*                                             Mob Entity Methods                                                 */
    /* -------------------------------------------------------------------------------------------------------------- */

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return Set.of();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot pSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot pSlot, ItemStack pStack) { }

    @Override
    public HumanoidArm getMainArm() {
        return null;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity entity) {
        return new ClientboundAddEntityPacket(this, entity);
    }
}
