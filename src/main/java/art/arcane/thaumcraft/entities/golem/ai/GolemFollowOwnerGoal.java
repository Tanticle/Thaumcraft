package art.arcane.thaumcraft.entities.golem.ai;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.PathType;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.entities.golem.GolemEntity;

import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;

public class GolemFollowOwnerGoal extends Goal {

    private final GolemEntity golem;
    private final double speedModifier;
    private final float startDistance;
    private final float stopDistance;
    private Player owner;
    private int pathFindDelay;
    private float oldWaterCost;

    public GolemFollowOwnerGoal(GolemEntity golem, double speed, float startDist, float stopDist) {
        this.golem = golem;
        this.speedModifier = speed;
        this.startDistance = startDist;
        this.stopDistance = stopDist;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (!golem.isFollowing()) return false;

        Optional<UUID> ownerUuid = golem.getOwnerUUID();
        if (ownerUuid.isEmpty()) return false;

        Player player = golem.level().getPlayerByUUID(ownerUuid.get());
        if (player == null || player.isSpectator() || !player.isAlive()) return false;
        if (golem.distanceToSqr(player) < (double) startDistance * startDistance) return false;

        owner = player;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        if (!golem.isFollowing()) return false;
        if (owner == null || !owner.isAlive() || owner.isSpectator()) return false;
        return golem.distanceToSqr(owner) > (double) stopDistance * stopDistance;
    }

    @Override
    public void start() {
        pathFindDelay = 0;
        oldWaterCost = golem.getPathfindingMalus(PathType.WATER);
        golem.setPathfindingMalus(PathType.WATER, 0.0F);
        moveTowardOwner();
    }

    @Override
    public void tick() {
        golem.getLookControl().setLookAt(owner, 10.0F, golem.getMaxHeadXRot());
        if (--pathFindDelay <= 0) {
            pathFindDelay = 10;
            if (!moveTowardOwner() && golem.distanceToSqr(owner) >= 64.0D) {
                tryTeleportNearOwner();
            }
        }
    }

    @Override
    public void stop() {
        owner = null;
        golem.getNavigation().stop();
        golem.setPathfindingMalus(PathType.WATER, oldWaterCost);
    }

    private void tryTeleportNearOwner() {
        int baseX = owner.getBlockX() - 2;
        int baseZ = owner.getBlockZ() - 2;
        int y = owner.getBlockY();

        for (int dx = 0; dx <= 4; dx++) {
            for (int dz = 0; dz <= 4; dz++) {
                if (dx >= 1 && dx <= 3 && dz >= 1 && dz <= 3) continue;
                int x = baseX + dx;
                int z = baseZ + dz;
                if (!golem.level().getBlockState(new net.minecraft.core.BlockPos(x, y - 1, z)).isSolid()) continue;
                if (!golem.level().getBlockState(new net.minecraft.core.BlockPos(x, y, z)).canBeReplaced()) continue;
                if (!golem.level().getBlockState(new net.minecraft.core.BlockPos(x, y + 1, z)).canBeReplaced()) continue;

                golem.moveTo(x + 0.5, y, z + 0.5, golem.getYRot(), golem.getXRot());
                golem.getNavigation().stop();
                return;
            }
        }

        if (golem.hasTrait(ThaumcraftData.GolemTraits.FLYER)) {
            double tx = owner.getX();
            double ty = owner.getY() + 1.0;
            double tz = owner.getZ();
            var bounds = golem.getBoundingBox().move(tx - golem.getX(), ty - golem.getY(), tz - golem.getZ());
            if (golem.level().noCollision(golem, bounds)) {
                golem.moveTo(tx, ty, tz, golem.getYRot(), golem.getXRot());
                golem.getNavigation().stop();
            }
        }
    }

    private boolean moveTowardOwner() {
        boolean moved = golem.getNavigation().moveTo(owner, speedModifier * golem.getGolemMoveSpeed());
        if (!moved && golem.hasTrait(ThaumcraftData.GolemTraits.FLYER)) {
            golem.getMoveControl().setWantedPosition(owner.getX(), owner.getY() + 1.0, owner.getZ(), speedModifier * golem.getGolemMoveSpeed());
            return true;
        }
        return moved;
    }
}
