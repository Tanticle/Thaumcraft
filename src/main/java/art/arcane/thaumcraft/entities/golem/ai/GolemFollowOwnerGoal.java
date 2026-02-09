package art.arcane.thaumcraft.entities.golem.ai;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
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
        golem.getNavigation().moveTo(owner, speedModifier);
    }

    @Override
    public void tick() {
        golem.getLookControl().setLookAt(owner, 10.0F, golem.getMaxHeadXRot());
        if (--pathFindDelay <= 0) {
            pathFindDelay = 10;
            golem.getNavigation().moveTo(owner, speedModifier);
        }
    }

    @Override
    public void stop() {
        owner = null;
        golem.getNavigation().stop();
    }
}
