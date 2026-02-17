package art.arcane.thaumcraft.data.golemancy.seals;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.data.golemancy.GolemTask;
import art.arcane.thaumcraft.data.golemancy.GolemTaskManager;
import art.arcane.thaumcraft.data.golemancy.SealInstance;
import art.arcane.thaumcraft.entities.golem.GolemEntity;

import java.util.List;

public class SealGuardBehavior extends AbstractSealBehavior {

    @Override
    public void tickSeal(ServerLevel level, SealInstance seal) {
        GolemTaskManager manager = GolemTaskManager.get(level);
        BlockPos.MutableBlockPos min = scanMin(seal);
        BlockPos.MutableBlockPos max = scanMax(seal);

        AABB area = new AABB(min.getX(), min.getY(), min.getZ(), max.getX() + 1, max.getY() + 1, max.getZ() + 1);
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, area);
        boolean targetMob = seal.getToggle("pmob", true);
        boolean targetAnimal = seal.getToggle("panimal", false);
        boolean targetPlayer = seal.getToggle("pplayer", false) && level.getServer() != null && level.getServer().isPvpAllowed();

        for (LivingEntity target : entities) {
            if (!isValidTarget(target, targetMob, targetAnimal, targetPlayer)) continue;
            if (!manager.hasEntityTask(seal.getSealPos(), target.getId())) {
                manager.addTask(GolemTask.entityTask(seal.getSealPos(), target.blockPosition(), target.getId(), seal.getPriority()));
            }
        }
    }

    @Override
    public void onTaskStarted(ServerLevel level, GolemEntity golem, GolemTask task) {
        net.minecraft.world.entity.Entity entity = level.getEntity(task.getEntityId());
        if (entity instanceof Mob mob && mob.isAlive()) {
            golem.setTarget(mob);
        }
        task.setSuspended(true);
    }

    @Override
    public boolean onTaskCompleted(ServerLevel level, GolemEntity golem, GolemTask task) {
        return false;
    }

    @Override
    public boolean canGolemPerformTask(GolemEntity golem, GolemTask task) {
        if (!golem.hasTrait(ThaumcraftData.GolemTraits.FIGHTER)) return false;
        if (golem.level() instanceof ServerLevel level) {
            SealInstance seal = art.arcane.thaumcraft.data.golemancy.SealSavedData.get(level).getSeal(task.sealPos());
            if (seal != null && seal.getToggle("ranged_only", false) && !golem.hasTrait(ThaumcraftData.GolemTraits.RANGED)) {
                return false;
            }
            net.minecraft.world.entity.Entity target = level.getEntity(task.getEntityId());
            if (target instanceof LivingEntity living && golem.isAlliedTo(living)) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidTarget(LivingEntity target, boolean targetMob, boolean targetAnimal, boolean targetPlayer) {
        if (targetMob && target instanceof Monster) return true;
        if (targetAnimal && target instanceof Animal) return true;
        if (targetPlayer && target instanceof Player) return true;
        return false;
    }
}
