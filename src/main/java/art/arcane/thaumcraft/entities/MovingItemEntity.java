package art.arcane.thaumcraft.entities;

import art.arcane.thaumcraft.registries.ConfigEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.UUID;

public class MovingItemEntity extends ItemEntity {

	private static final EntityDataAccessor<Optional<UUID>> DATA_TARGET_UUID =
		SynchedEntityData.defineId(MovingItemEntity.class, EntityDataSerializers.OPTIONAL_UUID);

	private Entity cachedTarget;

	public MovingItemEntity(EntityType<? extends ItemEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.noPhysics = true;
		this.setNoGravity(true);
	}

	public MovingItemEntity(ItemEntity entity, Entity target) {
		this(entity.level(), entity.position(), entity.getItem(), target);
	}

	public MovingItemEntity(Level pLevel, Vec3 position, ItemStack stack, Entity target) {
		super(ConfigEntities.MOVING_ITEM.entityType(), pLevel);
		this.setPos(position.x(), position.y(), position.z());
		this.setItem(stack);
		if (target != null) {
			this.entityData.set(DATA_TARGET_UUID, Optional.of(target.getUUID()));
			this.cachedTarget = target;
		}
		this.noPhysics = true;
		this.setNoGravity(true);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(DATA_TARGET_UUID, Optional.empty());
	}

	private Entity getTargetEntity() {
		Optional<UUID> targetUUID = this.entityData.get(DATA_TARGET_UUID);
		if (targetUUID.isEmpty()) {
			cachedTarget = null;
			return null;
		}

		if (cachedTarget != null && cachedTarget.isAlive() && cachedTarget.getUUID().equals(targetUUID.get())) {
			return cachedTarget;
		}

		if (level() instanceof ServerLevel serverLevel) {
			cachedTarget = serverLevel.getEntity(targetUUID.get());
		} else if (level().isClientSide()) {
			Minecraft mc = Minecraft.getInstance();
			if (mc.player != null && mc.player.getUUID().equals(targetUUID.get())) {
				cachedTarget = mc.player;
			} else {
				for (Entity entity : level().getEntities(this, getBoundingBox().inflate(64), e -> e.getUUID().equals(targetUUID.get()))) {
					cachedTarget = entity;
					break;
				}
			}
		}
		return cachedTarget;
	}

	private void clearTarget() {
		this.entityData.set(DATA_TARGET_UUID, Optional.empty());
		this.cachedTarget = null;
	}

	private boolean hasTarget() {
		return this.entityData.get(DATA_TARGET_UUID).isPresent();
	}

	@Override
	public void tick() {
		Vec3 previousPosition = position();
		Entity target = getTargetEntity();

		if (target != null && target.isAlive()) {
			this.noPhysics = true;
			this.setNoGravity(true);

			Vec3 targetPosition = target.getEyePosition();
			double distance = targetPosition.distanceTo(this.position());

			if (distance > 0.5) {
				Vec3 direction = targetPosition.subtract(position()).normalize();
				double speed = Math.min(0.4, distance * 0.15);
				Vec3 newPos = position().add(direction.scale(speed));
				this.setPos(newPos.x, newPos.y, newPos.z);
				this.setDeltaMovement(Vec3.ZERO);
			} else {
				clearTarget();
				this.noPhysics = false;
				this.setNoGravity(false);
			}

			this.hasImpulse = true;
			this.hurtMarked = true;
		}

		if (!hasTarget()) {
			super.tick();
		} else {
			this.tickCount++;
			this.checkBelowWorld();
		}

		if (level().isClientSide() && hasTarget()) {
			Vec3 currentPosition = position();
			Vec3 delta = currentPosition.subtract(previousPosition);
			double trailLength = delta.length();
			int particleCount = Math.max(3, (int) (trailLength * 20));
			for (int i = 0; i < particleCount; i++) {
				double t = (double) i / particleCount;
				double x = previousPosition.x() + delta.x() * t;
				double y = previousPosition.y() + delta.y() * t + 0.25;
				double z = previousPosition.z() + delta.z() * t;
				level().addParticle(ParticleTypes.ENCHANT,
					x + (random.nextDouble() - 0.5) * 0.15,
					y + (random.nextDouble() - 0.5) * 0.15,
					z + (random.nextDouble() - 0.5) * 0.15,
					0, 0.02, 0);
			}
		}
	}

	@Override
	public void move(MoverType type, Vec3 movement) {
		if (hasTarget()) {
			return;
		}
		super.move(type, movement);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		Optional<UUID> targetUUID = this.entityData.get(DATA_TARGET_UUID);
		if (targetUUID.isPresent()) {
			tag.putUUID("TargetUUID", targetUUID.get());
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (tag.hasUUID("TargetUUID")) {
			this.entityData.set(DATA_TARGET_UUID, Optional.of(tag.getUUID("TargetUUID")));
		}
	}
}
