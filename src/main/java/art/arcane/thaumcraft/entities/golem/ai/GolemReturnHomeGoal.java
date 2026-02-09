package art.arcane.thaumcraft.entities.golem.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import art.arcane.thaumcraft.entities.golem.GolemEntity;

import java.util.EnumSet;

public class GolemReturnHomeGoal extends Goal {

    private final GolemEntity golem;
    private static final double MAX_DISTANCE_SQ = 32.0 * 32.0;

    public GolemReturnHomeGoal(GolemEntity golem) {
        this.golem = golem;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        BlockPos home = golem.getHomePos();
        if (home.equals(BlockPos.ZERO)) return false;
        return golem.blockPosition().distSqr(home) > MAX_DISTANCE_SQ;
    }

    @Override
    public boolean canContinueToUse() {
        BlockPos home = golem.getHomePos();
        if (home.equals(BlockPos.ZERO)) return false;
        if (golem.getNavigation().isDone()) return false;
        return golem.blockPosition().distSqr(home) > 4.0;
    }

    @Override
    public void start() {
        BlockPos home = golem.getHomePos();
        golem.getNavigation().moveTo(home.getX() + 0.5, home.getY(), home.getZ() + 0.5, 1.0);
    }

    @Override
    public void stop() {
        golem.getNavigation().stop();
    }
}
