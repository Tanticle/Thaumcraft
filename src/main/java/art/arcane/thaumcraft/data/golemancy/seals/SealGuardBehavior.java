package art.arcane.thaumcraft.data.golemancy.seals;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Monster;
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
        List<Monster> mobs = level.getEntitiesOfClass(Monster.class, area);

        for (Monster mob : mobs) {
            if (!manager.hasEntityTask(seal.getSealPos(), mob.getId())) {
                manager.addTask(GolemTask.entityTask(seal.getSealPos(), mob.blockPosition(), mob.getId(), seal.getPriority()));
            }
        }
    }

    @Override
    public boolean onTaskCompleted(ServerLevel level, GolemEntity golem, GolemTask task) {
        net.minecraft.world.entity.Entity entity = level.getEntity(task.getEntityId());
        if (entity instanceof Mob mob && mob.isAlive()) {
            golem.setTarget(mob);
            return true;
        }
        return false;
    }

    @Override
    public boolean canGolemPerformTask(GolemEntity golem, GolemTask task) {
        return golem.hasTrait(ThaumcraftData.GolemTraits.FIGHTER);
    }
}
