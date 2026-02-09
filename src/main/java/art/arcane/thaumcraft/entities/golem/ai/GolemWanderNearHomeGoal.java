package art.arcane.thaumcraft.entities.golem.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;
import art.arcane.thaumcraft.entities.golem.GolemEntity;

import java.util.EnumSet;

public class GolemWanderNearHomeGoal extends Goal {

    private final GolemEntity golem;
    private final double speed;
    private final int maxDistFromHome;
    private double targetX;
    private double targetY;
    private double targetZ;
    private int idleTicks;

    public GolemWanderNearHomeGoal(GolemEntity golem, double speed, int maxDistFromHome) {
        this.golem = golem;
        this.speed = speed;
        this.maxDistFromHome = maxDistFromHome;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (golem.isFollowing()) return false;
        if (golem.getNavigation().isInProgress()) return false;

        if (idleTicks > 0) {
            idleTicks--;
            return false;
        }

        BlockPos home = golem.getHomePos();
        Vec3 target = DefaultRandomPos.getPos(golem, 6, 3);
        if (target == null) return false;

        if (!home.equals(BlockPos.ZERO)) {
            double distToHome = target.distanceToSqr(home.getX() + 0.5, home.getY(), home.getZ() + 0.5);
            if (distToHome > (double) maxDistFromHome * maxDistFromHome) return false;
        }

        targetX = target.x;
        targetY = target.y;
        targetZ = target.z;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return !golem.getNavigation().isDone();
    }

    @Override
    public void start() {
        golem.getNavigation().moveTo(targetX, targetY, targetZ, speed);
    }

    @Override
    public void stop() {
        golem.getNavigation().stop();
        idleTicks = 40 + golem.getRandom().nextInt(60);
    }
}
