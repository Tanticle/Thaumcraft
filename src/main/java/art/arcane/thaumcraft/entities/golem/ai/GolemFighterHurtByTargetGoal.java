package art.arcane.thaumcraft.entities.golem.ai;

import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.entities.golem.GolemEntity;

public class GolemFighterHurtByTargetGoal extends HurtByTargetGoal {

    private final GolemEntity golem;

    public GolemFighterHurtByTargetGoal(GolemEntity golem) {
        super(golem);
        this.golem = golem;
    }

    @Override
    public boolean canUse() {
        return golem.hasTrait(ThaumcraftData.GolemTraits.FIGHTER) && super.canUse();
    }
}
