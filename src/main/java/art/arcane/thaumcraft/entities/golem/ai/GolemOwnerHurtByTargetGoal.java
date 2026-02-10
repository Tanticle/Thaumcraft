package art.arcane.thaumcraft.entities.golem.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.entities.golem.GolemEntity;

import java.util.Optional;
import java.util.UUID;

public class GolemOwnerHurtByTargetGoal extends TargetGoal {

    private final GolemEntity golem;
    private LivingEntity ownerAttacker;
    private int timestamp;

    public GolemOwnerHurtByTargetGoal(GolemEntity golem) {
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
        ownerAttacker = owner.getLastHurtByMob();
        int revengeTime = owner.getLastHurtByMobTimestamp();
        return revengeTime != timestamp && canAttack(ownerAttacker, TargetingConditions.DEFAULT);
    }

    @Override
    public void start() {
        mob.setTarget(ownerAttacker);
        Optional<UUID> ownerUuid = golem.getOwnerUUID();
        if (ownerUuid.isPresent()) {
            Player owner = golem.level().getPlayerByUUID(ownerUuid.get());
            if (owner != null) {
                timestamp = owner.getLastHurtByMobTimestamp();
            }
        }
        super.start();
    }
}
