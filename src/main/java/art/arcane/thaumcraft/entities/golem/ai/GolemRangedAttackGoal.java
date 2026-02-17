package art.arcane.thaumcraft.entities.golem.ai;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.entities.golem.GolemEntity;

import java.util.EnumSet;

public class GolemRangedAttackGoal extends Goal {

    private final GolemEntity golem;
    private final double speedModifier;
    private final float attackRadius;
    private final float attackRadiusSqr;
    private int attackCooldown = -1;

    public GolemRangedAttackGoal(GolemEntity golem, double speedModifier, float attackRadius) {
        this.golem = golem;
        this.speedModifier = speedModifier;
        this.attackRadius = attackRadius;
        this.attackRadiusSqr = attackRadius * attackRadius;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = golem.getTarget();
        return target != null
                && target.isAlive()
                && golem.hasTrait(ThaumcraftData.GolemTraits.FIGHTER)
                && golem.hasTrait(ThaumcraftData.GolemTraits.RANGED);
    }

    @Override
    public boolean canContinueToUse() {
        return canUse();
    }

    @Override
    public void stop() {
        attackCooldown = -1;
    }

    @Override
    public void tick() {
        LivingEntity target = golem.getTarget();
        if (target == null) return;

        double distanceSqr = golem.distanceToSqr(target.getX(), target.getY(), target.getZ());
        boolean canSee = golem.getSensing().hasLineOfSight(target);

        golem.getLookControl().setLookAt(target, 30.0F, 30.0F);
        if (distanceSqr <= attackRadiusSqr && canSee) {
            golem.getNavigation().stop();
        } else {
            golem.getNavigation().moveTo(target, speedModifier * golem.getGolemMoveSpeed());
        }

        if (--attackCooldown <= 0) {
            if (distanceSqr <= attackRadiusSqr && canSee) {
                float distanceFactor = Mth.sqrt((float) distanceSqr) / attackRadius;
                golem.performRangedAttack(target, Mth.clamp(distanceFactor, 0.1F, 1.0F));
                attackCooldown = 25;
            } else {
                attackCooldown = 5;
            }
        }
    }
}
