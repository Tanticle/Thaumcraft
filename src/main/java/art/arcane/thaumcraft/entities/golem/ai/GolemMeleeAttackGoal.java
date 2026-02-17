package art.arcane.thaumcraft.entities.golem.ai;

import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.entities.golem.GolemEntity;

public class GolemMeleeAttackGoal extends MeleeAttackGoal {

    private final GolemEntity golem;

    public GolemMeleeAttackGoal(GolemEntity golem, double speedModifier, boolean followingTargetEvenIfNotSeen) {
        super(golem, speedModifier, followingTargetEvenIfNotSeen);
        this.golem = golem;
    }

    @Override
    public boolean canUse() {
        return golem.hasTrait(ThaumcraftData.GolemTraits.FIGHTER)
                && (!golem.hasTrait(ThaumcraftData.GolemTraits.RANGED)
                || golem.getTarget() == null
                || golem.distanceToSqr(golem.getTarget()) < 9.0D)
                && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return golem.hasTrait(ThaumcraftData.GolemTraits.FIGHTER) && super.canContinueToUse();
    }
}
