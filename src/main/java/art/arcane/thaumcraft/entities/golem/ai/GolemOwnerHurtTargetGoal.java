package art.arcane.thaumcraft.entities.golem.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.entities.golem.GolemEntity;

import java.util.Optional;
import java.util.UUID;

public class GolemOwnerHurtTargetGoal extends TargetGoal {

    private final GolemEntity golem;
    private LivingEntity ownerTarget;
    private int timestamp;

    public GolemOwnerHurtTargetGoal(GolemEntity golem) {
        super(golem, false);
        this.golem = golem;
    }

    @Override
    public boolean canUse() {
        if (!golem.hasTrait(ThaumcraftData.GolemTraits.FIGHTER) || !golem.isFollowing()) return false;
        Optional<UUID> ownerUuid = golem.getOwnerUUID();
        if (ownerUuid.isEmpty()) return false;
        Player owner = golem.level().getPlayerByUUID(ownerUuid.get());
        if (owner == null) return false;
        ownerTarget = owner.getLastHurtMob();
        int attackTime = owner.getLastHurtMobTimestamp();
        return attackTime != timestamp && canAttack(ownerTarget, TargetingConditions.DEFAULT);
    }

    @Override
    public void start() {
        mob.setTarget(ownerTarget);
        Optional<UUID> ownerUuid = golem.getOwnerUUID();
        if (ownerUuid.isPresent()) {
            Player owner = golem.level().getPlayerByUUID(ownerUuid.get());
            if (owner != null) {
                timestamp = owner.getLastHurtMobTimestamp();
            }
        }
        super.start();
    }
}
