package art.arcane.thaumcraft.data.golemancy.seals;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.phys.AABB;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.data.golemancy.GolemTask;
import art.arcane.thaumcraft.data.golemancy.GolemTaskManager;
import art.arcane.thaumcraft.data.golemancy.SealInstance;
import art.arcane.thaumcraft.entities.golem.GolemEntity;

import java.util.List;

public class SealButcherBehavior extends AbstractSealBehavior {

    @Override
    public void tickSeal(ServerLevel level, SealInstance seal) {
        GolemTaskManager manager = GolemTaskManager.get(level);
        BlockPos.MutableBlockPos min = scanMin(seal);
        BlockPos.MutableBlockPos max = scanMax(seal);

        AABB area = new AABB(min.getX(), min.getY(), min.getZ(), max.getX() + 1, max.getY() + 1, max.getZ() + 1);
        List<Animal> animals = level.getEntitiesOfClass(Animal.class, area);

        boolean adultsOnly = seal.getToggle("adults_only", true);

        for (Animal animal : animals) {
            if (adultsOnly && animal.isBaby()) continue;
            if (!manager.hasEntityTask(seal.getSealPos(), animal.getId())) {
                manager.addTask(GolemTask.entityTask(seal.getSealPos(), animal.blockPosition(), animal.getId(), seal.getPriority()));
            }
        }
    }

    @Override
    public void onTaskStarted(ServerLevel level, GolemEntity golem, GolemTask task) {
        net.minecraft.world.entity.Entity entity = level.getEntity(task.getEntityId());
        if (entity instanceof Animal animal && animal.isAlive()) {
            golem.setTarget(animal);
        }
        task.setSuspended(true);
    }

    @Override
    public boolean onTaskCompleted(ServerLevel level, GolemEntity golem, GolemTask task) {
        return false;
    }

    @Override
    public boolean canGolemPerformTask(GolemEntity golem, GolemTask task) {
        return golem.hasTrait(ThaumcraftData.GolemTraits.FIGHTER);
    }
}
