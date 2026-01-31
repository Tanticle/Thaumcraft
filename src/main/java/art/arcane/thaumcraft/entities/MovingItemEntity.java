package art.arcane.thaumcraft.entities;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class MovingItemEntity extends ItemEntity {

    private Entity targetEntity;

    public MovingItemEntity(EntityType<? extends ItemEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public MovingItemEntity(ItemEntity entity, Entity target) {
        this(entity.level(), entity.position(), entity.getItem(), target);
        this.setDeltaMovement(entity.getDeltaMovement());
    }

    public MovingItemEntity(Level pLevel, Vec3 position, ItemStack stack, Entity target) {
        super(pLevel, position.x(), position.y(), position.z(), stack);
        this.targetEntity = target;
        this.noPhysics = true;
    }

    @Override
    public void tick() {
        if(targetEntity != null && targetEntity.isAlive()) {
            Vec3 targetPosition = targetEntity.getEyePosition();
            double distance = targetPosition.distanceTo(this.position());
            if(distance > 0.5F) {
                Vec3 speed = targetPosition.subtract(position());
                speed = new Vec3(speed.x() / distance, speed.y() / distance, speed.z() / distance);
                setDeltaMovement(speed);
            } else {
                setDeltaMovement(getDeltaMovement().multiply(0.1, 0.1, 0.1));
                targetEntity = null;
                this.noPhysics = false;
            }
            if(level().isClientSide()) {
                //TODO: Farticles
            }
        }
        super.tick();
    }
}
