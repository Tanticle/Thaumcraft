package art.arcane.thaumcraft.entities.golem;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.data.golemancy.GolemMaterial;
import art.arcane.thaumcraft.data.golemancy.GolemPart;
import art.arcane.thaumcraft.data.golemancy.GolemTrait;
import art.arcane.thaumcraft.entities.golem.ai.GolemExecuteTaskGoal;
import art.arcane.thaumcraft.entities.golem.ai.GolemFighterHurtByTargetGoal;
import art.arcane.thaumcraft.entities.golem.ai.GolemFollowOwnerGoal;
import art.arcane.thaumcraft.entities.golem.ai.GolemMeleeAttackGoal;
import art.arcane.thaumcraft.entities.golem.ai.GolemOwnerHurtByTargetGoal;
import art.arcane.thaumcraft.entities.golem.ai.GolemOwnerHurtTargetGoal;
import art.arcane.thaumcraft.entities.golem.ai.GolemRangedAttackGoal;
import art.arcane.thaumcraft.entities.golem.ai.GolemReturnHomeGoal;
import art.arcane.thaumcraft.registries.ConfigDataRegistries;
import art.arcane.thaumcraft.registries.ConfigSounds;

import java.util.*;

public class GolemEntity extends PathfinderMob {

    private static final EntityDataAccessor<String> DATA_MATERIAL = SynchedEntityData.defineId(GolemEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> DATA_HEAD = SynchedEntityData.defineId(GolemEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> DATA_ARMS = SynchedEntityData.defineId(GolemEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> DATA_LEGS = SynchedEntityData.defineId(GolemEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> DATA_ADDON = SynchedEntityData.defineId(GolemEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> DATA_RANK = SynchedEntityData.defineId(GolemEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> DATA_COLOR = SynchedEntityData.defineId(GolemEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Optional<UUID>> DATA_OWNER = SynchedEntityData.defineId(GolemEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Boolean> DATA_FOLLOWING = SynchedEntityData.defineId(GolemEntity.class, EntityDataSerializers.BOOLEAN);

    private static final ResourceLocation HEALTH_MODIFIER_ID = Thaumcraft.id("golem_material_health");
    private static final ResourceLocation ARMOR_MODIFIER_ID = Thaumcraft.id("golem_material_armor");
    private static final ResourceLocation DAMAGE_MODIFIER_ID = Thaumcraft.id("golem_material_damage");

    private int rankXp;
    private BlockPos homePos;
    private final SimpleContainer inventory;
    private Set<ResourceKey<GolemTrait>> resolvedTraits;
    private boolean aiInitialized;

    public GolemEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
        this.inventory = new SimpleContainer(2);
        this.homePos = BlockPos.ZERO;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_MATERIAL, ThaumcraftData.GolemMaterials.WOOD.location().toString());
        builder.define(DATA_HEAD, ThaumcraftData.GolemParts.HEAD_BASIC.location().toString());
        builder.define(DATA_ARMS, ThaumcraftData.GolemParts.ARM_BASIC.location().toString());
        builder.define(DATA_LEGS, ThaumcraftData.GolemParts.LEG_WALKER.location().toString());
        builder.define(DATA_ADDON, ThaumcraftData.GolemParts.ADDON_NONE.location().toString());
        builder.define(DATA_RANK, 0);
        builder.define(DATA_COLOR, (byte) -1);
        builder.define(DATA_OWNER, Optional.empty());
        builder.define(DATA_FOLLOWING, false);
    }

    @Override
    protected void registerGoals() {
        aiInitialized = true;
        rebuildGoals();
    }

    public void rebuildGoals() {
        refreshMovementController();
        goalSelector.removeAllGoals(goal -> true);
        targetSelector.removeAllGoals(goal -> true);
        Set<ResourceKey<GolemTrait>> traits = getResolvedTraits();
        boolean fighter = traits.contains(ThaumcraftData.GolemTraits.FIGHTER);
        boolean ranged = traits.contains(ThaumcraftData.GolemTraits.RANGED);
        boolean following = isFollowing();

        goalSelector.addGoal(0, new FloatGoal(this));
        if (following) {
            goalSelector.addGoal(1, new GolemFollowOwnerGoal(this, 1.0, 4.0F, 2.0F));
        } else {
            goalSelector.addGoal(2, new GolemExecuteTaskGoal(this));
            goalSelector.addGoal(5, new GolemReturnHomeGoal(this));
        }
        if (fighter) {
            if (ranged) {
                goalSelector.addGoal(3, new GolemRangedAttackGoal(this, 1.0, 16.0F));
            }
            goalSelector.addGoal(4, new GolemMeleeAttackGoal(this, 1.15, false));
        }
        goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        if (fighter) {
            if (following) {
                targetSelector.addGoal(1, new GolemOwnerHurtByTargetGoal(this));
                targetSelector.addGoal(2, new GolemOwnerHurtTargetGoal(this));
            }
            targetSelector.addGoal(3, new GolemFighterHurtByTargetGoal(this));
        }
    }

    public ResourceKey<GolemMaterial> getMaterialKey() {
        return ResourceKey.create(ThaumcraftData.Registries.GOLEM_MATERIAL, ResourceLocation.parse(entityData.get(DATA_MATERIAL)));
    }

    public void setMaterialKey(ResourceKey<GolemMaterial> key) {
        entityData.set(DATA_MATERIAL, key.location().toString());
        resolvedTraits = null;
        applyMaterialModifiers();
        if (aiInitialized) rebuildGoals();
    }

    public ResourceKey<GolemPart> getHeadKey() {
        return ResourceKey.create(ThaumcraftData.Registries.GOLEM_PART, ResourceLocation.parse(entityData.get(DATA_HEAD)));
    }

    public void setHeadKey(ResourceKey<GolemPart> key) {
        entityData.set(DATA_HEAD, key.location().toString());
        resolvedTraits = null;
        applyMaterialModifiers();
        if (aiInitialized) rebuildGoals();
    }

    public ResourceKey<GolemPart> getArmsKey() {
        return ResourceKey.create(ThaumcraftData.Registries.GOLEM_PART, ResourceLocation.parse(entityData.get(DATA_ARMS)));
    }

    public void setArmsKey(ResourceKey<GolemPart> key) {
        entityData.set(DATA_ARMS, key.location().toString());
        resolvedTraits = null;
        applyMaterialModifiers();
        if (aiInitialized) rebuildGoals();
    }

    public ResourceKey<GolemPart> getLegsKey() {
        return ResourceKey.create(ThaumcraftData.Registries.GOLEM_PART, ResourceLocation.parse(entityData.get(DATA_LEGS)));
    }

    public void setLegsKey(ResourceKey<GolemPart> key) {
        entityData.set(DATA_LEGS, key.location().toString());
        resolvedTraits = null;
        applyMaterialModifiers();
        if (aiInitialized) rebuildGoals();
    }

    public ResourceKey<GolemPart> getAddonKey() {
        return ResourceKey.create(ThaumcraftData.Registries.GOLEM_PART, ResourceLocation.parse(entityData.get(DATA_ADDON)));
    }

    public void setAddonKey(ResourceKey<GolemPart> key) {
        entityData.set(DATA_ADDON, key.location().toString());
        resolvedTraits = null;
        applyMaterialModifiers();
        if (aiInitialized) rebuildGoals();
    }

    public int getRank() {
        return entityData.get(DATA_RANK);
    }

    public void setRank(int rank) {
        entityData.set(DATA_RANK, rank);
        applyMaterialModifiers();
        refreshMovementController();
    }

    public byte getGolemColor() {
        return entityData.get(DATA_COLOR);
    }

    public void setGolemColor(byte color) {
        entityData.set(DATA_COLOR, color);
    }

    public Optional<UUID> getOwnerUUID() {
        return entityData.get(DATA_OWNER);
    }

    public void setOwnerUUID(UUID owner) {
        entityData.set(DATA_OWNER, Optional.ofNullable(owner));
    }

    public boolean isFollowing() {
        return entityData.get(DATA_FOLLOWING);
    }

    public void setFollowing(boolean following) {
        entityData.set(DATA_FOLLOWING, following);
        if (aiInitialized) rebuildGoals();
    }

    public BlockPos getHomePos() {
        return homePos;
    }

    public boolean hasHome() {
        return !homePos.equals(BlockPos.ZERO);
    }

    public void setHomePos(BlockPos pos) {
        this.homePos = pos;
    }

    public void goHome() {
        if (!hasHome() || level().isClientSide) return;
        double oldX = getX();
        double oldY = getY();
        double oldZ = getZ();
        moveTo(homePos.getX() + 0.5, homePos.getY(), homePos.getZ() + 0.5, getYRot(), getXRot());
        BlockPos check = blockPosition();
        int topY = level().getHeight();
        while (check.getY() < topY) {
            BlockPos up = check.above();
            BlockState state = level().getBlockState(up);
            if (state.blocksMotion()) {
                break;
            }
            moveTo(getX(), getY() + 1.0, getZ(), getYRot(), getXRot());
            check = up;
        }
        if (!level().noCollision(this, getBoundingBox())) {
            moveTo(oldX, oldY, oldZ, getYRot(), getXRot());
        } else {
            getNavigation().stop();
        }
    }

    public int getRankXp() {
        return rankXp;
    }

    public SimpleContainer getInventory() {
        return inventory;
    }

    public int getCarrySlotCount() {
        return hasTrait(ThaumcraftData.GolemTraits.HAULER) ? 2 : 1;
    }

    public ItemStack holdItem(ItemStack stack) {
        if (stack.isEmpty() || stack.getCount() <= 0) {
            return stack;
        }
        ItemStack working = stack.copy();
        for (int i = 0; i < getCarrySlotCount(); i++) {
            ItemStack current = inventory.getItem(i);
            if (current.isEmpty()) {
                inventory.setItem(i, working);
                return ItemStack.EMPTY;
            }
            if (ItemStack.isSameItemSameComponents(current, working) && current.getCount() < current.getMaxStackSize()) {
                int moved = Math.min(working.getCount(), current.getMaxStackSize() - current.getCount());
                current.grow(moved);
                working.shrink(moved);
                if (working.isEmpty()) {
                    return ItemStack.EMPTY;
                }
            }
        }
        return working;
    }

    public ItemStack dropItem(ItemStack stack) {
        ItemStack out = ItemStack.EMPTY;
        for (int i = 0; i < getCarrySlotCount(); i++) {
            ItemStack current = inventory.getItem(i);
            if (current.isEmpty()) continue;
            if (stack == null || stack.isEmpty()) {
                out = current.copy();
                inventory.setItem(i, ItemStack.EMPTY);
                break;
            }
            if (ItemStack.isSameItemSameComponents(current, stack)) {
                out = current.copyWithCount(Math.min(stack.getCount(), current.getCount()));
                current.shrink(out.getCount());
                if (current.isEmpty()) {
                    inventory.setItem(i, ItemStack.EMPTY);
                }
                break;
            }
        }
        if (getCarrySlotCount() > 1 && inventory.getItem(0).isEmpty() && !inventory.getItem(1).isEmpty()) {
            inventory.setItem(0, inventory.getItem(1).copy());
            inventory.setItem(1, ItemStack.EMPTY);
        }
        return out;
    }

    public int canCarryAmount(ItemStack stack) {
        if (stack.isEmpty()) return 0;
        int amount = 0;
        for (int i = 0; i < getCarrySlotCount(); i++) {
            ItemStack current = inventory.getItem(i);
            if (current.isEmpty()) {
                amount += stack.getMaxStackSize();
            } else if (ItemStack.isSameItemSameComponents(current, stack)) {
                amount += Math.max(0, current.getMaxStackSize() - current.getCount());
            }
        }
        return amount;
    }

    public boolean canCarry(ItemStack stack, boolean partial) {
        int canCarry = canCarryAmount(stack);
        if (canCarry <= 0) return false;
        return partial || canCarry >= stack.getCount();
    }

    public boolean isCarrying(ItemStack stack) {
        if (stack.isEmpty()) return false;
        for (int i = 0; i < getCarrySlotCount(); i++) {
            ItemStack current = inventory.getItem(i);
            if (!current.isEmpty() && ItemStack.isSameItemSameComponents(current, stack)) {
                return true;
            }
        }
        return false;
    }

    public Set<ResourceKey<GolemTrait>> getResolvedTraits() {
        if (resolvedTraits == null) {
            resolvedTraits = resolveTraits();
        }
        return resolvedTraits;
    }

    public boolean hasTrait(ResourceKey<GolemTrait> trait) {
        return getResolvedTraits().contains(trait);
    }

    private Set<ResourceKey<GolemTrait>> resolveTraits() {
        Set<ResourceKey<GolemTrait>> traits = new HashSet<>();

        GolemMaterial material = ConfigDataRegistries.GOLEM_MATERIALS.get(registryAccess(), getMaterialKey());
        traits.addAll(material.traits());

        addPartTraits(traits, getHeadKey());
        addPartTraits(traits, getArmsKey());
        addPartTraits(traits, getLegsKey());
        addPartTraits(traits, getAddonKey());

        Set<ResourceKey<GolemTrait>> toRemove = new HashSet<>();
        for (ResourceKey<GolemTrait> trait : traits) {
            GolemTrait traitData = ConfigDataRegistries.GOLEM_TRAITS.get(registryAccess(), trait);
            traitData.opposite().ifPresent(opposite -> {
                if (traits.contains(opposite)) {
                    toRemove.add(trait);
                    toRemove.add(opposite);
                }
            });
        }
        traits.removeAll(toRemove);

        return Collections.unmodifiableSet(traits);
    }

    private void addPartTraits(Set<ResourceKey<GolemTrait>> traits, ResourceKey<GolemPart> partKey) {
        GolemPart part = ConfigDataRegistries.GOLEM_PARTS.get(registryAccess(), partKey);
        traits.addAll(part.traits());
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ConfigSounds.CLACK.value();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ConfigSounds.CLACK.value();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ConfigSounds.TOOL.value();
    }

    public void applyMaterialModifiers() {
        GolemMaterial material = ConfigDataRegistries.GOLEM_MATERIALS.get(registryAccess(), getMaterialKey());
        Set<ResourceKey<GolemTrait>> traits = getResolvedTraits();

        double maxHealth = 10 + material.healthModifier();
        if (traits.contains(ThaumcraftData.GolemTraits.FRAGILE)) {
            maxHealth *= 0.75;
        }
        maxHealth += getRank();
        applyModifier(Attributes.MAX_HEALTH, HEALTH_MODIFIER_ID, maxHealth - 10.0, AttributeModifier.Operation.ADD_VALUE);

        double armor = material.armor();
        if (traits.contains(ThaumcraftData.GolemTraits.ARMORED)) {
            armor = Math.max(armor * 1.5, armor + 1.0);
        }
        if (traits.contains(ThaumcraftData.GolemTraits.FRAGILE)) {
            armor *= 0.75;
        }
        applyModifier(Attributes.ARMOR, ARMOR_MODIFIER_ID, armor, AttributeModifier.Operation.ADD_VALUE);

        double damage = 0.0;
        if (traits.contains(ThaumcraftData.GolemTraits.FIGHTER)) {
            damage = material.damage();
            if (traits.contains(ThaumcraftData.GolemTraits.BRUTAL)) {
                damage = Math.max(damage * 1.5, damage + 1.0);
            }
            damage += getRank() * 0.25;
        }
        applyModifier(Attributes.ATTACK_DAMAGE, DAMAGE_MODIFIER_ID, damage, AttributeModifier.Operation.ADD_VALUE);
        applyModifier(Attributes.FOLLOW_RANGE, Thaumcraft.id("golem_follow_range"),
                (traits.contains(ThaumcraftData.GolemTraits.SCOUT) ? 56.0 : 40.0) - 32.0, AttributeModifier.Operation.ADD_VALUE);

        setHealth(getMaxHealth());
    }

    private void applyModifier(Holder<Attribute> attribute, ResourceLocation id, double value, AttributeModifier.Operation op) {
        AttributeInstance instance = getAttribute(attribute);
        if (instance != null) {
            instance.removeModifier(id);
            if (value != 0) {
                instance.addPermanentModifier(new AttributeModifier(id, value, op));
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("Material", entityData.get(DATA_MATERIAL));
        tag.putString("Head", entityData.get(DATA_HEAD));
        tag.putString("Arms", entityData.get(DATA_ARMS));
        tag.putString("Legs", entityData.get(DATA_LEGS));
        tag.putString("Addon", entityData.get(DATA_ADDON));
        tag.putInt("Rank", getRank());
        tag.putInt("RankXP", rankXp);
        tag.putByte("GolemColor", getGolemColor());
        getOwnerUUID().ifPresent(uuid -> tag.putUUID("Owner", uuid));
        tag.putBoolean("Following", isFollowing());
        tag.putLong("HomePos", homePos.asLong());

        CompoundTag inv = new CompoundTag();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            if (!inventory.getItem(i).isEmpty()) {
                inv.put("Slot" + i, inventory.getItem(i).save(registryAccess()));
            }
        }
        tag.put("Inventory", inv);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Material")) entityData.set(DATA_MATERIAL, tag.getString("Material"));
        if (tag.contains("Head")) entityData.set(DATA_HEAD, tag.getString("Head"));
        if (tag.contains("Arms")) entityData.set(DATA_ARMS, tag.getString("Arms"));
        if (tag.contains("Legs")) entityData.set(DATA_LEGS, tag.getString("Legs"));
        if (tag.contains("Addon")) entityData.set(DATA_ADDON, tag.getString("Addon"));
        if (tag.contains("Rank")) setRank(tag.getInt("Rank"));
        if (tag.contains("RankXP")) rankXp = tag.getInt("RankXP");
        if (tag.contains("GolemColor")) setGolemColor(tag.getByte("GolemColor"));
        if (tag.hasUUID("Owner")) setOwnerUUID(tag.getUUID("Owner"));
        if (tag.contains("Following")) setFollowing(tag.getBoolean("Following"));
        if (tag.contains("HomePos")) homePos = BlockPos.of(tag.getLong("HomePos"));

        resolvedTraits = null;

        if (tag.contains("Inventory")) {
            CompoundTag inv = tag.getCompound("Inventory");
            for (int i = 0; i < inventory.getContainerSize(); i++) {
                String key = "Slot" + i;
                if (inv.contains(key)) {
                    inventory.setItem(i, net.minecraft.world.item.ItemStack.parse(registryAccess(), inv.getCompound(key)).orElse(net.minecraft.world.item.ItemStack.EMPTY));
                }
            }
        }

        applyMaterialModifiers();
    }

    @Override
    public boolean removeWhenFarAway(double distanceToPlayer) {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        Set<ResourceKey<GolemTrait>> traits = getResolvedTraits();
        refreshMovementController();
        alignBodyToMovement();

        if (!level().isClientSide) {
            if (getTarget() != null && !getTarget().isAlive()) {
                setTarget(null);
            }
            if (getTarget() != null && traits.contains(ThaumcraftData.GolemTraits.RANGED) && distanceToSqr(getTarget()) > 1024.0) {
                setTarget(null);
            }
            if (getTarget() instanceof Player && level().getServer() != null && !level().getServer().isPvpAllowed()) {
                setTarget(null);
            }
            if (tickCount % (traits.contains(ThaumcraftData.GolemTraits.REPAIR) ? 40 : 100) == 0) {
                heal(1.0F);
            }
        }
    }

    private void alignBodyToMovement() {
        double dx;
        double dz;

        if (isFollowing() && getOwnerUUID().isPresent()) {
            Player owner = level().getPlayerByUUID(getOwnerUUID().get());
            if (owner != null && owner.isAlive()) {
                dx = owner.getX() - getX();
                dz = owner.getZ() - getZ();
            } else {
                dx = getX() - xo;
                dz = getZ() - zo;
            }
        } else {
            dx = getX() - xo;
            dz = getZ() - zo;
        }

        if (dx * dx + dz * dz <= 1.0E-6) {
            return;
        }

        float targetYaw = (float) (Mth.atan2(dz, dx) * (180.0 / Math.PI)) - 90.0F;
        float newYaw = Mth.rotLerp(0.7F, getYRot(), targetYaw);
        setYRot(newYaw);
        yBodyRot = Mth.rotLerp(0.8F, yBodyRot, newYaw);
        yBodyRotO = yBodyRot;
        if (getTarget() == null && !isFollowing()) {
            setYHeadRot(newYaw);
        }
    }

    @Override
    public boolean onClimbable() {
        if (hasTrait(ThaumcraftData.GolemTraits.CLIMBER) && horizontalCollision) {
            return true;
        }
        return super.onClimbable();
    }

    public double getGolemMoveSpeed() {
        Set<ResourceKey<GolemTrait>> traits = getResolvedTraits();
        return 1.0
                + getRank() * 0.025
                + (traits.contains(ThaumcraftData.GolemTraits.LIGHT) ? 0.2 : 0.0)
                + (traits.contains(ThaumcraftData.GolemTraits.HEAVY) ? -0.175 : 0.0)
                + (traits.contains(ThaumcraftData.GolemTraits.FLYER) ? -0.33 : 0.0)
                + (traits.contains(ThaumcraftData.GolemTraits.WHEELED) ? 0.25 : 0.0);
    }

    public int getHomeRadius() {
        return hasTrait(ThaumcraftData.GolemTraits.SCOUT) ? 48 : 32;
    }

    public void performRangedAttack(net.minecraft.world.entity.LivingEntity target, float rangeFactor) {
        if (level().isClientSide) return;
        Arrow arrow = new Arrow(level(), this, new ItemStack(Items.ARROW), null);
        double dx = target.getX() - getX();
        double dz = target.getZ() - getZ();
        double dy = target.getY(0.3333333333333333D) - arrow.getY();
        double horizontal = Math.sqrt(dx * dx + dz * dz);
        arrow.shoot(dx, dy + horizontal * 0.2, dz, 1.6F, 12.0F - rangeFactor * 4.0F);
        level().addFreshEntity(arrow);
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, source, recentlyHit);
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            if (!inventory.getItem(i).isEmpty()) {
                spawnAtLocation(level, inventory.getItem(i));
            }
        }
    }

    private void refreshMovementController() {
        boolean flyer = hasTrait(ThaumcraftData.GolemTraits.FLYER);
        boolean climber = hasTrait(ThaumcraftData.GolemTraits.CLIMBER);

        if (flyer) {
            if (!(navigation instanceof FlyingPathNavigation)) {
                navigation.stop();
                navigation = new FlyingPathNavigation(this, level());
            }
            if (!(moveControl instanceof GolemFlyingMoveControl)) {
                moveControl = new GolemFlyingMoveControl(this);
            }
        } else {
            if (climber) {
                if (!(navigation instanceof WallClimberNavigation)) {
                    navigation.stop();
                    navigation = new WallClimberNavigation(this, level());
                }
            } else if (!(navigation instanceof GroundPathNavigation)) {
                navigation.stop();
                navigation = new GroundPathNavigation(this, level());
            }
            if (moveControl instanceof FlyingMoveControl || moveControl instanceof GolemFlyingMoveControl) {
                moveControl = new MoveControl(this);
            }
        }

        navigation.setCanFloat(true);
        setNoGravity(flyer);
    }

    private static class GolemFlyingMoveControl extends MoveControl {
        private final GolemEntity golem;

        private GolemFlyingMoveControl(GolemEntity golem) {
            super(golem);
            this.golem = golem;
        }

        @Override
        public void tick() {
            if (operation == Operation.MOVE_TO) {
                double dx = wantedX - golem.getX();
                double dy = wantedY - golem.getY();
                double dz = wantedZ - golem.getZ();
                double distSq = dx * dx + dy * dy + dz * dz;
                double dist = Math.sqrt(distSq);
                if (dist < golem.getBoundingBox().getSize()) {
                    operation = Operation.WAIT;
                    golem.setDeltaMovement(golem.getDeltaMovement().scale(0.5));
                } else {
                    Vec3 motion = golem.getDeltaMovement().add(
                            dx / dist * 0.06 * speedModifier,
                            dy / dist * 0.025 * speedModifier,
                            dz / dist * 0.06 * speedModifier
                    );
                    golem.setDeltaMovement(motion);

                    float yaw;
                    if (golem.getTarget() == null) {
                        yaw = (float) (-Mth.atan2(motion.x, motion.z) * (180.0 / Math.PI));
                    } else {
                        double tx = golem.getTarget().getX() - golem.getX();
                        double tz = golem.getTarget().getZ() - golem.getZ();
                        yaw = (float) (-Mth.atan2(tx, tz) * (180.0 / Math.PI));
                    }
                    golem.setYRot(yaw);
                    golem.yBodyRot = yaw;
                    golem.yBodyRotO = yaw;
                    if (golem.getTarget() == null && !golem.isFollowing()) {
                        golem.setYHeadRot(yaw);
                    }
                }
            }
        }
    }

}
