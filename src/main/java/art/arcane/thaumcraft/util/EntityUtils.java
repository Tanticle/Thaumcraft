package art.arcane.thaumcraft.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public final class EntityUtils {

    public static List<Entity> getEntitiesInRange(Level world, BlockPos pos, double range, Entity entity, EntityType<?> type) {
        return getEntitiesInRange(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, range, entity, type);
    }

    public static List<Entity> getEntitiesInRange(Level world, Vec3 pos, double range, Entity entity, EntityType<?> type) {
        return getEntitiesInRange(world, pos.x() + 0.5, pos.y() + 0.5, pos.z() + 0.5, range, entity, type);
    }

    public static List<Entity> getEntitiesInRange(Level world, double x, double y, double z, double range, Entity entity, EntityType<?> type) {
        return world.getEntities(entity, new AABB(x, y, z, x, y, z).inflate(range, range, range), e -> e.getType() == type);
    }

    public static BlockHitResult rayTrace(Level worldIn, Entity entityIn, boolean useLiquids) {
        double d3 = 5.0;
        if (entityIn instanceof ServerPlayer p) {
            d3 = p.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE);
        }
        return rayTrace(worldIn, entityIn, useLiquids, d3);
    }

    public static BlockHitResult rayTrace(Level worldIn, Entity entityIn, boolean useLiquids, double range) {
        float f = entityIn.getXRot();
        float f2 = entityIn.getXRot();
        Vec3 vec3d = entityIn.getEyePosition();
        float f3 = Mth.cos(-f2 * 0.017453292f - 3.1415927f);
        float f4 = Mth.sin(-f2 * 0.017453292f - 3.1415927f);
        float f5 = -Mth.cos(-f * 0.017453292f);
        float f6 = Mth.sin(-f * 0.017453292f);
        float f7 = f4 * f5;
        float f8 = f3 * f5;
        Vec3 vec3d2 = vec3d.add(f7 * range, f6 * range, f8 * range);
        return worldIn.clip(new ClipContext(vec3d, vec3d2, ClipContext.Block.OUTLINE, useLiquids ? ClipContext.Fluid.ANY : ClipContext.Fluid.NONE, entityIn));
    }

    public static Vec3 getHandPosition(LivingEntity entity, InteractionHand hand) {
        float yaw = entity.getYRot() * Mth.DEG_TO_RAD;
        float pitch = entity.getXRot() * Mth.DEG_TO_RAD;

        double handOffset = hand == InteractionHand.MAIN_HAND ? -0.4 : 0.4;
        if (entity.getMainArm() == net.minecraft.world.entity.HumanoidArm.LEFT) {
            handOffset = -handOffset;
        }

        double sideX = -Mth.sin(yaw) * handOffset;
        double sideZ = Mth.cos(yaw) * handOffset;

        double forwardX = -Mth.sin(yaw) * Mth.cos(pitch) * 0.5;
        double forwardY = -Mth.sin(pitch) * 0.5;
        double forwardZ = Mth.cos(yaw) * Mth.cos(pitch) * 0.5;

        double x = entity.getX() + sideX + forwardX;
        double y = entity.getY() + entity.getEyeHeight() - 0.2 + forwardY;
        double z = entity.getZ() + sideZ + forwardZ;

        return new Vec3(x, y, z);
    }
}
