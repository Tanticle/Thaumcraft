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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.data.golemancy.GolemMaterial;
import art.arcane.thaumcraft.data.golemancy.GolemPart;
import art.arcane.thaumcraft.data.golemancy.GolemTrait;
import art.arcane.thaumcraft.entities.golem.ai.GolemExecuteTaskGoal;
import art.arcane.thaumcraft.entities.golem.ai.GolemFollowOwnerGoal;
import art.arcane.thaumcraft.entities.golem.ai.GolemReturnHomeGoal;
import art.arcane.thaumcraft.entities.golem.ai.GolemWanderNearHomeGoal;
import art.arcane.thaumcraft.registries.ConfigDataRegistries;

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

    public GolemEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
        this.inventory = new SimpleContainer(1);
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
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new GolemFollowOwnerGoal(this, 1.0, 10.0F, 2.0F));
        goalSelector.addGoal(2, new GolemExecuteTaskGoal(this));
        goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0, true));
        goalSelector.addGoal(4, new GolemReturnHomeGoal(this));
        goalSelector.addGoal(5, new GolemWanderNearHomeGoal(this, 0.6, 16));
        goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    public ResourceKey<GolemMaterial> getMaterialKey() {
        return ResourceKey.create(ThaumcraftData.Registries.GOLEM_MATERIAL, ResourceLocation.parse(entityData.get(DATA_MATERIAL)));
    }

    public void setMaterialKey(ResourceKey<GolemMaterial> key) {
        entityData.set(DATA_MATERIAL, key.location().toString());
        resolvedTraits = null;
    }

    public ResourceKey<GolemPart> getHeadKey() {
        return ResourceKey.create(ThaumcraftData.Registries.GOLEM_PART, ResourceLocation.parse(entityData.get(DATA_HEAD)));
    }

    public void setHeadKey(ResourceKey<GolemPart> key) {
        entityData.set(DATA_HEAD, key.location().toString());
        resolvedTraits = null;
    }

    public ResourceKey<GolemPart> getArmsKey() {
        return ResourceKey.create(ThaumcraftData.Registries.GOLEM_PART, ResourceLocation.parse(entityData.get(DATA_ARMS)));
    }

    public void setArmsKey(ResourceKey<GolemPart> key) {
        entityData.set(DATA_ARMS, key.location().toString());
        resolvedTraits = null;
    }

    public ResourceKey<GolemPart> getLegsKey() {
        return ResourceKey.create(ThaumcraftData.Registries.GOLEM_PART, ResourceLocation.parse(entityData.get(DATA_LEGS)));
    }

    public void setLegsKey(ResourceKey<GolemPart> key) {
        entityData.set(DATA_LEGS, key.location().toString());
        resolvedTraits = null;
    }

    public ResourceKey<GolemPart> getAddonKey() {
        return ResourceKey.create(ThaumcraftData.Registries.GOLEM_PART, ResourceLocation.parse(entityData.get(DATA_ADDON)));
    }

    public void setAddonKey(ResourceKey<GolemPart> key) {
        entityData.set(DATA_ADDON, key.location().toString());
        resolvedTraits = null;
    }

    public int getRank() {
        return entityData.get(DATA_RANK);
    }

    public void setRank(int rank) {
        entityData.set(DATA_RANK, rank);
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
    }

    public BlockPos getHomePos() {
        return homePos;
    }

    public void setHomePos(BlockPos pos) {
        this.homePos = pos;
    }

    public int getRankXp() {
        return rankXp;
    }

    public SimpleContainer getInventory() {
        return inventory;
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

    private SoundType getMaterialSoundType() {
        String mat = entityData.get(DATA_MATERIAL);
        if (mat.contains("wood")) return SoundType.WOOD;
        if (mat.contains("clay")) return SoundType.GRAVEL;
        return SoundType.METAL;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return getMaterialSoundType().getStepSound();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return getMaterialSoundType().getHitSound();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return getMaterialSoundType().getBreakSound();
    }

    public void applyMaterialModifiers() {
        GolemMaterial material = ConfigDataRegistries.GOLEM_MATERIALS.get(registryAccess(), getMaterialKey());

        applyModifier(Attributes.MAX_HEALTH, HEALTH_MODIFIER_ID, material.healthModifier(), AttributeModifier.Operation.ADD_VALUE);
        applyModifier(Attributes.ARMOR, ARMOR_MODIFIER_ID, material.armor(), AttributeModifier.Operation.ADD_VALUE);
        applyModifier(Attributes.ATTACK_DAMAGE, DAMAGE_MODIFIER_ID, material.damage(), AttributeModifier.Operation.ADD_VALUE);

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
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, source, recentlyHit);
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            if (!inventory.getItem(i).isEmpty()) {
                spawnAtLocation(level, inventory.getItem(i));
            }
        }
    }
}
